package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumTier implements IStringSerializable
{
	MK1("mk1", 0, ProjectEXConfig.general.mk1),
	MK2("mk2", 1, ProjectEXConfig.general.mk2),
	MK3("mk3", 2, ProjectEXConfig.general.mk3),
	MK4("mk4", 3, ProjectEXConfig.general.mk4),
	MK5("mk5", 4, ProjectEXConfig.general.mk5),
	MK6("mk6", 5, ProjectEXConfig.general.mk6),
	MK7("mk7", 6, ProjectEXConfig.general.mk7),
	MK8("mk8", 7, ProjectEXConfig.general.mk8),
	MK9("mk9", 8, ProjectEXConfig.general.mk9),
	MK10("mk10", 9, ProjectEXConfig.general.mk10),
	MK_FINAL("mk_final", 15, ProjectEXConfig.general.mk_final);

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
	public ProjectEXConfig.MKBlock properties;

	EnumTier(String n, int m, ProjectEXConfig.MKBlock b)
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