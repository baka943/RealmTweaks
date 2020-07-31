package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

//@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class PaperToolHandler {

	private static final Map<ResourceLocation, Predicate<ItemStack>> PAPERTOOL_LIST = new HashMap<>();
	private static final Map<ResourceLocation, Predicate<ItemStack>> PAPERTOOL_BLACKLIST = new HashMap<>();

	private static final Map<ResourceLocation, Predicate<ItemStack>> PARCHMENTTOOL_LIST = new HashMap<>();
	private static final Map<ResourceLocation, Predicate<ItemStack>> PARCHMENTTOOL_BLACKLIST = new HashMap<>();

	private static final Map<ResourceLocation, Predicate<ItemStack>> ENDERTOOL_LIST = new HashMap<>();
	private static final Map<ResourceLocation, Predicate<ItemStack>> ENDERTOOL_BLACKLIST = new HashMap<>();

	private static final Map<ResourceLocation, Predicate<ItemStack>> DEBUGTOOL_LIST = new HashMap<>();
	private static final Map<ResourceLocation, Predicate<ItemStack>> DEBUGTOOL_BLACKLIST = new HashMap<>();

	private static final List<String> DISABLE_LIST = new ArrayList<>();

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;

		if(event.phase == TickEvent.Phase.END && !player.world.isRemote && player.ticksExisted % 5 == 0) {
			updatePlayerInventory(player);
		}
	}

	private static void updatePlayerInventory(EntityPlayer player) {
		InventoryPlayer inventory = player.inventory;
		int invCount = inventory.getSizeInventory();
		int dimension = player.dimension;

		for(int i = 0; i < invCount; i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if(!stack.isEmpty()) {
				Item item = stack.getItem();

				//Paper Tool
				if(dimension != Utils.getDimId("overworld") && dimension != Utils.getDimId("the_nether") && isToolPapered(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PAPER_TOOL);

					ModItems.PAPER_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if((dimension == Utils.getDimId("overworld") || dimension == Utils.getDimId("the_nether")) && item == ModItems.PAPER_TOOL) {
					ItemStack originalStack = ModItems.PAPER_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				//Parchment Tool
				if(dimension != Utils.getDimId("betweenlands") && isToolParchmented(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PARCHMENT_TOOL);

					ModItems.PARCHMENT_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimId("betweenlands")) {
					if(item == ModItems.PARCHMENT_TOOL) {
						ItemStack originalStack = ModItems.PARCHMENT_TOOL.getOriginalStack(stack);

						if(!originalStack.isEmpty()) {
							inventory.setInventorySlotContents(i, originalStack);
						} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}

					if(item.equals(new ItemBlock(Blocks.COBBLESTONE))) {
						inventory.setInventorySlotContents(i, new ItemStack(BlockRegistry.BETWEENSTONE, stack.getCount()));
					}

					if(item.equals(new ItemBlock(Blocks.STONE)) && item.getMetadata(stack) == 0) {
						inventory.setInventorySlotContents(i, new ItemStack(BlockRegistry.SMOOTH_BETWEENSTONE, stack.getCount()));
					}
				}

				//Ender Tool
				if(dimension != Utils.getDimId("the_end") && isToolEndered(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.ENDER_TOOL);

					ModItems.ENDER_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimId("the_end") && item == ModItems.ENDER_TOOL) {
					ItemStack originalStack = ModItems.ENDER_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				//Debug Tool
				if(dimension != Utils.getDimId("lostcities") && isToolDebuged(stack)) {
					ItemStack replaceToolStack = new ItemStack(ModItems.DEBUG_TOOL);

					ModItems.DEBUG_TOOL.setOriginalStack(replaceToolStack, stack);
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				if(dimension == Utils.getDimId("lostcities") && item == ModItems.DEBUG_TOOL) {
					ItemStack originalStack = ModItems.DEBUG_TOOL.getOriginalStack(stack);

					if(!originalStack.isEmpty()) {
						inventory.setInventorySlotContents(i, originalStack);
					} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				//Disable Tool
				if(item instanceof ItemTool && !isToolPapered(stack) && !isToolParchmented(stack) && !isToolEndered(stack) && !isToolDebuged(stack) && !Utils.getModId(stack).equals("psi") && !Utils.getModId(stack).equals("draconicevolution") && !Utils.getModId(stack).equals("redstonearsenal") && !Utils.getModId(stack).equals("pyrotech") && !Utils.getModId(stack).equals("artisanworktables")) {
					ItemStack replaceToolStack = new ItemStack(ModItems.PAPER_TOOL);

					ModItems.PAPER_TOOL.setOriginalStack(replaceToolStack, new ItemStack(ModItems.FOREST_BAT));
					inventory.setInventorySlotContents(i, replaceToolStack);
				}

				//Disable Items
				for(String modid : DISABLE_LIST) {
					if(Utils.getModId(stack).equals(modid)) {
						inventory.setInventorySlotContents(i, new ItemStack(ModItems.FOREST_BAT));

						player.sendMessage(new TextComponentTranslation("chat." + LibMisc.MOD_ID + ".mod_disable"));
					}
				}
			}
		}
	}

	private static boolean isToolPapered(ItemStack stack) {
		for(Predicate<ItemStack> predicate : PAPERTOOL_BLACKLIST.values()) {
			if(!stack.isEmpty() && predicate.test(stack)) {
				return false;
			}
		}

		for(Predicate<ItemStack> predicate : PAPERTOOL_LIST.values()) {
			if(predicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolParchmented(ItemStack stack) {
		for(Predicate<ItemStack> predicate : PARCHMENTTOOL_BLACKLIST.values()) {
			if(!stack.isEmpty() && predicate.test(stack)) {
				return false;
			}
		}

		for(Predicate<ItemStack> predicate : PARCHMENTTOOL_LIST.values()) {
			if(predicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolEndered(ItemStack stack) {
		for(Predicate<ItemStack> predicate : ENDERTOOL_BLACKLIST.values()) {
			if(!stack.isEmpty() && predicate.test(stack)) {
				return false;
			}
		}

		for(Predicate<ItemStack> predicate : ENDERTOOL_LIST.values()) {
			if(predicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isToolDebuged(ItemStack stack) {
		for(Predicate<ItemStack> predicate : DEBUGTOOL_BLACKLIST.values()) {
			if(!stack.isEmpty() && predicate.test(stack)) {
				return false;
			}
		}

		for(Predicate<ItemStack> predicate : DEBUGTOOL_LIST.values()) {
			if(predicate.test(stack)) {
				return true;
			}
		}

		return false;
	}

	static {
		//Disable List
		DISABLE_LIST.add("projecte");
		DISABLE_LIST.add("randomthings");
		DISABLE_LIST.add("cyclicmagic");
		DISABLE_LIST.add("akashictome");
		DISABLE_LIST.add("integrateddynamics");
		DISABLE_LIST.add("notenoughwands");
		DISABLE_LIST.add("torcherino");
	}

}
