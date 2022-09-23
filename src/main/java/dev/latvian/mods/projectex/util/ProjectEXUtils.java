package dev.latvian.mods.projectex.util;

import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ProjectEXUtils {
    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ProjectEX.MOD_ID, path);
    }

    public static ItemStack fixOutput(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(stack, 1);

        if (stack1.getMaxDamage() > 0) {
            stack1.setDamageValue(0);
        }

        // TODO 1.18 verify what should replace this, if anything
//        if (!NBTWhitelist.shouldDupeWithNBT(stack1)) {
//            stack1.setTagCompound(null);
//
//            try {
//                stack1.getItem().readNBTShareTag(stack1, null);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                //Catch exception if readNBTShareTag fails
//            }
//        }

        return stack1;

    }
}
