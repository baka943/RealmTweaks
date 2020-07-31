package baka943.realmtweaks.common.integrations;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import baka943.realmtweaks.common.block.ModBlocks;
import baka943.realmtweaks.common.item.ModItems;
import baka943.realmtweaks.common.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenSpikes.EndSpike;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class BloodMagicTweaks {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(BloodMagicTweaks.class);
	}

	@SubscribeEvent
	public static void willDrop(LivingDropsEvent event) {
		EntityLivingBase attackedEntity = event.getEntityLiving();
		DamageSource source = event.getSource();
		Entity entity = source.getTrueSource();
		World world = attackedEntity.getEntityWorld();
		Random rand = world.rand;

		if(entity != null && !entity.getEntityWorld().isRemote && entity instanceof EntityPlayer) {
			if(world.provider.getDimension() == Utils.getDimId("the_nether") && attackedEntity instanceof EntityMob) {
				double souls = rand.nextDouble() * 20;
				ItemStack stack = ((IDemonWill)RegistrarBloodMagicItems.MONSTER_SOUL).createWill(0, souls);

				if(rand.nextInt(3) == 0) {
					event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, stack));
				}
			}
		}
	}

	@SubscribeEvent
	public static void lifeDrop(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;

		if(event.phase == TickEvent.Phase.START) {
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(network.getOrbTier() > 0 && !world.isRemote) {
				int amount = (int)Math.ceil(network.getCurrentEssence() * 0.01);

				amount *= network.getOrbTier();

				if(world.getTotalWorldTime() % 30 == 0) {
					network.syphonAndDamage(network.getPlayer(), new SoulTicket(Math.min(amount, 2500)));

					if(network.getCurrentEssence() == 0) {
						float syphon = 1.0F;

						if(player.getHealth() == 1.0F) {
							syphon = 0.0F;
						}

						network.hurtPlayer(player, syphon);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerDrops(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		Random rand = new Random();

		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			if(!player.world.isRemote && network.getOrbTier() == 0) {
				if(source.getTrueSource() instanceof EntityEnderman && rand.nextInt(10) == 0) {
					EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(ModItems.BLOOD_TEAR));

					player.world.spawnEntity(item);
					item.setDefaultPickupDelay();
				}

				float currentHealth = player.getHealth();
				float maxHealth = player.getMaxHealth();
				float damage = event.getAmount();

				if(currentHealth < maxHealth) {
					event.setAmount(Math.max(1.0F, damage * (currentHealth / maxHealth)));
				}
			}
		}
	}

	@SubscribeEvent
	public static void itemDecay(ItemExpireEvent event) {
		if(!event.getEntityItem().getItem().isEmpty()) {
			Item item = event.getEntityItem().getItem().getItem();
			BlockPos pos = event.getEntityItem().getPosition();
			Block block = event.getEntityItem().getEntityWorld().getBlockState(pos.down()).getBlock();

			if(item == ModItems.BLOOD_TEAR && block == Blocks.END_STONE) {
				event.getEntityItem().getEntityWorld().setBlockState(pos.down(), ModBlocks.BLOODED_ENDSTONE.getDefaultState());
			}
		}
	}

	@SubscribeEvent
	public static void itemToss(ItemTossEvent event) {
		if(!event.getEntityItem().getItem().isEmpty()) {
			Item item = event.getEntityItem().getItem().getItem();

			if(item == ModItems.BLOOD_TEAR) {
				event.getEntityItem().lifespan = 100;
			}
		}
	}

	@SubscribeEvent
	public static void mobControl(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if(!world.isRemote) {
			if(!(world.provider instanceof WorldProviderEnd)
					&& entity instanceof EntityEnderman) {
				entity.setDead();
				event.setCanceled(true);
			}

			if(world.provider instanceof WorldProviderEnd) {
				WorldProviderEnd provider = (WorldProviderEnd)world.provider;
				DragonFightManager manager = provider.getDragonFightManager();

				if(entity.getClass().equals(EntityDragon.class) && !manager.hasPreviouslyKilledDragon()) {
					((EntityDragon)entity).setHealth(0.0F);
					manager.processDragonDeath((EntityDragon)entity);

					EndSpike[] spikes = BiomeEndDecorator.getSpikesForWorld(world);

					for(EndSpike spike : spikes) {
						world.getEntitiesWithinAABB(EntityEnderCrystal.class, spike.getTopBoundingBox()).forEach(Entity::setDead);
					}
				}
			}
		}
	}

}
