package baka943.realmtweaks.common.core.handler;

import net.minecraftforge.event.world.BlockEvent.PortalSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PortalHandler {

	@SubscribeEvent
	public void netherPortalSpawn(PortalSpawnEvent event) {
		event.setCanceled(true);
	}

}
