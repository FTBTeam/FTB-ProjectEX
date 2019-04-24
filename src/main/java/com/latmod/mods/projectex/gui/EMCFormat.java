package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEXConfig;
import net.minecraft.client.gui.GuiScreen;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;

/**
 * @author LatvianModder
 */
public class EMCFormat extends DecimalFormat
{
	public static final EMCFormat INSTANCE = new EMCFormat(false);
	public static final EMCFormat INSTANCE_IGNORE_SHIFT = new EMCFormat(true);

	private final boolean ignoreShift;

	private EMCFormat(boolean is)
	{
		super("#,###");
		setRoundingMode(RoundingMode.DOWN);
		ignoreShift = is;
	}

	@Override
	public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition)
	{
		if (ProjectEXConfig.general.override_emc_formatter && number >= 1_000_000D && (ignoreShift || !GuiScreen.isShiftKeyDown()))
		{
			double num;
			char c;

			if (number >= 1_000_000_000_000_000_000_000_000D)
			{
				num = number / 1_000_000_000_000_000_000_000_000D;
				c = 'Y';
			}
			else if (number >= 1_000_000_000_000_000_000_000D)
			{
				num = number / 1_000_000_000_000_000_000_000D;
				c = 'Z';
			}
			else if (number >= 1_000_000_000_000_000_000D)
			{
				num = number / 1_000_000_000_000_000_000D;
				c = 'E';
			}
			else if (number >= 1_000_000_000_000_000D)
			{
				num = number / 1_000_000_000_000_000D;
				c = 'P';
			}
			else if (number >= 1_000_000_000_000D)
			{
				num = number / 1_000_000_000_000D;
				c = 'T';
			}
			else if (number >= 1_000_000_000D)
			{
				num = number / 1_000_000_000D;
				c = 'G';
			}
			else
			{
				num = number / 1_000_000D;
				c = 'M';
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append(String.format("%.02f", num));
			buffer.append(c);
			return buffer;
		}

		return super.format(number, result, fieldPosition);
	}

	@Override
	public StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition)
	{
		if (ProjectEXConfig.general.override_emc_formatter && number >= 1_000_000L && (ignoreShift || !GuiScreen.isShiftKeyDown()))
		{
			double num;
			char c;

			if (number >= 1_000_000_000_000_000_000L)
			{
				num = number / 1_000_000_000_000_000_000D;
				c = 'E';
			}
			else if (number >= 1_000_000_000_000_000L)
			{
				num = number / 1_000_000_000_000_000D;
				c = 'P';
			}
			else if (number >= 1_000_000_000_000L)
			{
				num = number / 1_000_000_000_000D;
				c = 'T';
			}
			else if (number >= 1_000_000_000L)
			{
				num = number / 1_000_000_000D;
				c = 'G';
			}
			else
			{
				num = number / 1_000_000D;
				c = 'M';
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append(String.format("%.02f", num));
			buffer.append(c);
			return buffer;
		}

		return super.format(number, result, fieldPosition);
	}
}