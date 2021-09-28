package dev.latvian.mods.projectex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GlowingItem extends Item {
	public GlowingItem(Properties p) {
		super(p);
	}

	@Override
	public boolean isFoil(ItemStack stac) {
		return true;
	}
}
