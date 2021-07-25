package baka943.realmtweaks.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class FontRGB extends FontRenderer {

	private static final Minecraft MINECRAFT = Minecraft.getMinecraft();
	private float hue, r, g, b;
	private boolean firstLine;
	public static FontRGB INSTANCE = new FontRGB();

	private FontRGB() {
		super(MINECRAFT.gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"), MINECRAFT.getTextureManager(), MINECRAFT.isUnicode());

		((IReloadableResourceManager) MINECRAFT.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public int drawStringWithShadow(@Nonnull String text, float x, float y, int color) {
		int length = super.drawStringWithShadow(text, x, y, color);
		this.firstLine = false;

		return length;
	}

	@Override
	public void drawSplitString(@Nonnull String str, int x, int y, int wrapWidth, int textColor) {
		super.drawSplitString(str, x, y, wrapWidth, textColor);
		this.firstLine = false;
	}

	public FontRGB init() {
		this.hue = Minecraft.getSystemTime() / 1920F;
		FontRenderer renderer = MINECRAFT.fontRenderer;
		this.setUnicodeFlag(renderer.getUnicodeFlag());
		this.setBidiFlag(renderer.getBidiFlag());

		this.firstLine = true;

		return this;
	}

	@Override
	protected void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;

		this.setHueColor();
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic) {
		this.advanceHue();
		this.setHueColor();

		return super.renderDefaultChar(ch, italic);
	}

	private void advanceHue() {
		this.hue += 1 / 16F;
	}

	private void setHueColor() {
		int rgb = Color.HSBtoRGB(this.hue, 1.0F, 1.0F);

		if(!this.firstLine) {
			super.setColor(this.r, this.g, this.b, 1.0F);
		} else {
			if(this.r == this.g && this.g == this.b) {
				super.setColor(
						this.r * ColorHelper.getR(rgb),
						this.g * ColorHelper.getG(rgb),
						this.b * ColorHelper.getB(rgb),
						1.0F);
			} else {
				super.setColor(
						r * 0.5F * (1.0F + ColorHelper.getR(rgb)),
						g * 0.5F * (1.0F + ColorHelper.getG(rgb)),
						b * 0.5F * (1.0F + ColorHelper.getB(rgb)),
						1.0F);
			}
		}
	}

	@Override
	protected float renderUnicodeChar(char ch, boolean italic) {
		this.advanceHue();
		this.setHueColor();

		return super.renderUnicodeChar(ch, italic);
	}

	public static class ColorHelper {

		public static float getR(int col) {
			return ((col & 0x00ff0000) >> 16) / 255.0F;
		}

		public static float getG(int col) {
			return ((col & 0x0000ff00) >> 8) / 255.0F;
		}

		public static float getB(int col) {
			return (col & 0x000000ff) / 255.0F;
		}

	}

}
