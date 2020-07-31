package baka943.realmtweaks.common.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

public class Utils {

	public static ResourceLocation getRL(String name) {
		return new ResourceLocation(LibMisc.MOD_ID, name);
	}

	public static String getModId(ItemStack stack) {
		return stack.getItem().getCreatorModId(stack);
	}

	public static int getDimId(String name) {
		return DimensionType.byName(name).getId();
	}

	public static NBTTagCompound getTagSafe(NBTTagCompound tag) {
		return tag == null ? new NBTTagCompound() : tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}

}
