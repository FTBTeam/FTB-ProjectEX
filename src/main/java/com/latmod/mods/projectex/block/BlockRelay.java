package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.gui.EMCFormat;
import com.latmod.mods.projectex.tile.TileRelay;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockRelay extends BlockTier
{
	public BlockRelay()
	{
		super(Material.ROCK, MapColor.BLACK);
		setHardness(10F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileRelay();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		ProjectEXConfig.BlockTier properties = EnumTier.byMeta(stack.getMetadata()).properties;
		tooltip.add(I18n.format("tile.projectex.relay.tooltip"));

		if (properties.relay_transfer != Double.MAX_VALUE)
		{
			tooltip.add(I18n.format("tile.projectex.relay.max_transfer", TextFormatting.GREEN + EMCFormat.INSTANCE.format(properties.relay_transfer)));
		}

		tooltip.add(I18n.format("tile.projectex.relay.relay_bonus", TextFormatting.GREEN + EMCFormat.INSTANCE.format(properties.relay_bonus)));
	}
}