package baka943.realmtweaks.common;

import baka943.realmtweaks.common.core.handler.*;
import baka943.realmtweaks.common.core.proxy.IProxy;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.world.gen.feature.WorldGenEntityMob;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import java.util.Iterator;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class RealmTweaks {

	@Mod.Instance
	public static RealmTweaks instance;

	@SidedProxy(serverSide = LibMisc.PROXY_SERVER, clientSide = LibMisc.PROXY_CLIENT)
	private static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new WorldTypeHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenHandler());
		MinecraftForge.ORE_GEN_BUS.register(new OreGenHandler());
		MinecraftForge.EVENT_BUS.register(new MobHandler());

		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ItemUseHandler());
		MinecraftForge.EVENT_BUS.register(new StartItemHandler());
		MinecraftForge.EVENT_BUS.register(new PortalHandler());

		WorldGenEntityMob.init();

		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OreDictHandler.registerOreDict();

		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void advancementHandler(FMLServerStartedEvent event) throws NoSuchFieldException, IllegalAccessException {
		Iterator iterator = AdvancementManager.ADVANCEMENT_LIST.getAdvancements().iterator();

		while(iterator.hasNext()) {
			Advancement advancement = (Advancement) iterator.next();
			String modid = advancement.getId().getNamespace();

			if(!modid.matches("realmtweaks") && !modid.matches("tombstone")) {
				iterator.remove();
			}
		}
	}

}
