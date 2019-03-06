package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.gui.ContainerTableBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
public class MessageSendSearch implements IMessage
{
	public String search;
	public int page;

	public MessageSendSearch()
	{
	}

	public MessageSendSearch(ContainerTableBase c)
	{
		search = c.search;
		page = c.page;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		search = ByteBufUtils.readUTF8String(buf);
		page = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, search);
		buf.writeInt(page);
	}

	public static class Handler implements IMessageHandler<MessageSendSearch, IMessage>
	{
		@Override
		public IMessage onMessage(MessageSendSearch message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;

			player.server.addScheduledTask(() -> {
				Container container = player.openContainer;
				if (container instanceof ContainerTableBase)
				{
					((ContainerTableBase) container).search = message.search;
					((ContainerTableBase) container).page = message.page;
					((ContainerTableBase) container).updateCurrentItemList();
				}
			});

			return null;
		}
	}
}