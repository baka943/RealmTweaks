package baka943.realmtweaks.common;

import baka943.realmtweaks.common.core.handler.MapGenHandler;
import baka943.realmtweaks.common.core.handler.OreDictHandler;
import baka943.realmtweaks.common.core.handler.WorldTypeHandler;
import baka943.realmtweaks.common.core.proxy.IProxy;
import baka943.realmtweaks.common.entity.ModEntites;
import baka943.realmtweaks.common.fluid.ModFluids;
import baka943.realmtweaks.common.integrations.*;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.world.gen.feature.WorldGenEntityMob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class RealmTweaks {

	public static boolean BMLoaded = false;
	public static boolean BTLoaded = false;

	@Mod.Instance
	public static RealmTweaks instance;

	@SidedProxy(serverSide = LibMisc.PROXY_SERVER, clientSide = LibMisc.PROXY_CLIENT)
	private static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		BMLoaded = Loader.isModLoaded("bloodmagic");
		BTLoaded = Loader.isModLoaded("thebetweenlands");

		MinecraftForge.EVENT_BUS.register(new WorldTypeHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenHandler());

		ModEntites.init();

		if(BMLoaded) ModFluids.registerFluids();

		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		WorldGenEntityMob.init();

		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OreDictHandler.registerOreDict();

		if(BTLoaded) {
			BetweenlandsTweaks.init();
			BotaniaTweaks.init();
			RootsTweaks.init();
		}

		TinkersTweaks.init();

		if(BMLoaded) BloodMagicTweaks.init();
	}

}
