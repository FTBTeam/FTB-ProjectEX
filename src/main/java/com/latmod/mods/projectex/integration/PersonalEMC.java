package com.latmod.mods.projectex.integration;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.net.MessageSyncEMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.impl.KnowledgeImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
public class PersonalEMC
{
	@CapabilityInject(IKnowledgeProvider.class)
	public static Capability<IKnowledgeProvider> CAP;

	private static final Map<UUID, IKnowledgeProvider> MAP = new HashMap<>();

	public static IKnowledgeProvider get(World world, UUID id)
	{
		if (world.isRemote)
		{
			return ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(id);
		}

		IKnowledgeProvider provider = MAP.get(id);

		if (provider == null)
		{
			provider = new OfflineKnowledgeProvider(id);
			MAP.put(id, provider);

			File playerDataFolder = new File(world.getSaveHandler().getWorldDirectory(), "playerdata");

			if (playerDataFolder.exists())
			{
				File playerFile = new File(playerDataFolder, id + ".dat");

				if (playerFile.exists() && playerFile.isFile())
				{
					try (FileInputStream in = new FileInputStream(playerFile))
					{
						NBTTagCompound nbt = CompressedStreamTools.readCompressed(in).getCompoundTag("ForgeCaps").getCompoundTag(KnowledgeImpl.Provider.NAME.toString());

						if (!nbt.isEmpty())
						{
							provider.deserializeNBT(nbt);
						}
					}
					catch (Throwable ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}

		return provider;
	}

	public static IKnowledgeProvider get(EntityPlayer player)
	{
		return Objects.requireNonNull(player.getCapability(CAP, null));
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			IKnowledgeProvider knowledgeProvider = MAP.get(event.player.getUniqueID());

			if (knowledgeProvider != null)
			{
				IKnowledgeProvider provider = event.player.getCapability(CAP, null);

				if (provider != null)
				{
					provider.deserializeNBT(knowledgeProvider.serializeNBT());
					MAP.put(event.player.getUniqueID(), provider);
					provider.sync((EntityPlayerMP) event.player);
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			IKnowledgeProvider provider = event.player.getCapability(CAP, null);

			if (provider != null)
			{
				OfflineKnowledgeProvider knowledgeProvider = new OfflineKnowledgeProvider(event.player.getUniqueID());
				knowledgeProvider.deserializeNBT(provider.serializeNBT());
				MAP.put(knowledgeProvider.playerId, knowledgeProvider);
			}
		}
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			double prev = event.player.getEntityData().getDouble("PEX_EMC");
			double emc = get(event.player).getEmc();

			if (prev != emc)
			{
				event.player.getEntityData().setDouble("PEX_EMC", emc);
				MessageSyncEMC.sync(event.player, emc);
			}
		}
	}

	@SubscribeEvent
	public static void worldSaved(WorldEvent.Save event)
	{
		if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
		{
			for (IKnowledgeProvider provider : MAP.values())
			{
				if (provider instanceof OfflineKnowledgeProvider && ((OfflineKnowledgeProvider) provider).shouldSave)
				{
					((OfflineKnowledgeProvider) provider).shouldSave = false;
					final NBTTagCompound nbt = provider.serializeNBT();
					final UUID id = ((OfflineKnowledgeProvider) provider).playerId;

					ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
						File playerDataFolder = new File(event.getWorld().getSaveHandler().getWorldDirectory(), "playerdata");

						if (playerDataFolder.exists())
						{
							File playerFile = new File(playerDataFolder, id + ".dat");

							if (playerFile.exists() && playerFile.isFile())
							{
								FileInputStream in = null;
								NBTTagCompound playerData = null;

								try
								{
									in = new FileInputStream(playerFile);
									playerData = CompressedStreamTools.readCompressed(in);
									NBTTagCompound forgeCapsTag = playerData.getCompoundTag("ForgeCaps");
									forgeCapsTag.setTag(KnowledgeImpl.Provider.NAME.toString(), nbt);
									playerData.setTag("ForgeCaps", forgeCapsTag);
								}
								catch (Throwable ex)
								{
									ex.printStackTrace();
								}

								IOUtils.closeQuietly(in);

								if (playerData != null)
								{
									FileOutputStream out = null;

									try
									{
										out = new FileOutputStream(playerFile);
										CompressedStreamTools.writeCompressed(playerData, out);
									}
									catch (Throwable ex)
									{
										ex.printStackTrace();
									}

									IOUtils.closeQuietly(out);
								}
							}
						}

						return false;
					});
				}
			}
			//save player files
		}
	}

	@SubscribeEvent
	public static void worldUnloaded(WorldEvent.Unload event)
	{
		if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
		{
			MAP.clear();
		}
	}
}