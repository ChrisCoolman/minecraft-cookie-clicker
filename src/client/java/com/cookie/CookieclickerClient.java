package com.cookie;


import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
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

import static com.cookie.Cookie.clickFrenzy;
import static com.cookie.Cookie.million;

public class CookieclickerClient implements ClientModInitializer {

	public static boolean gameEnabled = true;

	public static final GoldenCookie gc = new GoldenCookie();
	private final boolean[] keyStates = new boolean[400]; // for keeping track of key down last tick

	private float totalTickProgress = 0;
	private float cookieScale = 4.0f;

	private boolean wasPressed = false;

	private float ticksPassed = 0;

	public static final List<FloatingText> activeTexts = new ArrayList<>();

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
		KeyMapping debug = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.cookie-clicker.debug", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY));

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

			// Handle golden cookies
			if(gc.active) {
				handleGoldenCookie(client);
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
			while (debug.consumeClick()) {
				if(client.player != null) {
					//client.player.displayClientMessage(Component.literal("Opening cookie clicker!"), false);
					if(!gc.active) {
						Cookie.cookieSecondsLeft = 10;
						CookieclickerClient.gc.active = true;
						// random size 4 - 10
						int size = 4 + (int)(Math.random() * ((10 - 4) + 1));
						CookieclickerClient.gc.startNew(size);
					}
					else {
						gc.active = false;
					}
				}
			}
		});
		// Save on exit
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			Cookie.save();
		});
	}

	private void handleGoldenCookie(Minecraft client) {
		Window window = client.getWindow();
		int[] checkKeys = {GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D};

		for (int key : checkKeys) {
			boolean isDown = com.mojang.blaze3d.platform.InputConstants.isKeyDown(window, key);
			if (isDown && !keyStates[key]) {
				if(gc.input(key)) {
					// Golden cookie success
					Cookie.goldenCookieSuccess();
					client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_OUT, 2.0f));
					gc.active = false;
				}
				else {
					client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 2.0f));
				}
			}
			keyStates[key] = isDown;
		}
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

				if(Cookie.frenzy || Cookie.clickFrenzy) {
					graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), 0x44FFFF88);
				}

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

				if (gc.active) {
					int centerX = graphics.guiWidth() / 2;
					int y = graphics.guiHeight() / 2 + 40; // below big cookie

					for(int i = 0; i < gc.sequence.size(); i++) {
						int key = gc.sequence.get(i);
						String letter = switch (key) {
							case GLFW.GLFW_KEY_W -> "↑";
							case GLFW.GLFW_KEY_A -> "←";
							case GLFW.GLFW_KEY_S -> "↓";
							case GLFW.GLFW_KEY_D -> "→";
							default -> "?";
						};
						int color;
						if(i < gc.index) {
							color = 0xFFFFFF00;
						}
						else {
							color = 0xFF888888;
						}
						graphics.drawString(client.font, "Press these keys to activate a golden cookie", centerX - (client.font.width("Press these keys to activate a golden cookie") / 2), y, color, true);

						graphics.drawString(client.font, letter, centerX - (gc.sequence.size() * 10 / 2) + (i * 10), y + 20, color, true);
						letter = switch (key) {
							case GLFW.GLFW_KEY_W -> "W";
							case GLFW.GLFW_KEY_A -> "A";
							case GLFW.GLFW_KEY_S -> "S";
							case GLFW.GLFW_KEY_D -> "D";
							default -> "?";
						};
						graphics.drawString(client.font, letter, centerX - (gc.sequence.size() * 10 / 2) + (i * 10), y + 30, color, true);
						graphics.drawString(client.font, "Time Left:", centerX - (client.font.width("Time Left:") / 2), y + 40, color, true);
						graphics.drawString(client.font, Cookie.cookieSecondsLeft + " seconds", centerX - (client.font.width(Cookie.cookieSecondsLeft + " seconds") / 2), y + 50, color, true);
					}
				}

				// Push state
				matrices.pushMatrix();

				// Move the origin to the center of the screen
				matrices.translate(graphics.guiWidth()/2f, graphics.guiHeight()/2f);

				if(Cookie.frenzy) {
					graphics.drawString(client.font, "GOLDEN COOKIE!", 0 - (client.font.width("GOLDEN COOKIE!") / 2), -80, 0xFFFFFFFF, true);
					graphics.drawString(client.font, "Frenzy! x7 CPS!", 0 - (client.font.width("Frenzy! x7 CPS!") / 2), -60, 0xFFFFFFFF, true);
					graphics.drawString(client.font, "Time Left: " + Cookie.timer + " seconds", 0 - (client.font.width("Time Left: " + Cookie.timer + " seconds") / 2), -40, 0xFFFFFFFF, true);
				}
				else if(Cookie.clickFrenzy) {
					graphics.drawString(client.font, "GOLDEN COOKIE!", 0 - (client.font.width("GOLDEN COOKIE!") / 2), -80, 0xFFFFFFFF, true);
					graphics.drawString(client.font, "Frenzy! x777 Click Power!", 0 - (client.font.width("Frenzy! x777 Click Power!") / 2), -60, 0xFFFFFFFF, true);
					graphics.drawString(client.font, "Time Left: " + Cookie.timer + " seconds", 0 - (client.font.width("Time Left: " + Cookie.timer + " seconds") / 2), -40, 0xFFFFFFFF, true);
				}
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
					if(Cookie.clickFrenzy) {
						graphics.drawString(client.font, String.valueOf(Cookie.cookiesPerClick.multiply(BigDecimal.valueOf(777)).setScale(2, RoundingMode.HALF_UP)), (int)t.x, (int)t.y, color, true);
					}
					else {
						graphics.drawString(client.font, t.text, (int)t.x, (int)t.y, color, true);
					}
				}
				matrices.popMatrix();
			}
		};
	}
}