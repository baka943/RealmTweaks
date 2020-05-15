package baka943.realmtweaks.common.core.handler;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class SoulNetworkHandler {

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;

		if(event.phase == TickEvent.Phase.START) {
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(network.getOrbTier() > 0 && !world.isRemote) {
				int amount = (int)Math.ceil(network.getCurrentEssence() * 0.01);

				amount *= network.getOrbTier();

				if(world.getTotalWorldTime() % 30 == 0) {
					network.syphonAndDamage(network.getPlayer(), new SoulTicket(Math.min(amount, 2500)));

					if(network.getCurrentEssence() == 0) {
						network.hurtPlayer(player, 1.0F);
					}
				}
			}
		}
	}

}
