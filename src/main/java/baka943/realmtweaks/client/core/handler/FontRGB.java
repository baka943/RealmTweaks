package baka943.realmtweaks.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class FontRGB extends FontRenderer {

	private static final Minecraft mc = Minecraft.getMinecraft();
	private float hue;
	private float r;
	private float g;
	private float b;
	private float a;
	private boolean firstLine;
	public static FontRGB INSTANCE = new FontRGB();

	private FontRGB() {
		super(mc.gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());

		((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public int drawStringWithShadow(@Nonnull String text, float x, float y, int color) {
		int length = super.drawStringWithShadow(text, x, y, color);
		firstLine = false;

		return length;
	}

	@Override
	public void drawSplitString(@Nonnull String str, int x, int y, int wrapWidth, int textColor) {
		super.drawSplitString(str, x, y, wrapWidth, textColor);
		firstLine = false;
	}

	public FontRGB init() {
		hue = ((int)(Minecraft.getSystemTime() / 80) / 16F);
		FontRenderer renderer = mc.fontRenderer;
		setUnicodeFlag(renderer.getUnicodeFlag());
		setBidiFlag(renderer.getBidiFlag());

		firstLine = true;

		return this;
	}

	@Override
	protected void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;

		setHueColor();
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic) {
		advanceHue();
		setHueColor();

		return super.renderDefaultChar(ch, italic);
	}

	private void advanceHue() {
		hue += 1 / 16F;
	}

	private void setHueColor() {
		int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);

		if(!firstLine) {
			super.setColor(r, g, b, a);
		} else {
			if(r == g && g == b) {
				super.setColor(
						r * ColorHelper.getR(rgb),
						g * ColorHelper.getG(rgb),
						b * ColorHelper.getB(rgb),
						a);
			} else {
				super.setColor(
						r * 0.5F * (1.0F + ColorHelper.getR(rgb)),
						g * 0.5F * (1.0F + ColorHelper.getG(rgb)),
						b * 0.5F * (1.0F + ColorHelper.getB(rgb)),
						a);
			}
		}
	}

	@Override
	protected float renderUnicodeChar(char ch, boolean italic) {
		advanceHue();
		setHueColor();

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
