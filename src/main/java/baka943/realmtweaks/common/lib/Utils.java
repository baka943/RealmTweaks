package baka943.realmtweaks.common.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

public class Utils {

	public static ResourceLocation getRL(String name) {
		return new ResourceLocation(LibMisc.MOD_ID, name);
	}

	public static int getDimensionId(String dimensionName) {
		return DimensionType.byName(dimensionName).getId();
	}

	public static NBTTagCompound getTagSafe(NBTTagCompound tag) {
		return tag == null ? new NBTTagCompound() : tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}

}
