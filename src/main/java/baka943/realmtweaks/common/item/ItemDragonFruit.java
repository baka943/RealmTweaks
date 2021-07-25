package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonFruit extends ItemFood implements IModelRegister {

	public ItemDragonFruit() {
		super(4, 0.3F, false);
		this.setRegistryName(Utils.getRL("dragon_fruit"));
		this.setTranslationKey(LibMisc.MOD_ID + ".dragon_fruit");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityLivingBase entityLiving) {
		if(!worldIn.isRemote && worldIn.rand.nextInt(8) == 0
				&& entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			ItemStack dragon_breath = new ItemStack(Items.DRAGON_BREATH);

			if(!player.inventory.addItemStackToInventory(dragon_breath)) {
				player.dropItem(dragon_breath, false);
			}
		}

		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		tooltip.add(I18n.format("tooltip." + LibMisc.MOD_ID + ".dragon_fruit"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
