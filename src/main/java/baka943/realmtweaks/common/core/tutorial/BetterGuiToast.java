package baka943.realmtweaks.common.core.tutorial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;

public class BetterGuiToast extends GuiToast {

	private final TutorialDeque deque;

	public BetterGuiToast() {
		super(Minecraft.getMinecraft());
		this.deque = new TutorialDeque();
	}

}
