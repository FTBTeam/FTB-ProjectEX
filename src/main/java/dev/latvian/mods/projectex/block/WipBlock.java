package dev.latvian.mods.projectex.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class WipBlock extends Block {
	public WipBlock() {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE));
	}
}
