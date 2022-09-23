package dev.latvian.mods.projectex.client.gui;

import dev.latvian.mods.projectex.block.entity.RefinedLinkBlockEntity;
import dev.latvian.mods.projectex.menu.RefinedLinkMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RefinedLinkScreen extends AbstractLinkScreen<RefinedLinkMenu, RefinedLinkBlockEntity> {
    public RefinedLinkScreen(RefinedLinkMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return TEXTURE_MK2;
    }
}
