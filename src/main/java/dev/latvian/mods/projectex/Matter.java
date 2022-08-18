package dev.latvian.mods.projectex;

import dev.latvian.mods.projectex.item.ProjectEXItems;
import moze_intel.projecte.gameObjs.registries.PEItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
public enum Matter {
	BASIC("basic", "Basic", false, 4L, 1L, 64L, () -> Items.DIAMOND_BLOCK),
	DARK("dark", "Dark", false, 12L, 3L, 192L, () -> PEItems.DARK_MATTER.get()),
	RED("red", "Red", false, 40L, 10L, 640L, () -> PEItems.RED_MATTER.get()),
	MAGENTA("magenta", "Magenta", true, 160L, 40L, 2560L, null),
	PINK("pink", "Pink", true, 640L, 150L, 10240L, null),
	PURPLE("purple", "Purple", true, 2560L, 750L, 40960L, null),
	VIOLET("violet", "Violet", true, 10240L, 3750L, 163840L, null),
	BLUE("blue", "Blue", true, 40960L, 15000L, 655360L, null),
	CYAN("cyan", "Cyan", true, 163840L, 60000L, 2621440L, null),
	GREEN("green", "Green", true, 655360L, 240000L, 10485760L, null),
	LIME("lime", "Lime", true, 2621440L, 960000L, 41943040L, null),
	YELLOW("yellow", "Yellow", true, 10485760L, 3840000L, 167772160L, null),
	ORANGE("orange", "Orange", true, 41943040L, 15360000L, 671088640L, null),
	WHITE("white", "White", true, 167772160L, 61440000L, 2684354560L, null),
	FADING("fading", "Fading", true, 671088640L, 245760000L, 10737418240L, null),
	FINAL("final", "The Final", false, 1000000000000L, 1000000000000L, Long.MAX_VALUE, () -> ProjectEXItems.FINAL_STAR_SHARD.get());

	public static final Matter[] VALUES = values();

	public final String name;
	public final String displayName;
	public final boolean hasMatterItem;
	public final long collectorOutput;
	public final long relayBonus;
	public final long relayTransfer;
	public final Supplier<Item> item;

	Matter(String n, String d, boolean i, long co, long rb, long rt, @Nullable Supplier<Item> it) {
		name = n;
		displayName = d;
		hasMatterItem = i;
		collectorOutput = co;
		relayBonus = rb;
		relayTransfer = rt;
		item = it;
	}

	public Supplier<Item> getItem() {
		// Need to be kept as lambdas, so it doesn't reference the class before its loaded
		return item == null ? ProjectEXItems.MATTER.get(this) : item;
	}

	@Nullable
	public Matter getPrev() {
		return this == BASIC ? null : VALUES[ordinal() - 1];
	}

	public String getMK() {
		return "[MK" + (ordinal() + 1) + "]";
	}

	public long getPowerFlowerOutput() {
		return collectorOutput * 18L + relayBonus * 30L;
	}
}
