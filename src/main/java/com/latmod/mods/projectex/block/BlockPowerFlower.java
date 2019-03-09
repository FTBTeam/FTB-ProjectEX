package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.tile.TilePowerFlower;
import moze_intel.projecte.utils.Constants;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockPowerFlower extends BlockTier
{
	public BlockPowerFlower()
	{
		super(Material.GLASS, MapColor.YELLOW);
		setHardness(0.3F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TilePowerFlower();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
		{
			if (!world.isRemote)
			{
				TileEntity tileEntity = world.getTileEntity(pos);

				if (tileEntity instanceof TilePowerFlower)
				{
					player.sendStatusMessage(new TextComponentString(((TilePowerFlower) tileEntity).name), true);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TilePowerFlower)
		{
			((TilePowerFlower) tileEntity).owner = placer.getUniqueID();
			((TilePowerFlower) tileEntity).name = placer.getName();
			tileEntity.markDirty();
		}
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		if (side != EnumFacing.DOWN)
		{
			IBlockState state1 = world.getBlockState(pos.offset(side));

			if (state1.getBlock() == this)
			{
				return false;
			}
		}

		return super.shouldSideBeRendered(state, world, pos, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		ProjectEXConfig.BlockTier properties = EnumTier.byMeta(stack.getMetadata()).properties;
		tooltip.add(I18n.format("tile.projectex.collector.tooltip"));
		tooltip.add(I18n.format("tile.projectex.collector.emc_produced", TextFormatting.GREEN + Constants.EMC_FORMATTER.format(properties.powerFlowerOutput())));
	}
}