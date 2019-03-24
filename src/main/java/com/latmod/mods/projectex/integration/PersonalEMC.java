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
	private static final Map<UUID, OfflineKnowledgeProvider> OFFLINE_MAP = new HashMap<>();
	private static final Map<UUID, Double> EMC_MAP = new HashMap<>();

	public static IKnowledgeProvider get(World world, UUID id)
	{
		if (world.isRemote)
		{
			return get(ProjectEX.PROXY.getClientPlayer());
		}

		EntityPlayer player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(id);

		if (player != null)
		{
			return get(player);
		}

		OfflineKnowledgeProvider provider = OFFLINE_MAP.get(id);

		if (provider == null)
		{
			provider = new OfflineKnowledgeProvider(id);
			OFFLINE_MAP.put(provider.playerId, provider);

			File playerDataFolder = new File(world.getSaveHandler().getWorldDirectory(), "playerdata");

			if (playerDataFolder.exists())
			{
				File playerFile = new File(playerDataFolder, provider.playerId + ".dat");

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
		return Objects.requireNonNull(player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null));
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			OfflineKnowledgeProvider knowledgeProvider = OFFLINE_MAP.get(event.player.getUniqueID());

			if (knowledgeProvider != null)
			{
				IKnowledgeProvider provider = get(event.player);
				OfflineKnowledgeProvider.copy(knowledgeProvider, provider);
				OFFLINE_MAP.remove(knowledgeProvider.playerId);
				provider.sync((EntityPlayerMP) event.player);
			}
		}
	}

	@SubscribeEvent
	public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			IKnowledgeProvider provider = get(event.player);
			OfflineKnowledgeProvider knowledgeProvider = new OfflineKnowledgeProvider(event.player.getUniqueID());
			OfflineKnowledgeProvider.copy(provider, knowledgeProvider);
			OFFLINE_MAP.put(knowledgeProvider.playerId, knowledgeProvider);
			EMC_MAP.remove(knowledgeProvider.playerId);
		}
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			Double prev = EMC_MAP.get(event.player.getUniqueID());
			double emc = get(event.player).getEmc();

			if (prev == null || prev != emc)
			{
				EMC_MAP.put(event.player.getUniqueID(), emc);
				MessageSyncEMC.sync(event.player, emc);
			}
		}
	}

	@SubscribeEvent
	public static void worldSaved(WorldEvent.Save event)
	{
		if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
		{
			for (OfflineKnowledgeProvider provider : OFFLINE_MAP.values())
			{
				if (provider.shouldSave)
				{
					provider.shouldSave = false;
					final NBTTagCompound nbt = provider.serializeNBT();
					final UUID id = provider.playerId;

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
		}
	}

	@SubscribeEvent
	public static void worldUnloaded(WorldEvent.Unload event)
	{
		if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
		{
			OFFLINE_MAP.clear();
			EMC_MAP.clear();
		}
	}
}