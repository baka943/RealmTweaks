package baka943.realmtweaks.common.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class Utils {

	public static ResourceLocation getRL(String name) {
		return new ResourceLocation(LibMisc.MOD_ID, name);
	}

	public static void worldSaplingText(EntityPlayer playerIn, Block sapling) {
		playerIn.sendStatusMessage(
				new TextComponentTranslation(
						"chat.realmtweaks.world_sapling",
						new TextComponentTranslation(sapling.getTranslationKey() + ".name")
				),
				true
		);
	}

}
