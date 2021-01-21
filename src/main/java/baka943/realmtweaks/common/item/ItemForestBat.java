package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.core.handler.FontRGB;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemForestBat extends ItemMod {

	public ItemForestBat() {
		super("forest_bat");
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(@Nonnull ItemStack stack) {
		return FontRGB.INSTANCE.init();
	}

}
