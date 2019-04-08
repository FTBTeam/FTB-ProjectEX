package com.latmod.mods.projectex.gui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.latmod.mods.projectex.ProjectEXUtils;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.config.ProjectEConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ContainerArcaneTablet extends ContainerTableBase
{
	private static final int[] ROTATION_SLOTS = {0, 1, 2, 5, 8, 7, 6, 3};

	public final InventoryCrafting craftMatrix;
	public final InventoryCraftResult craftResult;
	public boolean skipRefill = false;

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

			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack)
			{
				if (skipRefill)
				{
					return super.onTake(player, stack);
				}

				ItemStack[] prevItems = new ItemStack[craftMatrix.getSizeInventory()];

				for (int i = 0; i < prevItems.length; i++)
				{
					prevItems[i] = craftMatrix.getStackInSlot(i);

					if (!prevItems[i].isEmpty())
					{
						prevItems[i] = ItemHandlerHelper.copyStackWithSize(prevItems[i], 1);
					}
				}

				ItemStack is = super.onTake(player, stack);

				for (int i = 0; i < prevItems.length; i++)
				{
					if (!prevItems[i].isEmpty() && craftMatrix.getStackInSlot(i).isEmpty())
					{
						transferFromTablet(i, new ItemStack[] {prevItems[i]});
					}
				}

				return is;
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
			addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 193)
			{
				@Override
				public boolean canTakeStack(EntityPlayer player)
				{
					return getSlotIndex() != player.inventory.currentItem;
				}
			});
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
			skipRefill = true;
			ItemStack stack = super.transferStackInSlot(player, index);
			skipRefill = false;
			return stack;
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
		clearCraftingMatrix();

		int max = Math.min(recipe.length, craftMatrix.getSizeInventory());
		transferItems(recipe, max);

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
				if (transferFromInventory(i, recipe[i], false) || transferFromInventory(i, recipe[i], true))
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

	private boolean transferFromInventory(int i, ItemStack[] possibilities, boolean ignoreNBT)
	{
		for (ItemStack possibility : possibilities)
		{
			int meta = possibility.getItem().isDamageable() ? -1 : possibility.getMetadata();

			for (int j = 0; j < player.inventory.getSizeInventory(); ++j)
			{
				ItemStack stack = player.inventory.getStackInSlot(j);

				if (possibility.getItem() == stack.getItem() && (meta == -1 || possibility.getMetadata() == meta) && (meta == -1 || ignoreNBT || Objects.equals(possibility.getItem().getNBTShareTag(possibility), stack.getItem().getNBTShareTag(stack))))
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

	public void clearCraftingMatrix()
	{
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			ItemStack stack = craftMatrix.removeStackFromSlot(i);

			if (!stack.isEmpty())
			{
				if (ProjectEConfig.difficulty.covalenceLoss >= 1D && ProjectEAPI.getEMCProxy().hasValue(stack))
				{
					int k = ProjectEXUtils.addKnowledge(player, playerData, ProjectEXUtils.fixOutput(stack));

					if (k > 0)
					{
						playerData.setEmc(playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());

						if (k > 1 && knowledgeUpdate != null)
						{
							knowledgeUpdate.updateKnowledge();
						}

						continue;
					}
				}

				player.inventory.placeItemBackInInventory(player.world, stack);
			}
		}

		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
	}

	public void rotateCraftingMatrix(boolean cw)
	{
		ItemStack[] stacks = new ItemStack[ROTATION_SLOTS.length];

		if (cw)
		{
			for (int i = 0; i < ROTATION_SLOTS.length; i++)
			{
				int j = i - 1;

				if (j < 0)
				{
					j = ROTATION_SLOTS.length - 1;
				}

				stacks[i] = craftMatrix.getStackInSlot(ROTATION_SLOTS[j % ROTATION_SLOTS.length]);
			}
		}
		else
		{
			for (int i = 0; i < ROTATION_SLOTS.length; i++)
			{
				stacks[i] = craftMatrix.getStackInSlot(ROTATION_SLOTS[(i + 1) % ROTATION_SLOTS.length]);
			}
		}

		for (int i = 0; i < ROTATION_SLOTS.length; i++)
		{
			craftMatrix.setInventorySlotContents(ROTATION_SLOTS[i], stacks[i]);
		}

		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
		detectAndSendChanges();
	}

	public void balanceCraftingMatrix()
	{
		ArrayListMultimap<NBTTagCompound, ItemStack> map = ArrayListMultimap.create();
		Multiset<NBTTagCompound> itemCount = HashMultiset.create();

		for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			ItemStack stack = craftMatrix.getStackInSlot(i);

			if (!stack.isEmpty() && stack.getMaxStackSize() > 1)
			{
				NBTTagCompound key = stack.serializeNBT();
				key.removeTag("Count");
				map.put(key, stack);
				itemCount.add(key, stack.getCount());
			}
		}

		for (NBTTagCompound key : map.keySet())
		{
			List<ItemStack> list = map.get(key);
			int totalCount = itemCount.count(key);
			int countPerStack = totalCount / list.size();
			int restCount = totalCount % list.size();

			for (ItemStack stack : list)
			{
				stack.setCount(countPerStack);
			}

			int idx = 0;

			while (restCount > 0)
			{
				ItemStack stack = list.get(idx);

				if (stack.getCount() < stack.getMaxStackSize())
				{
					stack.grow(1);
					restCount--;
				}

				idx++;

				if (idx >= list.size())
				{
					idx = 0;
				}
			}
		}

		slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
		detectAndSendChanges();
	}

	public void spreadCraftingMatrix()
	{
		while (true)
		{
			ItemStack biggestStack = null;
			int biggestSize = 1;

			for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
			{
				ItemStack stack = craftMatrix.getStackInSlot(i);

				if (!stack.isEmpty() && stack.getCount() > biggestSize)
				{
					biggestStack = stack;
					biggestSize = stack.getCount();
				}
			}

			if (biggestStack == null)
			{
				return;
			}

			boolean emptyBiggestSlot = false;

			for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
			{
				ItemStack stack = craftMatrix.getStackInSlot(i);

				if (stack.isEmpty())
				{
					if (biggestStack.getCount() > 1)
					{
						craftMatrix.setInventorySlotContents(i, biggestStack.splitStack(1));
					}
					else
					{
						emptyBiggestSlot = true;
					}
				}
			}

			if (!emptyBiggestSlot)
			{
				break;
			}
		}

		balanceCraftingMatrix();
	}

	@Override
	public boolean enchantItem(EntityPlayer player, int id)
	{
		switch (id)
		{
			case 40:
				clearCraftingMatrix();
				return true;
			case 41:
				rotateCraftingMatrix(true);
				return true;
			case 42:
				rotateCraftingMatrix(false);
				return true;
			case 43:
				balanceCraftingMatrix();
				return true;
			case 44:
				spreadCraftingMatrix();
				return true;
			default:
				return super.enchantItem(player, id);
		}
	}
}