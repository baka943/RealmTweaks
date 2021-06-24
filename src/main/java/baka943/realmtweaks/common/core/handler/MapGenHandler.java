package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.lib.Utils;
import baka943.realmtweaks.common.world.gen.structure.MapGenMineshaftEmpty;
import baka943.realmtweaks.common.world.gen.structure.MapGenStrongholdEmpty;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapGenHandler {

	@SubscribeEvent
	public void initMapGen(InitMapGenEvent event) {
		if(event.getType() == EventType.STRONGHOLD) {
			event.setNewGen(new MapGenStrongholdEmpty());
		}

		if(event.getType() == EventType.MINESHAFT) {
			event.setNewGen(new MapGenMineshaftEmpty());
		}
	}

	@SubscribeEvent
	public void populateChunk(Populate event) {
		if(event.getType() == Populate.EventType.DUNGEON) {
			event.setResult(Event.Result.DENY);
		}

		if(event.getWorld().provider.getDimension() == Utils.getDimId("overworld")
				&& event.getType() == Populate.EventType.LAVA) {
			event.setResult(Event.Result.DENY);
		}
	}

}
