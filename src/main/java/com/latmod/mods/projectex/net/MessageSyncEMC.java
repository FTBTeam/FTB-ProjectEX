package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.integration.PersonalEMC;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class MessageSyncEMC implements IMessage
{
	public static void sync(@Nullable EntityPlayer player, double emc)
	{
		if (player instanceof EntityPlayerMP)
		{
			MessageSyncEMC message = new MessageSyncEMC();
			message.emc = emc;
			ProjectEXNetHandler.NET.sendTo(message, (EntityPlayerMP) player);
		}
	}

	public double emc;

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
			PersonalEMC.get(Minecraft.getMinecraft().player).setEmc(emc);
		}
	}
}