package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.Star;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ColossalStarItem extends MagnumStarItem {
    public ColossalStarItem(Star t) {
        super(t);
    }

    @Override
    public long getMaximumEmc(@Nonnull ItemStack stack) {
        return STAR_EMC[tier.ordinal() + 6];
    }
}
