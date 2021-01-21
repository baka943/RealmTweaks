package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.block.BlockRealmSapling;
import baka943.realmtweaks.common.entity.EntityBoneHook;
import baka943.realmtweaks.common.item.ItemOctineFlintstones;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.item.misc.ItemSwampTalisman.EnumTalisman;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.List;
import java.util.Random;

import static thebetweenlands.common.handler.OverworldItemHandler.*;

public class BetweenlandsTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BetweenlandsTweaks.class);

		FIRE_TOOL_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemOctineFlintstones);
		TORCH_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
		ROTTING_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemFood && (Utils.getModId(stack).equals("roots") || Utils.getModId(stack).equals("mysticalworld")));

		if(RealmTweaks.isRootsLoaded) {
			TOOL_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemDruidKnife);
		}

	}

	@SubscribeEvent
	public static void disableSwampTalisman(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();

		if(!world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			ItemStack stack = event.getItemStack();

			boolean inCustomListed = BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionTargetsList.isListed(state);

			if(EnumTalisman.SWAMP_TALISMAN_0.isItemOf(stack) || EnumTalisman.SWAMP_TALISMAN_5.isItemOf(stack)) {
				if(state.getBlock() instanceof BlockSapling || inCustomListed) {
					player.sendStatusMessage(new TextComponentTranslation("chat." + LibMisc.MOD_ID + ".wrong"), true);

					event.setUseItem(Event.Result.DENY);
					event.setCanceled(true);
				}

				if(state.getBlock() instanceof BlockRealmSapling) {
					event.setUseItem(Event.Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public static void boneFishHook(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote
				&& world.provider.getDimension() == Utils.getDimId("betweenlands")) {
			if(entity instanceof EntityFishHook
					&& entity.getClass().equals(EntityFishHook.class)) {
				EntityPlayer player = ((EntityFishHook) entity).getAngler();
				ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

				if(stack.getItem() != Items.FISHING_ROD) {
					stack = player.getHeldItem(EnumHand.OFF_HAND);
				}

				entity.setDead();
				event.setCanceled(true);

				EntityBoneHook hook = new EntityBoneHook(world, player);

				int speed = EnchantmentHelper.getFishingSpeedBonus(stack);
				int luck = EnchantmentHelper.getFishingLuckBonus(stack);

				if(speed > 0) hook.setLureSpeed(speed);

				if(luck > 0) hook.setLuck(luck);

				world.spawnEntity(hook);
			}
		}
	}

	@SubscribeEvent
	public static void onBlockCreate(BlockEvent.NeighborNotifyEvent event) {
		try {
			BlockPos pos = event.getPos();
			World world = event.getWorld();
			Block block = event.getState().getBlock();

			if(!world.isRemote
					&& world.provider.getDimension() == Utils.getDimId("betweenlands")) {
				if(block == Blocks.COBBLESTONE) {
					world.setBlockState(pos, BlockRegistry.BETWEENSTONE.getDefaultState());
				}

				if(block == Blocks.STONE && event.getState().getBlock().getMetaFromState(event.getState()) == 0) {
					world.setBlockState(pos, BlockRegistry.BETWEENSTONE.getDefaultState());
				}
			}
		} catch(Throwable throwable) {
			System.out.println("NO!");
		}
	}

	@SubscribeEvent
	public static void onDrops(BlockEvent.HarvestDropsEvent event) {
		Block block = event.getState().getBlock();
		List<ItemStack> drops = event.getDrops();
		Random rand = event.getWorld().rand;

		if(RealmTweaks.isRootsLoaded) {
			if(block == Blocks.TALLGRASS) {
				ItemStack blacklist = ItemStack.EMPTY;

				for(ItemStack stack : drops) {
					if(stack.getItem() == ModItems.wildroot) {
						drops.remove(stack);
					}

					if(stack.getItem() == Items.WHEAT_SEEDS && rand.nextInt(3) == 0) {
						blacklist = stack;

						break;
					}
				}

				if(!blacklist.isEmpty()) {
					drops.remove(blacklist);
					drops.add(new ItemStack(rand.nextBoolean() ? Items.PUMPKIN_SEEDS : Items.MELON_SEEDS));
				}
			}

			if(block == BlockRegistry.SWAMP_TALLGRASS) {
				for(ItemStack stack : drops) {
					if(stack.isEmpty() && rand.nextInt(10) == 0) {
						drops.add(new ItemStack(ModItems.wildroot));
					}
				}
			}
		}
	}

}
