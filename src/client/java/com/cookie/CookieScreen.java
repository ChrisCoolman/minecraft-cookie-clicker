package com.cookie;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CookieScreen extends Screen {
    public CookieScreen(Component title) {
        super(title);
    }

    int buttonWidth = 200;

    @Override
    protected void init() {
        Button buyCursor = Button.builder(Component.literal(Cookie.cursor.amountPurchased + " Purchase Cursor -  " + Cookie.cursor.calculatePrice()), (btn) -> {
            Cookie.cursor.purchase();
        }).bounds((this.width - buttonWidth) / 2, 40, buttonWidth, 20).build();
        // x, y, width, height
        // always make height 20

        this.addRenderableWidget(buyCursor);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        // textRenderer, text, x, y, color, hasShadow
        graphics.drawString(this.font, "Purchase Buildings", (this.width / 2) - (this.font.width("Purchase Buildings") / 2), 10, 0xFFFFFFFF, true);
    }

}
