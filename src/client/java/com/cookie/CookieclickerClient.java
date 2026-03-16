package com.cookie;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import org.joml.Matrix3x2fStack;

public class CookieclickerClient implements ClientModInitializer {

	private float totalTickProgress = 0;
	private float cookieScale = 2.0f;

	public float cookies = 0.0f;

	private boolean wasPressed = false;


	@Override
	public void onInitializeClient() {
		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath("cookieclicker", "last_element"),
				hudLayer()
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean isPressed = client.mouseHandler.isLeftPressed();

			if (isPressed && !wasPressed) {
				cookieScale = 4.0f;
				cookies += 1.0f;
			}

			wasPressed = isPressed;

			if (cookieScale > 2.0f) {
				cookieScale -= 0.2f;
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
			graphics.drawString(client.font, "Cookies: " + cookies, 5, 10, 0xFFFFFFFF, true);

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