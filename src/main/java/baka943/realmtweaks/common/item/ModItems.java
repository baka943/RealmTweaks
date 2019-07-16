package baka943.realmtweaks.common.item;

import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import thebetweenlands.common.capability.circlegem.CircleGemType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModItems {

	public static final Item ENDER_SHARD = new ItemMod("ender_shard");
	public static final Item REALM_KEY = new ItemRealmKey();
	public static final Item CRYSTAL_AQUA = new ItemRealmCrystal(CircleGemType.AQUA);
	public static final Item CRYSTAL_CRIMSON = new ItemRealmCrystal(CircleGemType.CRIMSON);
	public static final Item CRYSTAL_GREEN = new ItemRealmCrystal(CircleGemType.GREEN);
	public static final ItemPaperTool PAPER_TOOL = new ItemPaperTool();

	public static final Item ADVANCEMENT_BOOK = new ItemMod("advancement_book") {

		@Nonnull
		@SideOnly(Side.CLIENT)
		@Override
		public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
			if(worldIn.isRemote) {
				Minecraft minecraft = Minecraft.getMinecraft();
				EntityPlayerSP playerSP = minecraft.player;

				minecraft.displayGuiScreen(new GuiScreenAdvancements(playerSP.connection.getAdvancementManager()));
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		}

	};

	public static final Item FORESTRY_BAT = new ItemMod("forestry_bat") {

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
			list.add("“新人，请说出常用模组”");
		}

	};

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(ENDER_SHARD);
		registry.register(REALM_KEY);
		registry.register(CRYSTAL_AQUA);
		registry.register(CRYSTAL_CRIMSON);
		registry.register(CRYSTAL_GREEN);
		registry.register(PAPER_TOOL);
		registry.register(ADVANCEMENT_BOOK);
		registry.register(FORESTRY_BAT);
	}

}
