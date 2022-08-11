package baka943.realmtweaks.common.item.armor;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSnowneeeBoots extends ItemSnowneeeArmor {

	public ItemSnowneeeBoots() {
		super(EntityEquipmentSlot.FEET, "snowneee_snowball_iii");

	}

	@Override
	public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {
		if(!world.isRemote) {
			if(hasArmorSet(player)) {
				for(int l = 0; l < 4; ++l) {
					int i = MathHelper.floor(player.posX + (l % 2 * 2 - 1) * 0.25F);
					int j = MathHelper.floor(player.posY);
					int k = MathHelper.floor(player.posZ + (l / 2 % 2 * 2 - 1) * 0.25F);
					BlockPos blockpos = new BlockPos(i, j, k);

					if(world.getBlockState(blockpos).getMaterial() == Material.AIR && world.getBiome(blockpos).getTemperature(blockpos) < 0.8F && Blocks.SNOW_LAYER.canPlaceBlockAt(world, blockpos)) {
						world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
					}
				}
			}

			EnchantmentFrostWalker.freezeNearby(player, world, player.getPosition(), 1);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flags) {
		list.add(I18n.format("tooltip.realmtweaks.snowneee_boots.desc"));
		super.addInformation(stack, world, list, flags);
	}

}
