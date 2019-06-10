package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.entity.mobs.EntitySwampHag;

import java.util.Random;

public class MobHandler {

	@SubscribeEvent
	public void mobSpawn(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(entity instanceof EntitySquid || entity instanceof EntityPigZombie
				|| (!(world.provider instanceof WorldProviderEnd)
				&& entity instanceof EntityEnderman)) {
			entity.setDead();
			event.setCanceled(true);
		}

		if(entity instanceof EntitySwampHag) {
			entity.setDead();
			event.setCanceled(true);

			EntityCaveSpider spider = new EntityCaveSpider(world);
			spider.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0, 0);
			world.spawnEntity(spider);
		}
	}

	@SubscribeEvent
	public void mobDrops(LivingDropsEvent event) {
		Entity entity = event.getEntity();

		if(entity instanceof EntityEnderman) {
			event.getDrops().clear();

			Random rand = new Random();

			if(rand.nextInt(3) == 0) {
				entity.entityDropItem(new ItemStack(ModItems.ENDER_SHARD), 0.0F);
			}
		}
	}

}
