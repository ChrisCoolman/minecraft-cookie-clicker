package com.cookie;


import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;
import org.lwjgl.glfw.GLFW;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CookieclickerClient implements ClientModInitializer {

	public static boolean gameEnabled = true;

	private float totalTickProgress = 0;
	private float cookieScale = 4.0f;

	private boolean wasPressed = false;

	private float ticksPassed = 0;

	private final List<FloatingText> activeTexts = new ArrayList<>();

	@Override
	public void onInitializeClient() {

		Cookie.start();
		Cookie.load();

		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath("cookieclicker", "last_element"),
				hudLayer()
		);

		KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Cookieclicker.MOD_ID, "cookie_clicker"));

		KeyMapping openBuildings = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.open_buildings_screen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY));
		KeyMapping openUpgrades = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.open_upgrades_screen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));
		KeyMapping toggleGame = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.toggle_game", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, CATEGORY));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean isPressed = client.mouseHandler.isLeftPressed();

			if (isPressed && !wasPressed && gameEnabled) {
				cookieScale = 8.0f;
				Cookie.mouseDown(gameEnabled);

				activeTexts.add(new FloatingText(0, 0, "+" + Cookie.format(Cookie.cookiesPerClick)));
			}

			wasPressed = isPressed;

			activeTexts.removeIf(t -> {
				t.tick();
				return t.age >= 1.0; // Keep age between 0.0 and 1.0
			});

			if (cookieScale > 4.0f) {
				cookieScale -= 0.5f;
			}

			// Runs every second - 20 minecraft ticks is one second
			ticksPassed += 1;
			if(ticksPassed >= 20) {
				Cookie.tick();
				ticksPassed = 0;
			}

			// If open Buildings
			while (openBuildings.consumeClick()) {
				if(client.player != null) {
					//client.player.displayClientMessage(Component.literal("Opening cookie clicker!"), false);
					Minecraft.getInstance().setScreen( new CookieScreen(Component.empty()));
				}
			}
			// If open Upgrades
			while (openUpgrades.consumeClick()) {
				if(client.player != null) {
					//client.player.displayClientMessage(Component.literal("Opening cookie clicker!"), false);
					Minecraft.getInstance().setScreen( new UpgradeScreen(Component.empty()));
				}
			}
			// toggle game
			while (toggleGame.consumeClick()) {
				if(client.player != null) {
					//client.player.displayClientMessage(Component.literal("Opening cookie clicker!"), false);
					gameEnabled = !gameEnabled;
				}
			}
		});
		// Save on exit
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			Cookie.save();
		});
	}

	private HudElement hudLayer() {
		return (graphics, deltaTracker) -> {
			if (gameEnabled) {
				var client = Minecraft.getInstance();

				if(client.screen != null) {
					return;
				}
				totalTickProgress += deltaTracker.getGameTimeDeltaPartialTick(true);
				Matrix3x2fStack matrices = graphics.pose();

				// Calculate Scale and Rotation
				float idlePulse = Mth.sin(totalTickProgress / 20F) * 0.1f;
				float finalScale = cookieScale + idlePulse;

				// TextRenderer, text (string, or Text object), x, y, color, shadow
				graphics.drawString(client.font, "Cookies: " + Cookie.format(Cookie.cookies), 2, 5, 0xFFFFFFFF, true);
				graphics.drawString(client.font, "Cookies Per Second: " + Cookie.format(Cookie.cookiesPerSecond), 2, 15, 0xFFFFFFFF, true);
				graphics.drawString(client.font, "Max Cookies: " + Cookie.format(Cookie.maxCookies), 2, 25, 0xFFFFFFFF, true);
				graphics.drawString(client.font, "Milk%: " + Cookie.format(BigDecimal.valueOf(Cookie.getMilk()).setScale(2, RoundingMode.HALF_UP)), 2, 35, 0xFFFFFFFF, true);

				//graphics.drawString(client.font, "X: " + client.mouseHandler.xpos(), 2, 45, 0xFFFFFFFF, true);
				//graphics.drawString(client.font, "Y: " + client.mouseHandler.ypos(), 2, 55, 0xFFFFFFFF, true);

				// Draw milk
				graphics.fill(0, Cookie.milkSize(graphics.guiHeight(), Cookie.getMilk()), graphics.guiWidth(), graphics.guiHeight(), 0x66FFFFFF);

				// Push state
				matrices.pushMatrix();

				// Move the origin to the center of the screen
				matrices.translate(graphics.guiWidth()/2f, graphics.guiHeight()/2f);

				matrices.scale(finalScale, finalScale);
				// cookie texture
				Identifier texture = Identifier.fromNamespaceAndPath("minecraft", "textures/item/cookie.png");
				// renderLayer, texture, x, y, u, v, width, height, textureWidth, textureHeight
				graphics.blit(RenderPipelines.GUI_TEXTURED, texture, -8, -8, 0, 0, 16, 16, 16, 16);
				matrices.popMatrix();

				matrices.pushMatrix();
				matrices.translate(graphics.guiWidth()/2f, graphics.guiHeight()/2f);
				matrices.scale(1.5f, 1.5f);
				// Draw floating text
				for(FloatingText t : activeTexts) {
					float fadeOut = (5.0f - (float)t.age) / 5.0f;
					int alpha = (int)(Mth.clamp(fadeOut, 0.0f, 1.0f) * 255);
					int color = (alpha << 24) | 0xFFFFFF;
					graphics.drawString(client.font, t.text, (int)t.x, (int)t.y, color, true);
				}
				matrices.popMatrix();
			}
		};
	}
}