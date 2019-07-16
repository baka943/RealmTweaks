package baka943.realmtweaks.common.core.handler;

import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OreGenHandler {

	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
	public void oreGenMinable(GenerateMinable event) {
		switch(event.getType()) {
			case COAL:
			case IRON:
			case GOLD:
			case LAPIS:
			case REDSTONE:
			case DIAMOND:
			case EMERALD:
				event.setResult(Event.Result.DENY);
				break;

			default:
				event.setResult(Event.Result.DEFAULT);
				break;
		}
	}

}
