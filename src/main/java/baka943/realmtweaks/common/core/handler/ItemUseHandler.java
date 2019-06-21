package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.capability.circlegem.CircleGemType;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.item.misc.ItemGem;
import thebetweenlands.common.item.misc.ItemSwampTalisman;
import thebetweenlands.common.item.misc.ItemSwampTalisman.EnumTalisman;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.WorldProviderBetweenlands;

public class ItemUseHandler {

	@SubscribeEvent
	public void onItemUse(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();

		if(!world.isRemote) {
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			ItemStack stack = event.getItemStack();
			Item item = stack.getItem();

			EntityPlayer player = event.getEntityPlayer();

			//Obtain Realm Sapling
			if(item instanceof ItemGem && block == BlockRegistry.SAPLING_WEEDWOOD) {
				if(world.provider instanceof WorldProviderBetweenlands) {
					CircleGemType type = ((ItemGem) item).type;

					if(type == CircleGemType.NONE) {
						player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
					} else {
						world.setBlockToAir(pos);

						if(!player.capabilities.isCreativeMode) stack.shrink(1);

						switch(type) {
							case AQUA:
								world.setBlockState(pos,
										ModBlocks.NIGHTMARE_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.NIGHTMARE_SAPLING);
								break;

							case CRIMSON:
								world.setBlockState(pos,
										ModBlocks.ALTERNATE_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.ALTERNATE_SAPLING);
								break;

							case GREEN:
								world.setBlockState(pos,
										ModBlocks.ANOTHER_SAPLING.getDefaultState());
								Utils.worldSaplingText(player, ModBlocks.ANOTHER_SAPLING);
								break;
						}
					}
				} else {
					player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
				}
			}

			boolean inCustomListed = BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionTargetsList.isListed(state);

			//Disable Vanilla WeedwoodPortalTree
			if(item instanceof ItemSwampTalisman) {
				event.setUseItem(Event.Result.DENY);
				event.setCanceled(true);

				if(EnumTalisman.SWAMP_TALISMAN_0.isItemOf(stack)
						|| EnumTalisman.SWAMP_TALISMAN_5.isItemOf(stack)) {
					player.sendStatusMessage(new TextComponentTranslation("chat.realmtweaks.wrong"), true);
				}
			}
		}
	}

}
