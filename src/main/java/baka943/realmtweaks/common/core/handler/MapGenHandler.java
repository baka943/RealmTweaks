package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.world.gen.structure.MapGenStrongholdEmpty;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapGenHandler {

	@SubscribeEvent
	public void initMapGen(InitMapGenEvent event) {
		if(event.getType() == EventType.STRONGHOLD) {
			event.setNewGen(new MapGenStrongholdEmpty());
		}
	}

}
