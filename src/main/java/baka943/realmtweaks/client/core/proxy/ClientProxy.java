package baka943.realmtweaks.client.core.proxy;

import baka943.realmtweaks.client.render.tile.RenderTileBetweenAltar;
import baka943.realmtweaks.common.block.tile.TileBetweenAltar;
import baka943.realmtweaks.common.core.proxy.IProxy;
import baka943.realmtweaks.common.entity.ModEntites;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModEntites.initModels();

		ClientRegistry.bindTileEntitySpecialRenderer(TileBetweenAltar.class, new RenderTileBetweenAltar());
	}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {}

}
