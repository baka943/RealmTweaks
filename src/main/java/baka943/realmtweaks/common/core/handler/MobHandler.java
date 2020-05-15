package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.entity.EntityBoneHook;
import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.entity.mobs.EntityDarkDruid;
import thebetweenlands.common.world.WorldProviderBetweenlands;

import java.util.Random;

public class MobHandler {

	@SubscribeEvent
	public void mobSpawn(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote) {
			if(entity instanceof EntitySquid || entity instanceof EntityPigZombie
					|| (!(world.provider instanceof WorldProviderEnd)
					&& entity instanceof EntityEnderman)) {
				entity.setDead();
				event.setCanceled(true);
			}

			if(entity instanceof EntityFishHook
					&& entity.getClass().equals(EntityFishHook.class)
					&& world.provider instanceof WorldProviderBetweenlands) {
				EntityPlayer player = ((EntityFishHook) entity).getAngler();
				ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

				if(stack.getItem() != Items.FISHING_ROD) {
					stack = player.getHeldItem(EnumHand.OFF_HAND);
				}

				entity.setDead();
				event.setCanceled(true);

				EntityBoneHook hook = new EntityBoneHook(world, player);

				int speed = EnchantmentHelper.getFishingSpeedBonus(stack);
				int luck = EnchantmentHelper.getFishingLuckBonus(stack);

				if(speed > 0) hook.setLureSpeed(speed);

				if(luck > 0) hook.setLuck(luck);

				world.spawnEntity(hook);
			}
		}
	}

	@SubscribeEvent
	public void mobDrops(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;

		if(!world.isRemote) {
			if(entity instanceof EntityEnderman) {
				event.getDrops().clear();

				Random rand = new Random();

				if(rand.nextInt(3) == 0) {
					entity.entityDropItem(new ItemStack(ModItems.ENDER_SHARD), 0.0F);
				}
			}

			if(entity instanceof EntityDarkDruid) {
				event.getDrops().clear();
			}
		}
	}

	@SubscribeEvent
	public void playerDrops(LivingHurtEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			DamageSource source = event.getSource();

			if(source.getTrueSource() instanceof EntityEnderman) {
				EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(ModItems.BLOOD_TEAR));
				item.motionX = 0;
				item.motionY = 0;
				item.motionZ = 0;
				player.world.spawnEntity(item);
				item.setDefaultPickupDelay();
			}

			if(event.getSource().damageType.equals("mob")) {
				float currentHealth = player.getHealth();
				float maxHealth = player.getMaxHealth();
				float damage = event.getAmount();

				if(currentHealth < maxHealth) {
					event.setAmount(Math.min(0.1F, damage * (currentHealth / maxHealth)));
				}
			}
		}

	}

}
