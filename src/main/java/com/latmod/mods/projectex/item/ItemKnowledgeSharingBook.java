package com.latmod.mods.projectex.item;

import com.latmod.mods.projectex.ProjectEXUtils;
import com.latmod.mods.projectex.integration.PersonalEMC;
import com.mojang.util.UUIDTypeAdapter;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ItemKnowledgeSharingBook extends Item
{
	public ItemKnowledgeSharingBook()
	{
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking())
		{
			if (!world.isRemote)
			{
				stack.setTagInfo("id", new NBTTagString(UUIDTypeAdapter.fromUUID(player.getUniqueID())));
				stack.setTagInfo("name", new NBTTagString(player.getName()));
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("id"))
		{
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}

		UUID id = UUIDTypeAdapter.fromString(stack.getTagCompound().getString("id"));

		if (id.equals(player.getUniqueID()))
		{
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}

		if (!world.isRemote)
		{
			IKnowledgeProvider playerKnowledge = PersonalEMC.get(player);
			IKnowledgeProvider otherKnowledge = PersonalEMC.get(world, id);

			for (ItemStack stack1 : otherKnowledge.getKnowledge())
			{
				ProjectEXUtils.addKnowledge(player, playerKnowledge, stack1);
			}

			playerKnowledge.sync((EntityPlayerMP) player);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);
		}
		else
		{
			for (int i = 0; i < 5; i++)
			{
				Vec3d vec3d = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
				vec3d = vec3d.rotatePitch(-player.rotationPitch * 0.017453292F);
				vec3d = vec3d.rotateYaw(-player.rotationYaw * 0.017453292F);
				double d0 = (double) (-world.rand.nextFloat()) * 0.6D - 0.3D;
				Vec3d vec3d1 = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
				vec3d1 = vec3d1.rotatePitch(-player.rotationPitch * 0.017453292F);
				vec3d1 = vec3d1.rotateYaw(-player.rotationYaw * 0.017453292F);
				vec3d1 = vec3d1.add(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(this), 0);
			}
		}

		stack.shrink(1);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("id");
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("name"))
		{
			tooltip.add(stack.getTagCompound().getString("name"));
		}
	}
}