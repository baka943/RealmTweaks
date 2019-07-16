package baka943.realmtweaks.common.core.handler;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.item.misc.ItemSwampTalisman.EnumTalisman;

public class ItemUseHandler {

	/**
	 * Disable default WeedwoodPortalTree
	 * @param event rightClickBlockEvent
	 */
	@SubscribeEvent
	public void onItemUse(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();

		if(!world.isRemote) {
			IBlockState state = world.getBlockState(event.getPos());
			ItemStack stack = event.getItemStack();
			EntityPlayer player = event.getEntityPlayer();

			boolean isBlockBush = this.isBlockBush(state);
			boolean inCustomListed = BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionTargetsList.isListed(state);

			if((isBlockBush || inCustomListed) && (EnumTalisman.SWAMP_TALISMAN_0.isItemOf(stack) || EnumTalisman.SWAMP_TALISMAN_5.isItemOf(stack))) {
				player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);

				event.setUseItem(Event.Result.DENY);
				event.setCanceled(true);
			}
		}
	}

	private boolean isBlockBush(IBlockState state) {
		return state.getBlock() instanceof BlockBush;
	}

}
