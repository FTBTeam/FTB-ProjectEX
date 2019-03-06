package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.tile.TileLink;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
public class MessageSendLinkStack implements IMessage
{
	public BlockPos pos;
	public int slot;
	public ItemStack stack;

	public MessageSendLinkStack()
	{
	}

	public MessageSendLinkStack(BlockPos p, int s, ItemStack is)
	{
		pos = p;
		slot = s;
		stack = is;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
		slot = buf.readUnsignedByte();
		stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeByte(slot);
		ByteBufUtils.writeItemStack(buf, stack);
	}

	public static class Handler implements IMessageHandler<MessageSendLinkStack, IMessage>
	{
		@Override
		public IMessage onMessage(MessageSendLinkStack message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;

			player.server.addScheduledTask(() -> {
				TileEntity tileEntity = player.world.getTileEntity(message.pos);

				if (tileEntity instanceof TileLink)
				{
					((TileLink) tileEntity).setOutputStack(player, message.stack);
				}
			});

			return null;
		}
	}
}