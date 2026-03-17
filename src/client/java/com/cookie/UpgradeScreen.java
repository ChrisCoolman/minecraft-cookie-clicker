package com.cookie;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UpgradeScreen extends Screen {
    public UpgradeScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {

        int buttonWidth = 400;
        int x = (this.width - buttonWidth) / 2;
        int startY = 40;
        int spacing = 24;

        for (int i = 0; i < Cookie.UPGRADES.size(); i++) {
            Upgrade u = Cookie.UPGRADES.get(i);
            int y = startY + (i * spacing);

            Button button = Button.builder(Component.literal(" Purchase " + u.name + " -  " + u.price), (btn) -> {
                u.purchase();
                Cookie.UPGRADES.remove(u);
                this.rebuildWidgets(); // refresh when bought
            }).bounds(x, y, buttonWidth, 20).build();

            if (Cookie.cookies.compareTo(u.price) == -1) {
                button.active = false;
            }
            else {
                button.active = true;
            }


            this.addRenderableWidget(button);
            // x, y, width, height
            // always make height 20
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        // textRenderer, text, x, y, color, hasShadow
        graphics.drawString(this.font, "Purchase Upgrades", (this.width / 2) - (this.font.width("Purchase Upgrades") / 2), 10, 0xFFFFFFFF, true);
    }

}
