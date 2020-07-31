package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.RealmTweaks;
import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import java.util.Iterator;

public class BotaniaTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BotaniaTweaks.class);

		disableBotaniaFunctionalFlower("orechid");
		disableBotaniaFunctionalFlower("orechidIgnem");

		BotaniaAPI.registerPureDaisyRecipe(Blocks.MAGMA, Blocks.LAVA.getDefaultState());

		if(RealmTweaks.isBetweenlandsLoaded) {
			BotaniaAPI.registerPureDaisyRecipe(BlockRegistry.SWAMP_WATER, BlockRegistry.BLACK_ICE.getDefaultState());

			disablePureDaisyRecipe(ModBlocks.livingrock.getDefaultState());
			disablePureDaisyRecipe(ModBlocks.livingwood.getDefaultState());
			BotaniaAPI.registerPureDaisyRecipe("logWeedwood", ModBlocks.livingwood.getDefaultState(), 0);
			BotaniaAPI.registerPureDaisyRecipe(BlockRegistry.BETWEENSTONE, ModBlocks.livingrock.getDefaultState(), 0);

			BotaniaAPI.oreWeights.clear();
			BotaniaAPI.oreWeightsNether.clear();
		}
	}

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

		for(int i = 0; i < invCount; i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if(!stack.isEmpty()) {
				int count = stack.getCount();
				ItemStack orechid = ItemBlockSpecialFlower.ofType("orechid");
					orechid.setCount(count);
				ItemStack orechidIgnem = ItemBlockSpecialFlower.ofType("orechidIgnem");
					orechidIgnem.setCount(count);

				if(ItemStack.areItemStacksEqual(stack, orechid) || ItemStack.areItemStacksEqual(stack, orechidIgnem)) {
					inventory.setInventorySlotContents(i, new ItemStack(ModItems.FOREST_BAT));

					player.sendMessage(new TextComponentTranslation("chat." + LibMisc.MOD_ID + ".mod_disable"));
				}
			}
		}
	}

	private static void disableBotaniaFunctionalFlower(String name) {
		Iterator<LexiconEntry> it = BotaniaAPI.getAllEntries().iterator();

		while(it.hasNext()) {
			if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
				it.remove();

				break;
			}
		}

		it = BotaniaAPI.categoryFunctionalFlowers.entries.iterator();

		while(it.hasNext()) {
			if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
				it.remove();

				break;
			}
		}

		Iterator<RecipePetals> it2 = BotaniaAPI.petalRecipes.iterator();
		ItemStack stack = ItemBlockSpecialFlower.ofType(name);

		while(it2.hasNext()) {
			ItemStack output = it2.next().getOutput();

			if(ItemStack.areItemStacksEqual(stack, output)) {
				it2.remove();

				break;
			}
		}
	}

    private static void disableBotaniaGenerationFlower(String name) {
		Iterator<LexiconEntry> it = BotaniaAPI.getAllEntries().iterator();

		while(it.hasNext()) {
			if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
				it.remove();

				break;
			}
		}

		it = BotaniaAPI.categoryGenerationFlowers.entries.iterator();

		while(it.hasNext()) {
			if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
				it.remove();

				break;
			}
		}

		Iterator<RecipePetals> it2 = BotaniaAPI.petalRecipes.iterator();
		ItemStack stack = ItemBlockSpecialFlower.ofType(name);

		while(it2.hasNext()) {
			ItemStack output = it2.next().getOutput();

			if(ItemStack.areItemStacksEqual(stack, output)) {
				it2.remove();

				break;
			}
		}
	}

	private static void disablePureDaisyRecipe(IBlockState output) {
		Iterator<RecipePureDaisy> iterator = BotaniaAPI.pureDaisyRecipes.iterator();

		while(iterator.hasNext()) {
			IBlockState state = iterator.next().getOutputState();

			if(state == output) {
				iterator.remove();

				break;
			}
		}
	}

}
