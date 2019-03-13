package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.gui.ContainerTableBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
public class MessageCreateItemButton implements IMessage
{
	public ItemStack stack;
	public int mode;

	public MessageCreateItemButton()
	{
	}

	public MessageCreateItemButton(ItemStack is, int m)
	{
		stack = is;
		mode = m;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		stack = ByteBufUtils.readItemStack(buf);
		mode = buf.readUnsignedByte();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeByte(mode);
	}

	public static class Handler implements IMessageHandler<MessageCreateItemButton, IMessage>
	{
		@Override
		public IMessage onMessage(MessageCreateItemButton message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayerMP player = ctx.getServerHandler().player;

				if (player.openContainer instanceof ContainerTableBase && ((ContainerTableBase) player.openContainer).clickGuiSlot(message.stack, message.mode) > 0)
				{
					player.openContainer.detectAndSendChanges();
				}
			});

			return null;
		}
	}
}