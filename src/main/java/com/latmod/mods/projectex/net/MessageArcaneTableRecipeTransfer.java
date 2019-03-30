package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.gui.ContainerArcaneTablet;
import io.netty.buffer.ByteBuf;
import mezz.jei.api.gui.IGuiIngredient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class MessageArcaneTableRecipeTransfer implements IMessage
{
	private Map<Integer, ? extends IGuiIngredient<ItemStack>> inputs;
	private List<Slot> slots;
	private ItemStack[][] recipe = new ItemStack[9][];
	private boolean transferAll;

	public MessageArcaneTableRecipeTransfer()
	{
	}

	public MessageArcaneTableRecipeTransfer(Map<Integer, ? extends IGuiIngredient<ItemStack>> inputs, List<Slot> slots, boolean ta)
	{
		this.inputs = inputs;
		this.slots = slots;
		transferAll = ta;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int slots = buf.readInt();

		for (int i = 0; i < slots; ++i)
		{
			int ingredients = buf.readInt();

			recipe[i] = new ItemStack[ingredients];

			for (int j = 0; j < ingredients; ++j)
			{
				recipe[i][j] = ByteBufUtils.readItemStack(buf);
			}
		}

		transferAll = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(slots.size());

		for (Slot slot : slots)
		{
			IGuiIngredient<ItemStack> ingredient = inputs.get(slot.getSlotIndex() + 1);

			List<ItemStack> ingredients = new ArrayList<>();

			if (ingredient != null)
			{
				for (ItemStack possibleStack : ingredient.getAllIngredients())
				{
					if (possibleStack != null)
					{
						ingredients.add(possibleStack);
					}
				}
			}

			buf.writeInt(ingredients.size());

			for (ItemStack possibleStack : ingredients)
			{
				ByteBufUtils.writeItemStack(buf, possibleStack);
			}
		}

		buf.writeBoolean(transferAll);
	}

	public static class Handler implements IMessageHandler<MessageArcaneTableRecipeTransfer, IMessage>
	{
		@Override
		public IMessage onMessage(MessageArcaneTableRecipeTransfer message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;

			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				if (player.openContainer instanceof ContainerArcaneTablet)
				{
					((ContainerArcaneTablet) player.openContainer).onRecipeTransfer(message.recipe, message.transferAll);
				}
			});

			return null;
		}
	}
}
