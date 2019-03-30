package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEXUtils;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author LatvianModder
 */
public class ContainerArcaneTablet extends ContainerTableBase
{
	public final InventoryCrafting craftMatrix;
	public final InventoryCraftResult craftResult;

	public ContainerArcaneTablet(EntityPlayer p)
	{
		super(p);

		craftMatrix = new InventoryCraftingArcaneTablet(this, 3, 3, (IItemHandlerModifiable) playerData.getInputAndLocks());
		craftResult = new InventoryCraftResult();

		addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, -23, 75)
		{
			@Override
			protected void onCrafting(ItemStack stack)
			{
				super.onCrafting(stack);

				if (ProjectEXUtils.addKnowledge(player, playerData, ProjectEXUtils.fixOutput(stack)) == 2 && knowledgeUpdate != null)
				{
					knowledgeUpdate.updateKnowledge();
				}
			}
		});

		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 3; x++)
			{
				addSlotToContainer(new Slot(craftMatrix, x + y * 3, -59 + x * 18, 17 + y * 18));
			}
		}

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 135 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 193));
		}

		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		if (index == 0)
		{
			Slot slot = inventorySlots.get(index);
			ItemStack stack = slot.getStack();
			ItemStack old = stack.copy();
			stack.getItem().onCreated(stack, player.world, player);

			if (!mergeItemStack(stack, 10, 46, true))
			{
				return ItemStack.EMPTY;
			}

			slot.onSlotChange(stack, old);

			if (stack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (stack.getCount() == old.getCount())
			{
				return ItemStack.EMPTY;
			}

			player.dropItem(slot.onTake(player, stack), false);
			return old;
		}
		else if (index >= 1 && index <= 9)
		{
			Slot slot = inventorySlots.get(index);
			ItemStack stack = slot.getStack();
			ItemStack old = stack.copy();

			if (!mergeItemStack(stack, 10, 46, true))
			{
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
			return old;
		}

		return super.transferStackInSlot(player, index);
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return slot.inventory != craftResult && super.canMergeSlot(stack, slot);
	}

	public void onRecipeTransfer(ItemStack[][] recipe, boolean transferAll)
	{
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			player.inventory.placeItemBackInInventory(player.world, craftMatrix.removeStackFromSlot(i));
		}

		int max = Math.min(recipe.length, craftMatrix.getSizeInventory());
		transferItems(recipe, max);

		/*
		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);

		if (transferAll && !craftResult.getStackInSlot(0).isEmpty())
		{
			int times = craftResult.getStackInSlot(0).getMaxStackSize() - 1;

			for (int i = 0; i < times; i++)
			{
				transferItems(recipe, max);
			}
		}
		*/

		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
		detectAndSendChanges();
	}

	private void transferItems(ItemStack[][] recipe, int max)
	{
		for (int i = 0; i < max; i++)
		{
			if (recipe[i] != null && recipe[i].length > 0)
			{
				if (transferFromTablet(i, recipe[i]))
				{
					recipe[i] = null;
				}
			}
		}

		for (int i = 0; i < max; i++)
		{
			if (recipe[i] != null && recipe[i].length > 0)
			{
				if (transferFromInventory(i, recipe[i]))
				{
					recipe[i] = null;
				}
			}
		}
	}

	private boolean transferFromTablet(int i, ItemStack[] possibilities)
	{
		if (possibilities.length > 1)
		{
			Arrays.sort(possibilities, Comparator.comparingLong(o -> ProjectEAPI.getEMCProxy().getValue(o)));
		}

		for (ItemStack stack : possibilities)
		{
			ItemStack stack1 = ProjectEXUtils.fixOutput(stack);

			if (playerData.hasKnowledge(stack1))
			{
				long value = ProjectEAPI.getEMCProxy().getValue(stack1);

				if (value > 0L && playerData.getEmc() >= value)
				{
					playerData.setEmc(playerData.getEmc() - value);
					ItemStack slotItem = craftMatrix.getStackInSlot(i);

					if (slotItem.isEmpty())
					{
						craftMatrix.setInventorySlotContents(i, stack1);
					}
					else if (slotItem.getCount() < slotItem.getMaxStackSize())
					{
						slotItem.grow(1);
					}

					return true;
				}
			}
		}

		return false;
	}

	private boolean transferFromInventory(int i, ItemStack[] possibilities)
	{
		for (ItemStack possibility : possibilities)
		{
			int meta = possibility.getItem().isDamageable() ? -1 : possibility.getMetadata();

			for (int j = 0; j < player.inventory.getSizeInventory(); ++j)
			{
				ItemStack stack = player.inventory.getStackInSlot(j);

				if (possibility.getItem() == stack.getItem() && (meta == -1 || possibility.getMetadata() == meta))
				{
					ItemStack slotItem = craftMatrix.getStackInSlot(i);

					if (slotItem.isEmpty())
					{
						craftMatrix.setInventorySlotContents(i, ItemHandlerHelper.copyStackWithSize(stack, 1));
					}
					else if (slotItem.getCount() < slotItem.getMaxStackSize())
					{
						slotItem.grow(1);
					}

					stack.shrink(1);

					if (stack.isEmpty())
					{
						player.inventory.setInventorySlotContents(j, ItemStack.EMPTY);
					}

					return true;
				}
			}
		}

		return false;
	}
}