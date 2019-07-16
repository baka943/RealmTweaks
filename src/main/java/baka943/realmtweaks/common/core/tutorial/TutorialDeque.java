package baka943.realmtweaks.common.core.tutorial;

import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.tutorial.TutorialSteps;

import java.util.ArrayDeque;

public class TutorialDeque extends ArrayDeque<IToast> {

	public static boolean isBlocked(IToast toast) {
		if(toast instanceof TutorialToast) {
			return toast.getType() != TutorialSteps.MOVEMENT;
		} else return false;
	}

}
