package baka943.realmtweaks.common.core.handler;

import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.advancements.CriteriaTriggers;
import thebetweenlands.common.advancments.NoCriteriaTrigger;

public class CriterionHandler {

	public static final NoCriteriaTrigger REALM_ALTERNATE = CriteriaTriggers.register(new NoCriteriaTrigger(Utils.getRL("realm_alternate")));
	public static final NoCriteriaTrigger REALM_ANOTHER = CriteriaTriggers.register(new NoCriteriaTrigger(Utils.getRL("realm_another")));
	public static final NoCriteriaTrigger REALM_NIGHTMARE = CriteriaTriggers.register(new NoCriteriaTrigger(Utils.getRL("realm_nightmare")));
	public static final NoCriteriaTrigger REALM_KEY = CriteriaTriggers.register(new NoCriteriaTrigger(Utils.getRL("realm_key")));

	public static void preInit() {}

}
