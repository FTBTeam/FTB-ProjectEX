package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumTier implements IStringSerializable
{
	MK1("mk1", 0, ProjectEXConfig.tiers.mk1),
	MK2("mk2", 1, ProjectEXConfig.tiers.mk2),
	MK3("mk3", 2, ProjectEXConfig.tiers.mk3),
	MK4("mk4", 3, ProjectEXConfig.tiers.mk4),
	MK5("mk5", 4, ProjectEXConfig.tiers.mk5),
	MK6("mk6", 5, ProjectEXConfig.tiers.mk6),
	MK7("mk7", 6, ProjectEXConfig.tiers.mk7),
	MK8("mk8", 7, ProjectEXConfig.tiers.mk8),
	MK9("mk9", 8, ProjectEXConfig.tiers.mk9),
	MK10("mk10", 9, ProjectEXConfig.tiers.mk10),
	MK_FINAL("mk_final", 15, ProjectEXConfig.tiers.mk_final);

	public static final EnumTier[] VALUES = values();

	public static EnumTier byMeta(int meta)
	{
		for (EnumTier tier : VALUES)
		{
			if (tier.metadata == meta)
			{
				return tier;
			}
		}

		return MK1;
	}

	private final String name;
	public final int metadata;
	public ProjectEXConfig.BlockTier properties;

	EnumTier(String n, int m, ProjectEXConfig.BlockTier b)
	{
		name = n;
		metadata = m;
		properties = b;
	}

	@Override
	public String getName()
	{
		return name;
	}
}