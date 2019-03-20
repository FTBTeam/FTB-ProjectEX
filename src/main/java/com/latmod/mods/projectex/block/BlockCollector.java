package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.gui.EMCFormat;
import com.latmod.mods.projectex.tile.TileCollector;
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
public class BlockCollector extends BlockTier
{
	public BlockCollector()
	{
		super(Material.GLASS, MapColor.YELLOW);
		setHardness(0.3F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileCollector();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		ProjectEXConfig.BlockTier properties = EnumTier.byMeta(stack.getMetadata()).properties;
		tooltip.add(I18n.format("tile.projectex.collector.tooltip"));
		tooltip.add(I18n.format("tile.projectex.collector.emc_produced", TextFormatting.GREEN + EMCFormat.INSTANCE.format(properties.collector_output)));
	}
}