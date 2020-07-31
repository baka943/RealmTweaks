package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.tab.BLCreativeTabs;

import javax.annotation.Nonnull;

public class ItemOctineFlintstones extends ItemFlintAndSteel implements IModelRegister {

	public ItemOctineFlintstones() {
		super();
		this.setRegistryName(Utils.getRL("octine_flintstones"));
		this.setTranslationKey(LibMisc.MOD_ID + ".octine_flintstones");
		this.setMaxDamage(256);
		this.setCreativeTab(BLCreativeTabs.GEARS);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
