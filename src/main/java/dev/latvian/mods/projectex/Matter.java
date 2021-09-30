package dev.latvian.mods.projectex;

import dev.latvian.mods.projectex.item.ProjectEXItems;
import moze_intel.projecte.gameObjs.registries.PEItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
public enum Matter {
	BASIC("basic", "Basic", false, () -> Items.DIAMOND_BLOCK),
	DARK("dark", "Dark", false, () -> PEItems.DARK_MATTER.get()),
	RED("red", "Red", false, () -> PEItems.RED_MATTER.get()),
	MAGENTA("magenta", "Magenta", true, null),
	PINK("pink", "Pink", true, null),
	PURPLE("purple", "Purple", true, null),
	VIOLET("violet", "Violet", true, null),
	BLUE("blue", "Blue", true, null),
	CYAN("cyan", "Cyan", true, null),
	GREEN("green", "Green", true, null),
	LIME("lime", "Lime", true, null),
	YELLOW("yellow", "Yellow", true, null),
	ORANGE("orange", "Orange", true, null),
	WHITE("white", "White", true, null),
	FADING("fading", "Fading", true, null),
	FINAL("final", "The Final", false, () -> ProjectEXItems.FINAL_STAR_SHARD.get());

	public static final Matter[] VALUES = values();

	public final String name;
	public final String displayName;
	public final boolean hasMatterItem;

	public long collectorOutput;
	public long relayBonus;

	@Nullable
	public final Supplier<Item> item;

	Matter(String n, String d, boolean i, Supplier<Item> it) {
		name = n;
		displayName = d;
		hasMatterItem = i;
		item = it;

		collectorOutput = 1L << ordinal();
		relayBonus = 1L << ordinal();
	}

	public Supplier<Item> getItem() {
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
