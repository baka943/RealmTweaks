package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.registries.ItemRegistry;

public class ItemWeedwoodRod extends ItemFishingRod implements IModelRegister {

	private static final String NAME = "weedwood_fishing_rod";

	public ItemWeedwoodRod() {
		super();
		this.setMaxDamage(256);
		this.setRegistryName(Utils.getRL(NAME));
		this.setTranslationKey(LibMisc.MOD_ID + "." + NAME);

		this.addPropertyOverride(new ResourceLocation("cast"), (stack, worldIn, entityIn) -> {
			if(entityIn == null) {
				return 0;
			} else {
				boolean inMainHand = entityIn.getHeldItemMainhand() == stack;
				boolean inOffHand = entityIn.getHeldItemOffhand() == stack;

				if(entityIn.getHeldItemMainhand().getItem() instanceof ItemWeedwoodRod) {
					inOffHand = false;
				}

				return (inMainHand || inOffHand) && entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).fishEntity != null ? 1 : 0;
			}
		});
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == new ItemStack(ItemRegistry.ITEMS_MISC, 1, 2).getItem();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
