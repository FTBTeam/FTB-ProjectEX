package com.latmod.mods.projectex.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

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

		addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, -23, 75));

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
			return ItemStack.EMPTY;
		}

		return super.transferStackInSlot(player, index);
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);

		if (!player.world.isRemote)
		{
			clearContainer(player, player.world, craftMatrix);
		}
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return slot.inventory != craftResult && super.canMergeSlot(stack, slot);
	}

}