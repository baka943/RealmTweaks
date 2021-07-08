package baka943.realmtweaks.common.integrations;

import baka943.realmtweaks.common.entity.EntityBoneHook;
import baka943.realmtweaks.common.item.ItemOctineFlintstones;
import baka943.realmtweaks.common.lib.Utils;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static thebetweenlands.common.handler.OverworldItemHandler.*;

public class BetweenlandsTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BetweenlandsTweaks.class);

		FIRE_TOOL_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemOctineFlintstones);
		TORCH_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
		ROTTING_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemFood && (Utils.getModId(stack).equals("roots") || Utils.getModId(stack).equals("mysticalworld")));
		TOOL_WHITELIST.put(Utils.getRL("default_whitelist"), stack -> stack.getItem() instanceof ItemDruidKnife);
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
