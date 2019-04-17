package com.latmod.mods.projectex.item;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.tile.TileRelay;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.api.item.IPedestalItem;
import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemFinalStar extends Item implements IItemEmc, IPedestalItem
{
	public ItemFinalStar()
	{
		setMaxStackSize(1);
	}

	@Override
	public double addEmc(ItemStack stack, double toAdd)
	{
		return 0D;
	}

	@Override
	public double extractEmc(ItemStack stack, double toRemove)
	{
		return toRemove;
	}

	@Override
	public double getStoredEmc(ItemStack stack)
	{
		return 1_000_000_000_000_000D;
	}

	@Override
	public double getMaximumEmc(ItemStack stack)
	{
		return Double.MAX_VALUE;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}

	@Override
	public void updateInPedestal(World world, BlockPos pos)
	{
		if (ProjectEXConfig.general.final_star_update_interval <= 0)
		{
			return;
		}

		if (!world.isRemote && world.getTotalWorldTime() % (long) ProjectEXConfig.general.final_star_update_interval == TileRelay.mod(pos.hashCode(), ProjectEXConfig.general.final_star_update_interval))
		{
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, Block.FULL_BLOCK_AABB.offset(pos).expand(0D, 1D, 0D));

			if (!items.isEmpty())
			{
				for (EnumFacing facing : EnumFacing.VALUES)
				{
					if (facing != EnumFacing.UP)
					{
						TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
						IItemHandler handler = tileEntity == null ? null : tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());

						if (handler != null)
						{
							ItemStack stack = items.get(world.rand.nextInt(items.size())).getItem().copy();

							if (ProjectEXConfig.general.final_star_copy_any_item || ProjectEAPI.getEMCProxy().hasValue(stack))
							{
								stack.setCount(stack.getMaxStackSize());

								if (!stack.getHasSubtypes() && stack.isItemStackDamageable())
								{
									stack.setItemDamage(0);
								}

								if (!ProjectEXConfig.general.final_star_copy_nbt && stack.hasTagCompound() && !NBTWhitelist.shouldDupeWithNBT(stack))
								{
									stack.setTagCompound(new NBTTagCompound());
								}

								ItemHandlerHelper.insertItem(handler, stack, false);
								return;
							}
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<String> getPedestalDescription()
	{
		return Collections.singletonList(I18n.format("item.projectex.final_star.tooltip"));
	}
}