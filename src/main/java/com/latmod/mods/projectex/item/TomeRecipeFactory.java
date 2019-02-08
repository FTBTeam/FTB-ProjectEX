package com.latmod.mods.projectex.item;

import com.google.gson.JsonObject;
import com.latmod.mods.projectex.ProjectEXConfig;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

/**
 * @author LatvianModder
 */
public class TomeRecipeFactory implements IConditionFactory
{
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json)
	{
		return () -> ProjectEXConfig.items.tome;
	}
}