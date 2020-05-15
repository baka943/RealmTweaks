package baka943.realmtweaks.common.core.handler;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import thebetweenlands.common.item.tools.ItemBLAxe;
import thebetweenlands.common.item.tools.ItemBLPickaxe;
import thebetweenlands.common.item.tools.ItemBLShovel;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class EventHandler {

	public static final String TAG_HAS_STARTING_ITEM = LibMisc.MOD_ID + ".starting_item";
	public static final String TAG_HAS_SACRIFICE_DAGGER = LibMisc.MOD_ID + ".sacrifice_dagger";

	public static final Map<ResourceLocation, Predicate<ItemStack>> PAPERTOOL_LIST = new HashMap<>();
	public static final Map<ResourceLocation, Predicate<ItemStack>> PARCHMENTTOOL_LIST = new HashMap<>();
	public static final Map<ResourceLocation, Predicate<ItemStack>> TOMETOOL_LIST = new HashMap<>();
	public static final Map<ResourceLocation, Predicate<ItemStack>> DEBUGTOOL_LIST = new HashMap<>();

	static {
		PAPERTOOL_LIST.put(new ResourceLocation(LibMisc.MOD_ID, "default_blacklist"), stack -> {
			return stack.getItem() instanceof ItemTool && (stack.getItem().getCreatorModId(stack).equals("minecraft"));
		});

		PARCHMENTTOOL_LIST.put(new ResourceLocation(LibMisc.MOD_ID, "default_blacklist"), stack -> {
			return stack.getItem() instanceof ItemBLAxe || stack.getItem() instanceof ItemBLPickaxe || stack.getItem() instanceof ItemBLShovel;
		});

		TOMETOOL_LIST.put(new ResourceLocation(LibMisc.MOD_ID, "default_blacklist"), stack -> {
			return stack.getItem() instanceof ItemTool && (stack.getItem().getCreatorModId(stack).equals("bloodmagic"));
		});

		DEBUGTOOL_LIST.put(new ResourceLocation(LibMisc.MOD_ID, "default_blacklist"), stack -> {
			return stack.getItem() instanceof ItemTool && (stack.getItem().getCreatorModId(stack).equals("embers"));
		});
	}

	/**
	 * Give Starting Items
	 * @param event PlayerLoggedInEvent
	 */
	@SubscribeEvent
	public void onStartingItems(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;

		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = Utils.getTagSafe(playerData);

		if(!data.getBoolean(TAG_HAS_STARTING_ITEM)) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.ADVANCEMENT_BOOK));

			data.setBoolean(TAG_HAS_STARTING_ITEM, true);
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	/**
	 * Update player inventory items
	 * @param event playerTickEvent
	 */
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;

		if(event.phase == TickEvent.Phase.END && !player.world.isRemote && player.ticksExisted % 5 == 0 && !player.capabilities.isCreativeMode) {
			updatePlayerInventory(player);
		}
	}

	@SubscribeEvent
	public void onBlockCreate(BlockEvent.NeighborNotifyEvent event) {
		try {
			BlockPos pos = event.getPos();
			World world = event.getWorld();
			Block block = event.getState().getBlock();

			if(!world.isRemote && world.provider.getDimension() == Utils.getDimensionId("betweenlands")) {
				if(block == Blocks.COBBLESTONE) {
					world.setBlockState(pos, BlockRegistry.BETWEENSTONE.getDefaultState());
				}

				if(block == Blocks.STONE && event.getState().getBlock().getMetaFromState(event.getState()) == 0) {
					world.setBlockState(pos, BlockRegistry.SMOOTH_BETWEENSTONE.getDefaultState());
				}
			}
		} catch(Throwable throwable) {
			System.out.println("NO!");
		}
	}

	@SubscribeEvent
	public  void itemDecay(ItemExpireEvent event) {
		if(!event.getEntityItem().getItem().isEmpty()) {
			Item item = event.getEntityItem().getItem().getItem();
			BlockPos pos = event.getEntityItem().getPosition();
			Block block = event.getEntityItem().getEntityWorld().getBlockState(pos.down()).getBlock();

			if(item == ModItems.BLOOD_TEAR && block == Blocks.END_STONE) {
				event.getEntityItem().getEntityWorld().setBlockState(pos.down(), ModBlocks.BLOODED_ENDSTONE.getDefaultState());
			}
		}
	}

	@SubscribeEvent
	public void itemToss(ItemTossEvent event) {
		if(!event.getEntityItem().getItem().isEmpty()) {
			Item item = event.getEntityItem().getItem().getItem();

			if(item == ModItems.BLOOD_TEAR) {
				event.getEntityItem().lifespan = 200;
			}
		}
	}

	/**
	 * Replace Tools where in player inventory
	 * @param player EntityPlayer
	 */
	private static void updatePlayerInventory(EntityPlayer player) {
		InventoryPlayer inventory = player.inventory;
		int invCount = inventory.getSizeInventory();
		int dimension = player.dimension;

		for(int i = 0; i < invCount; i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if(!stack.isEmpty()) {
				//Paper Tool
				if(dimension != Utils.getDimensionId("overworld") && dimension != Utils.getDimensionId("the_nether") && isToolPapered(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PAPER_TOOL);

					ModItems.PAPER_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if((dimension == Utils.getDimensionId("overworld") || dimension == Utils.getDimensionId("the_nether")) && stack.getItem() == ModItems.PAPER_TOOL) {
					ItemStack originalStack = ModItems.PAPER_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				//Parchment Tool
				if(dimension != Utils.getDimensionId("betweenlands") && isToolParchmented(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PARCHMENT_TOOL);

					ModItems.PARCHMENT_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimensionId("betweenlands")) {
					if(stack.getItem() == ModItems.PARCHMENT_TOOL) {
						ItemStack originalStack = ModItems.PARCHMENT_TOOL.getOriginalStack(stack);

						if(!originalStack.isEmpty()) {
							inventory.setInventorySlotContents(i, originalStack);
						} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}

					if(stack.getItem().equals(new ItemBlock(Blocks.COBBLESTONE))) {
						inventory.setInventorySlotContents(i, new ItemStack(BlockRegistry.BETWEENSTONE));
					}

					if(stack.getItem().equals(new ItemBlock(Blocks.STONE)) && stack.getItem().getMetadata(stack) == 0) {
						inventory.setInventorySlotContents(i, new ItemStack(BlockRegistry.SMOOTH_BETWEENSTONE, 1, 0));
					}
				}

				//Tome Tool
				if(dimension != Utils.getDimensionId("the_end") && isToolTomed(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.TOME_TOOL);

					ModItems.TOME_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimensionId("the_end")) {
					if(stack.getItem() == ModItems.TOME_TOOL) {
						ItemStack originalStack = ModItems.TOME_TOOL.getOriginalStack(stack);

						if(!originalStack.isEmpty()) {
							inventory.setInventorySlotContents(i, originalStack);
						} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}

					if(stack.getItem() == RegistrarBloodMagicItems.SACRIFICIAL_DAGGER) {
						NBTTagCompound playerData = player.getEntityData();
						NBTTagCompound data = Utils.getTagSafe(playerData);

						if(!data.hasKey(TAG_HAS_SACRIFICE_DAGGER)) {
							data.setBoolean(TAG_HAS_SACRIFICE_DAGGER, true);
							playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
						}
					}
				}

				//Debug Tool
				if(dimension != Utils.getDimensionId("lostcities") && isToolDebuged(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.DEBUG_TOOL);

					ModItems.DEBUG_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimensionId("lostcities") && stack.getItem() == ModItems.DEBUG_TOOL) {
					ItemStack originalStack = ModItems.DEBUG_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}
		}
	}

	private static boolean isToolPapered(ItemStack stack) {
		for(Predicate<ItemStack> toollistPredicate : PAPERTOOL_LIST.values()) {
			if(toollistPredicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolParchmented(ItemStack stack) {
		for(Predicate<ItemStack> toollistPredicate : PARCHMENTTOOL_LIST.values()) {
			if(toollistPredicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolTomed(ItemStack stack) {
		for(Predicate<ItemStack> toollistPredicate : TOMETOOL_LIST.values()) {
			if(toollistPredicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolDebuged(ItemStack stack) {
		for(Predicate<ItemStack> toollistPredicate : DEBUGTOOL_LIST.values()) {
			if(toollistPredicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

}
