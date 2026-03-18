package com.cookie;


import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
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

public class CookieclickerClient implements ClientModInitializer {

	private float totalTickProgress = 0;
	private float cookieScale = 4.0f;

	private boolean wasPressed = false;

	private float ticksPassed = 0;

	@Override
	public void onInitializeClient() {

		Cookie.start();

		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath("cookieclicker", "last_element"),
				hudLayer()
		);

		KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Cookieclicker.MOD_ID, "cookie_clicker"));

		KeyMapping openBuildings = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.open_buildings_screen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY));
		KeyMapping openUpgrades = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.open_upgrades_screen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean isPressed = client.mouseHandler.isLeftPressed();

			if (isPressed && !wasPressed) {
				cookieScale = 8.0f;
				Cookie.mouseDown();
			}

			wasPressed = isPressed;

			if (cookieScale > 4.0f) {
				cookieScale -= 0.2f;
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
		});
	}

	private HudElement hudLayer() {
		return (graphics, deltaTracker) -> {

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
			graphics.drawString(client.font, "Milk%: " + Cookie.format(BigDecimal.valueOf(Cookie.getMilk())), 2, 35, 0xFFFFFFFF, true);

			// Draw milk
			graphics.fill(0, Cookie.milkSize(graphics.guiHeight(), Cookie.getMilk()), graphics.guiWidth(), graphics.guiHeight(), 0x66FFFFFF);

			// Push state
			matrices.pushMatrix();

			// Move the origin to the center of the screen
			matrices.translate(graphics.guiWidth()/2f, graphics.guiHeight()/2f);

			matrices.scale(finalScale, finalScale);
			// This draws a 40x40 color square centered at the current matrix position.
			// Parameters: (x1, y1, x2, y2, color)
			// We use -20 to 20 so the center of the square is at the rotation point.
			//graphics.fillGradient(-20, -20, 20, 20, 0xFFFF0000, 0xFF0000FF); // 0xFFFF0000 is Solid Red

			// cookie texture
			Identifier texture = Identifier.fromNamespaceAndPath("minecraft", "textures/item/cookie.png");
			// renderLayer, texture, x, y, u, v, width, height, textureWidth, textureHeight
			graphics.blit(RenderPipelines.GUI_TEXTURED, texture, -8, -8, 0, 0, 16, 16, 16, 16);

			// Move the origin to the top left corner
			matrices.translate(graphics.guiWidth(), graphics.guiHeight());

			matrices.popMatrix();
		};
	}
}