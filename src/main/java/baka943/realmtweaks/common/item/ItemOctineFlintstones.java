package baka943.realmtweaks.common.item;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemOctineFlintstones extends ItemFlintAndSteel implements IModelRegister {

	public ItemOctineFlintstones() {
		super();
		this.setRegistryName(Utils.getRL("octine_flintstones"));
		this.setTranslationKey(LibMisc.MOD_ID + ".octine_flintstones");
		this.setMaxDamage(-1);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote && worldIn.rand.nextInt(64) == 0) {
			player.setFire(5);
			player.sendStatusMessage(new TextComponentTranslation("tooltip.realmtweaks.octine_flintstones"), true);
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
