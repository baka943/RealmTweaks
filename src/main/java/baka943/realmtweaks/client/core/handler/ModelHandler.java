package baka943.realmtweaks.client.core.handler;

import baka943.realmtweaks.client.render.IModelRegister;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Locale;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class ModelHandler {

	private ModelHandler() {}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		OBJLoader.INSTANCE.addDomain(LibMisc.MOD_ID.toLowerCase(Locale.ROOT));

		for(Block block : Block.REGISTRY) {
			if(block instanceof IModelRegister) {
				((IModelRegister) block).registerModels();
			}
		}

		for(Item item : Item.REGISTRY) {
			if(item instanceof IModelRegister) {
				((IModelRegister) item).registerModels();
			}
		}
	}

	public static void registerInventoryVariant(Block block) {
		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(block),
				0,
				new ModelResourceLocation(block.getRegistryName(), "inventory")
		);
	}

}
