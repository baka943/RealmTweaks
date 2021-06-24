package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class MobHandler {

	@SubscribeEvent
	public static void disableSquid(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if(!event.getWorld().isRemote && entity instanceof EntitySquid) {
			entity.setDead();
			event.setCanceled(true);
		}
	}

}
