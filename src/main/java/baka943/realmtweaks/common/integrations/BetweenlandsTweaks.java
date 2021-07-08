package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.entity.EntityBoneHook;
import baka943.realmtweaks.common.item.ItemOctineFlintstones;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static thebetweenlands.common.handler.OverworldItemHandler.FIRE_TOOL_WHITELIST;

public class BetweenlandsTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BetweenlandsTweaks.class);

		FIRE_TOOL_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemOctineFlintstones);
	}

	@SubscribeEvent
	public static void boneFishHook(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote
				&& world.provider.getDimension() == Utils.getDimId("betweenlands")) {
			if(entity instanceof EntityFishHook
					&& entity.getClass().equals(EntityFishHook.class)) {
				EntityPlayer player = ((EntityFishHook) entity).getAngler();
				ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

				if(stack.getItem() != Items.FISHING_ROD) {
					stack = player.getHeldItem(EnumHand.OFF_HAND);
				}

				entity.setDead();
				event.setCanceled(true);

				EntityBoneHook hook = new EntityBoneHook(world, player);

				int speed = EnchantmentHelper.getFishingSpeedBonus(stack);
				int luck = EnchantmentHelper.getFishingLuckBonus(stack);

				if(speed > 0) hook.setLureSpeed(speed);
				if(luck > 0) hook.setLuck(luck);

				world.spawnEntity(hook);
			}
		}
	}

}
