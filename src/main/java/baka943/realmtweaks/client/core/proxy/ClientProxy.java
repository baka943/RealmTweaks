package baka943.realmtweaks.client.core.proxy;

import baka943.realmtweaks.client.render.entity.RenderSwampSpider;
import baka943.realmtweaks.common.core.proxy.IProxy;
import baka943.realmtweaks.common.entity.monster.EntitySwampSpider;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		initRenderers();
	}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {}

	private void initRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySwampSpider.class, RenderSwampSpider::new);
	}

}
