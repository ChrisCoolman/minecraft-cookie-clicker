package com.cookie;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CookieScreen extends Screen {
    public CookieScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {

        int buttonWidth = 200;
        int x = (this.width - buttonWidth) / 2;
        int startY = 40;
        int spacing = 24;

        for (int i = 0; i < Cookie.BUILDINGS.size(); i++) {
            Building b = Cookie.BUILDINGS.get(i);
            if(b.requiredCookies.compareTo(Cookie.maxCookies) <= 0) {
                int y = startY + (i * spacing);

                Button button = Button.builder(Component.literal(b.amountPurchased + " Purchase " + b.name + " -  " + b.calculatePrice()), (btn) -> {
                    b.purchase();
                    this.rebuildWidgets(); // refresh when bought
                }).bounds(x, y, buttonWidth, 20).tooltip(Tooltip.create(Component.literal(b.generateTooltip()))).build();
                // x, y, width, height
                // always make height 20

                if (Cookie.cookies.compareTo(b.calculatePrice()) == -1) {
                    button.active = false;
                }
                else {
                    button.active = true;
                }
                this.addRenderableWidget(button);
            }


        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        // textRenderer, text, x, y, color, hasShadow
        graphics.drawString(this.font, "Purchase Buildings", (this.width / 2) - (this.font.width("Purchase Buildings") / 2), 10, 0xFFFFFFFF, true);
    }

}
