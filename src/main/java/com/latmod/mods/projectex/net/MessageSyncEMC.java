package com.latmod.mods.projectex.net;

import io.netty.buffer.ByteBuf;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.client.Minecraft;
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
			Minecraft.getMinecraft().addScheduledTask(() -> {
				IKnowledgeProvider provider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(Minecraft.getMinecraft().player.getUniqueID());
				provider.setEmc(message.emc);
			});

			return null;
		}
	}
}