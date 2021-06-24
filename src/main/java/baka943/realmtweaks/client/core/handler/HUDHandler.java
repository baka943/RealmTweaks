package baka943.realmtweaks.client.core.handler;

import baka943.realmtweaks.common.block.tile.TileBetweenAltar;
import baka943.realmtweaks.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.common.core.helper.PlayerHelper;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public class HUDHandler {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft MC = Minecraft.getMinecraft();

		if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			RayTraceResult ray = MC.objectMouseOver;

			if(ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
				TileEntity tile = MC.world.getTileEntity(ray.getBlockPos());

				if(!PlayerHelper.hasHeldItemClass(MC.player, ILexicon.class) && tile instanceof TileBetweenAltar) {
					((TileBetweenAltar)tile).renderHUD(MC, event.getResolution());
				}
			}
		}
	}

}
