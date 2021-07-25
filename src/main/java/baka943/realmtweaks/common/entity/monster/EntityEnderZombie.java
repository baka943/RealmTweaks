package baka943.realmtweaks.common.entity.monster;

import baka943.realmtweaks.common.item.ModItems;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityEnderZombie extends EntityEnderman {

	private static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (new RangedAttribute(null, "ender_zombie.spawnReinforcements", 0.1D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");

	public EntityEnderZombie(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		this.stepHeight = 0.6F;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityEnderZombie.AIFindPlayer(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>() {
			public boolean apply(@Nullable EntityEndermite endermite) {
				return endermite.isSpawnedByPlayer();
			}
		}));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * ForgeModContainer.zombieSummonBaseChance);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);

		if(this.rand.nextFloat() < difficulty.getClampedAdditionalDifficulty() * 0.05F) {
			this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
		}
		return livingdata;
	}

	private boolean shouldAttackPlayer(EntityPlayer player) {
		ItemStack itemstack = player.inventory.armorInventory.get(3);

		if(itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
			return false;
		} else {
			Vec3d vec3d = player.getLook(1.0F).normalize();
			Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double)this.getEyeHeight() - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
			double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			double d1 = vec3d.dotProduct(vec3d1);

			return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
		}
	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}

	@Nonnull
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	@Override
	protected void playStepSound(@Nonnull BlockPos pos, @Nonnull Block blockIn) {
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);

		if(this.rand.nextInt(100) == 0) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.ENDER_PEARL));
		} else {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.ENDER_SHARD));
		}
	}

	static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

		private final EntityEnderZombie enderZombie;
		private EntityPlayer player;
		private int aggroTime;
		private int teleportTime;

		public AIFindPlayer(EntityEnderZombie enderZombie) {
			super(enderZombie, EntityPlayer.class, false);
			this.enderZombie = enderZombie;
		}

		@Override
		public boolean shouldExecute() {
			double distance = this.getTargetDistance();

			this.player = this.enderZombie.world.getNearestAttackablePlayer(this.enderZombie.posX, this.enderZombie.posY, this.enderZombie.posZ, distance, distance, null, new Predicate<EntityPlayer>() {
				@Override
				public boolean apply(@Nullable EntityPlayer player) {
					return player != null && AIFindPlayer.this.enderZombie.shouldAttackPlayer(player);
				}
			});

			return this.player != null;
		}

		@Override
		public void startExecuting() {
			this.aggroTime = 5;
			this.teleportTime = 0;
		}

		@Override
		public void resetTask() {
			this.player = null;
			super.resetTask();
		}

		@Override
		public boolean shouldContinueExecuting() {
			if(this.player != null) {
				if(!this.enderZombie.shouldAttackPlayer(this.player)) {
					return false;
				} else {
					this.enderZombie.faceEntity(this.player, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.targetEntity != null && this.targetEntity.isEntityAlive() ? true : super.shouldContinueExecuting();
			}
		}

		@Override
		public void updateTask() {
			if(this.player != null) {
				if(--this.aggroTime <= 0) {
					this.targetEntity = this.player;
					this.player = null;
					super.startExecuting();
				}
			} else {
				if(this.targetEntity != null) {
					if(this.enderZombie.shouldAttackPlayer(this.targetEntity)) {
						if(this.targetEntity.getDistanceSq(this.enderZombie) < 16.0D) {
							this.enderZombie.teleportRandomly();
						}

						this.teleportTime = 0;
					} else if (this.targetEntity.getDistanceSq(this.enderZombie) > 256.0D && this.teleportTime++ >= 30 && this.enderZombie.teleportToEntity(this.targetEntity)) {
						this.teleportTime = 0;
					}
				}

				super.updateTask();
			}
		}
	}

}
