package com.latmod.mods.projectex.net;

import io.netty.buffer.ByteBuf;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
public class MessageSyncEMC implements IMessage
{
	public double emc;

	public MessageSyncEMC()
	{
	}

	public MessageSyncEMC(double e)
	{
		emc = e;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		emc = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(emc);
	}

	public static class Handler implements IMessageHandler<MessageSyncEMC, IMessage>
	{
		@Override
		public IMessage onMessage(MessageSyncEMC message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> setEMC(message.emc));
			return null;
		}

		private void setEMC(double emc)
		{
			ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(Minecraft.getMinecraft().player.getUniqueID()).setEmc(emc);
		}
	}
}