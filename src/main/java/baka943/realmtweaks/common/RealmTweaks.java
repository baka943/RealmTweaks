package baka943.realmtweaks.common;

import baka943.realmtweaks.common.core.handler.*;
import baka943.realmtweaks.common.core.proxy.IProxy;
import baka943.realmtweaks.common.core.tutorial.BetterGuiToast;
import baka943.realmtweaks.common.entity.ModEntites;
import baka943.realmtweaks.common.fluid.ModFluids;
import baka943.realmtweaks.common.lib.LibMisc;
import baka943.realmtweaks.common.world.gen.feature.WorldGenEntityMob;
import baka943.realmtweaks.common.world.gen.structure.WorldGenSwampCircle;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class RealmTweaks {

	@Mod.Instance
	public static RealmTweaks instance;

	@SidedProxy(serverSide = LibMisc.PROXY_SERVER, clientSide = LibMisc.PROXY_CLIENT)
	private static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new WorldTypeHandler());
		MinecraftForge.EVENT_BUS.register(new BetterGuiToast());
		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenHandler());
		MinecraftForge.EVENT_BUS.register(new MobHandler());

		ModEntites.init();
		ModFluids.registerFluids();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ItemUseHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		GameRegistry.registerWorldGenerator(new WorldGenSwampCircle(), 0);

		WorldGenEntityMob.init();
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OreDictHandler.registerOreDict();
		proxy.postInit(event);
	}

}
