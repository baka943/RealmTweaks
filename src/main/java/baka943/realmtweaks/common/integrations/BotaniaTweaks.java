package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.lib.LibMisc;
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
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import java.util.Iterator;

public class BotaniaTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BotaniaTweaks.class);

		disableBotaniaFunctionalFlower("entropinnyum");

		BotaniaAPI.registerPureDaisyRecipe(Blocks.MAGMA, Blocks.LAVA.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(BlockRegistry.SWAMP_WATER, BlockRegistry.BLACK_ICE.getDefaultState());
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
				ItemStack e0 = ItemBlockSpecialFlower.ofType("entropinnyum");
					e0.setCount(count);
				ItemStack e1 = ItemBlockFloatingSpecialFlower.ofType("entropinnyum");
					e1.setCount(count);

				if(ItemStack.areItemStacksEqual(stack, e0) || ItemStack.areItemStacksEqual(stack, e1)) {
					inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					player.sendMessage(new TextComponentTranslation("chat." + LibMisc.MOD_ID + ".entropinnyum_disable"));
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

}
