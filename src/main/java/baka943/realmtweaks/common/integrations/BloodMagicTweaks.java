package baka943.realmtweaks.common.integrations;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import baka943.realmtweaks.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class BloodMagicTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BloodMagicTweaks.class);
	}

	@SubscribeEvent
	public static void willDrop(LivingDropsEvent event) {
		EntityLivingBase attackedEntity = event.getEntityLiving();
		Entity entity = event.getSource().getTrueSource();
		Random rand = new Random();

		if(entity != null && !entity.getEntityWorld().isRemote && entity instanceof EntityPlayer) {
			double souls = rand.nextDouble() * 20;
			ItemStack stack = ((IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL).createWill(0, souls);

			if(attackedEntity instanceof EntityEnderman && rand.nextInt(5) == 0) {
				event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, stack));
			}
		}
	}

	@SubscribeEvent
	public static void playerDrops(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		Random rand = new Random();

		if(event.getEntity() instanceof EntityPlayer && !(event.getEntity() instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(!player.world.isRemote && network.getOrbTier() == 0
					&& source.getTrueSource() instanceof EntityEnderman && rand.nextInt(5) == 0) {
				player.dropItem(new ItemStack(ModItems.BLOOD_TEAR), false);
			}
		}
	}

	@SubscribeEvent
	public static void mobControl(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote) {
			if(world.provider instanceof WorldProviderEnd) {
				DragonFightManager manager = ((WorldProviderEnd) world.provider).getDragonFightManager();

				if(entity.getClass().equals(EntityDragon.class) && !manager.hasPreviouslyKilledDragon()) {
					((EntityDragon) entity).setHealth(0.0F);
					manager.processDragonDeath((EntityDragon) entity);
				}
			} else if(entity instanceof EntityEnderman) {
				event.setCanceled(true);
				entity.setDead();
			}
		}
	}

	@SubscribeEvent
	public static void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event) {
		Block block = event.getState().getBlock();

		if(block.getRegistryName().toString().contains("realmtweaks:ender_essence")
				&& event.getWorld().provider instanceof WorldProviderEnd) {
			event.setResult(Event.Result.ALLOW);
		}
	}

}
