package baka943.realmtweaks.client.render;

import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.block.tile.TileBetweenAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thebetweenlands.common.registries.FluidRegistry;
import vazkii.botania.client.core.handler.ClientTickHandler;

import javax.annotation.Nonnull;

public class RenderTileBetweenAltar extends TileEntitySpecialRenderer<TileBetweenAltar> {

	@Override
	public void render(@Nonnull TileBetweenAltar altar, double d0, double d1, double d2, float pticks, int digProgress, float unused) {
		if(!altar.getWorld().isBlockLoaded(altar.getPos(), false)
				|| altar.getWorld().getBlockState(altar.getPos()).getBlock() != ModBlocks.ALTAR)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);

		GlStateManager.translate(d0 + 0.5, d1 + 1.5, d2 + 0.5);
		GlStateManager.enableRescaleNormal();

		if(altar.hasWater()) {
			GlStateManager.pushMatrix();
			float s = 1F / 256F * 10F;
			float v = 1F / 8F;
			float w = -v * 2.5F;

			int petals = 0;
			for(int i = 0; i < altar.getSizeInventory(); i++)
				if(!altar.getItemHandler().getStackInSlot(i).isEmpty())
					petals++;
				else break;

			if(petals > 0) {
				final float modifier = 6F;
				final float rotationModifier = 0.25F;
				final float radiusBase = 1.2F;
				final float radiusMod = 0.1F;

				double ticks = (ClientTickHandler.ticksInGame + pticks) * 0.5;
				float offsetPerPetal = 360 / petals;

				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.05F, -0.5F, 0F);
				GlStateManager.scale(v, v, v);
				for(int i = 0; i < petals; i++) {
					float offset = offsetPerPetal * i;
					float deg = (int) (ticks / rotationModifier % 360F + offset);
					float rad = deg * (float) Math.PI / 180F;
					float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
					float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
					float x =  (float) (radiusX * Math.cos(rad));
					float z = (float) (radiusZ * Math.sin(rad));
					float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

					GlStateManager.pushMatrix();
					GlStateManager.translate(x, y, z);
					float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
					float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
					float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

					v /= 2F;
					GlStateManager.translate(v, v, v);
					GlStateManager.rotate(deg, xRotate, yRotate, zRotate);
					GlStateManager.translate(-v, -v, -v);
					v *= 2F;

					GlStateManager.color(1F, 1F, 1F, 1F);

					ItemStack stack = altar.getItemHandler().getStackInSlot(i);
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
					GlStateManager.popMatrix();
				}

				GlStateManager.popMatrix();
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlpha();

			GlStateManager.color(1F, 1F, 1F, 0.7F);
			GlStateManager.translate(w, -0.3F, w);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.scale(s, s, s);

			renderIcon(0, 0, FluidRegistry.SWAMP_WATER.getStill(), 16, 16, -1);

			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

	public void renderIcon(int par1, int par2, ResourceLocation loc, int par4, int par5, int brightness) {
		TextureAtlasSprite par3Icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(loc.toString());
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		tessellator.getBuffer().pos(par1, par2 + par5, 0).tex(par3Icon.getMinU(), par3Icon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + par5, 0).tex(par3Icon.getMaxU(), par3Icon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2, 0).tex(par3Icon.getMaxU(), par3Icon.getMinV()).endVertex();
		tessellator.getBuffer().pos(par1, par2, 0).tex(par3Icon.getMinU(), par3Icon.getMinV()).endVertex();
		tessellator.draw();
	}

}
