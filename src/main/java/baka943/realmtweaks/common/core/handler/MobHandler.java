package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class MobHandler {

	@SubscribeEvent
	public static void disableSquid(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote && entity instanceof EntitySquid) {
			entity.setDead();
			event.setCanceled(true);
		}
	}

}
