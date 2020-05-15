package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModItems {

	public static final Item ENDER_SHARD = new ItemMod("ender_shard");
	public static final Item FOREST_BAT = new ItemMod("forest_bat");
	public static final ItemPaperTool PAPER_TOOL = new ItemPaperTool("paper_tool");
	public static final ItemPaperTool PARCHMENT_TOOL = new ItemPaperTool("parchment_tool");
	public static final ItemPaperTool TOME_TOOL = new ItemPaperTool("tome_tool");
	public static final ItemPaperTool DEBUG_TOOL = new ItemPaperTool("debug_tool");
	public static final Item ADVANCEMENT_BOOK = new ItemAdvancementBook();
	public static final Item BLOOD_TEAR = new ItemBloodTear();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(ENDER_SHARD);
		registry.register(FOREST_BAT);
		registry.register(PAPER_TOOL);
		registry.register(PARCHMENT_TOOL);
		registry.register(TOME_TOOL);
		registry.register(DEBUG_TOOL);
		registry.register(ADVANCEMENT_BOOK);
		registry.register(BLOOD_TEAR);
	}

}
