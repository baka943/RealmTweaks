package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.entity.EntityEnderShard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemEnderShard extends ItemMod {

	public ItemEnderShard() {
		super("ender_shard");
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(!playerIn.capabilities.isCreativeMode) {
			itemstack.shrink(1);
			playerIn.getCooldownTracker().setCooldown(this, 20);
		}

		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!worldIn.isRemote) {
			EntityEnderShard enderShard = new EntityEnderShard(worldIn, playerIn);

			enderShard.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntity(enderShard);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));

		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

}
