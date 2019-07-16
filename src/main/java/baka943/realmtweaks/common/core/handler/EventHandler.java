package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import thebetweenlands.common.handler.OverworldItemHandler;
import thebetweenlands.common.registries.ItemRegistry;

public class EventHandler {

	private static final String TAG_HAS_STARTING_ITEM = LibMisc.MOD_ID + ".starting_item";

	/**
	 * Give Starting Items
	 * @param event PlayerLoggedInEvent
	 */
	@SubscribeEvent
	public void onStartingItems(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;

		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTagSafe(playerData);

		if(!data.getBoolean(TAG_HAS_STARTING_ITEM)) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.ADVANCEMENT_BOOK));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemRegistry.EMPTY_AMATE_MAP));

			data.setBoolean(TAG_HAS_STARTING_ITEM, true);
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	/**
	 * Disable Nether Portal
	 * @param event PortalSpawnEvent
	 */
	@SubscribeEvent
	public void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
		event.setCanceled(true);
	}

	/**
	 * Replace Tool
	 * @param event playerTickEvent
	 */
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;

		if(!player.world.isRemote && player.ticksExisted % 5 == 0 && !player.capabilities.isCreativeMode) {
			updatePlayerInventory(player);
		}
	}

	/**
	 * Replace Tool while pickup
	 * @param event itemPickupEvent
	 */
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;

		if(!world.isRemote && !player.capabilities.isCreativeMode) {
			ItemStack stack = event.getItem().getItem();

			if(!stack.isEmpty()) {
				if(player.dimension != DimensionType.OVERWORLD.getId()) {
					if(OverworldItemHandler.isToolWeakened(stack)) {
						ItemStack replaceToolStack = new ItemStack(ModItems.PAPER_TOOL);

						ModItems.PAPER_TOOL.setOriginalStack(replaceToolStack, stack);
						event.getItem().setItem(replaceToolStack);
					}
				} else if(stack.getItem() == ModItems.PAPER_TOOL) {
					ItemStack originalStack = ModItems.PAPER_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						event.getItem().setItem(originalStack);
					} else {
						event.getItem().setDead();
						event.setCanceled(true);
					}
				}
			}
		}
	}

	private static void updatePlayerInventory(EntityPlayer player) {
		InventoryPlayer inventory = player.inventory;
		int invCount = inventory.getSizeInventory();

		if(player.dimension != DimensionType.OVERWORLD.getId()) {
			//Set to paper tool
			for(int i = 0; i < invCount; i++) {
				ItemStack stack = inventory.getStackInSlot(i);

				if(!stack.isEmpty() && OverworldItemHandler.isToolWeakened(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PAPER_TOOL);

					ModItems.PAPER_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}
			}
		} else {
			//Revert paper tool
			for(int i = 0; i < invCount; i++) {
				ItemStack stack = inventory.getStackInSlot(i);

				if(!stack.isEmpty() && stack.getItem() == ModItems.PAPER_TOOL) {
					ItemStack originalStack = ModItems.PAPER_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}
		}
	}

	private static NBTTagCompound getTagSafe(NBTTagCompound tag) {
		return tag == null ? new NBTTagCompound() : tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}

}
