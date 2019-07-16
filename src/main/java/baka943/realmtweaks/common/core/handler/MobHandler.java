package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.entity.EntityBoneHook;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.entity.mobs.EntitySwampHag;
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

			if(entity instanceof EntitySwampHag) {
				entity.setDead();
				event.setCanceled(true);

				EntitySwampSpider spider = new EntitySwampSpider(world);
				spider.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0, 0);
				world.spawnEntity(spider);
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

				if(speed > 0) {
					hook.setLureSpeed(speed);
				}

				if(luck > 0) {
					hook.setLuck(luck);
				}

				world.spawnEntity(hook);
			}
		}
	}

	@SubscribeEvent
	public void mobDrops(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;

		if(!world.isRemote && entity instanceof EntityEnderman) {
			event.getDrops().clear();

			Random rand = new Random();

			if(rand.nextInt(3) == 0) {
				entity.entityDropItem(new ItemStack(ModItems.ENDER_SHARD), 0.0F);
			}
		}
	}

}
