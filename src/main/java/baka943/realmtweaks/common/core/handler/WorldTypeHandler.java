package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.world.WorldTypeAtlantis;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTypeHandler {

	private final WorldType atlantis = new WorldTypeAtlantis().enableInfoNotice();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void createWorldGui(InitGuiEvent.Pre event) {
		GuiScreen screen = event.getGui();

		if(screen instanceof GuiCreateWorld) {
			GuiCreateWorld createWorld = (GuiCreateWorld) screen;

			if(createWorld.selectedIndex == WorldType.DEFAULT.getId()) {
				createWorld.selectedIndex = atlantis.getId();
			}
		}
	}

}
