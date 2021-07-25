package baka943.realmtweaks.common.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityEnderSkeleton extends EntityEnderman implements IRangedAttackMob {

	private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityEnderSkeleton.class, DataSerializers.BOOLEAN);
	private final EntityAIAttackRangedBow<EntityEnderSkeleton> aiArrowAttack = new EntityAIAttackRangedBow<>(this, 1.0D, 20, 15.0F);

	private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) {
		@Override
		public void resetTask() {
			super.resetTask();
			EntityEnderSkeleton.this.setSwingingArms(false);
		}

		@Override
		public void startExecuting() {
			super.startExecuting();
			EntityEnderSkeleton.this.setSwingingArms(true);
		}
	};

	public EntityEnderSkeleton(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.99F);
		this.stepHeight = 0.6F;
		this.setCombatTask();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityEnderSkeleton.AIFindPlayer(this));
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
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SWINGING_ARMS, Boolean.FALSE);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		this.setCombatTask();
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

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
		return SoundEvents.ENTITY_SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	@Override
	protected void playStepSound(@Nonnull BlockPos pos, @Nonnull Block blockIn) {
		this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	}

	@Override
	public void setItemStackToSlot(@Nonnull EntityEquipmentSlot slotIn, @Nonnull ItemStack stack) {
		super.setItemStackToSlot(slotIn, stack);

		if(!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
			this.setCombatTask();
		}
	}

	public void setCombatTask() {
		if(this.world != null & !this.world.isRemote) {
			this.tasks.removeTask(this.aiAttackOnCollide);
			this.tasks.removeTask(this.aiArrowAttack);
			
			ItemStack stack = this.getHeldItemMainhand();

			if(stack.getItem() == Items.BOW) {
				int i = 20;

				if(this.world.getDifficulty() != EnumDifficulty.HARD) {
					i = 40;
				}
				
				this.aiArrowAttack.setAttackCooldown(i);
				this.tasks.addTask(4, this.aiArrowAttack);
			} else {
				this.tasks.addTask(4, this.aiAttackOnCollide);
			}
		}
	}
	
	@Override
	public double getYOffset() {
		return -0.6D;
	}

	@Override
	public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
		EntityArrow entityArrow = this.getArrow(distanceFactor);
		double dX = target.posX - this.posX;
		double dY = target.getEntityBoundingBox().minY + target.height / 3.0F - entityArrow.posY;
		double dZ = target.posZ - this.posZ;
		double dH = MathHelper.sqrt(dX * dX + dZ * dZ);

		entityArrow.shoot(dX, dY + dH * 0.20000000298023224D, dZ, 1.6F, 14 - this.world.getDifficulty().getId() * 4);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityArrow);
	}

	private EntityArrow getArrow(float distanceFactor) {
		ItemStack stack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

		if(stack.getItem() == Items.SPECTRAL_ARROW) {
			EntitySpectralArrow entitySpectralArrow = new EntitySpectralArrow(this.world, this);
			entitySpectralArrow.setEnchantmentEffectsFromEntity(this, distanceFactor);

			return entitySpectralArrow;
		} else {
			EntityArrow entityArrow = new EntityTippedArrow(this.world, this);
			entityArrow.setEnchantmentEffectsFromEntity(this, distanceFactor);

			if(stack.getItem() == Items.TIPPED_ARROW && entityArrow instanceof EntityTippedArrow) {
				((EntityTippedArrow)entityArrow).setPotionEffect(stack);
			}

			return entityArrow;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isSwingingArms() {
		return this.dataManager.get(SWINGING_ARMS);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		this.dataManager.set(SWINGING_ARMS, swingingArms);
	}

	static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

		private final EntityEnderSkeleton enderSkeleton;
		private EntityPlayer player;
		private int aggroTime;
		private int teleportTime;

		public AIFindPlayer(EntityEnderSkeleton enderSkeleton) {
			super(enderSkeleton, EntityPlayer.class, false);
			this.enderSkeleton = enderSkeleton;
		}

		@Override
		public boolean shouldExecute() {
			double distance = this.getTargetDistance();

			this.player = this.enderSkeleton.world.getNearestAttackablePlayer(this.enderSkeleton.posX, this.enderSkeleton.posY, this.enderSkeleton.posZ, distance, distance, null, new Predicate<EntityPlayer>() {
				@Override
				public boolean apply(@Nullable EntityPlayer player) {
					return player != null && EntityEnderSkeleton.AIFindPlayer.this.enderSkeleton.shouldAttackPlayer(player);
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
				if(!this.enderSkeleton.shouldAttackPlayer(this.player)) {
					return false;
				} else {
					this.enderSkeleton.faceEntity(this.player, 10.0F, 10.0F);
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
					if(this.enderSkeleton.shouldAttackPlayer(this.targetEntity)) {
						if(this.targetEntity.getDistanceSq(this.enderSkeleton) < 16.0D) {
							this.enderSkeleton.teleportRandomly();
						}

						this.teleportTime = 0;
					} else if (this.targetEntity.getDistanceSq(this.enderSkeleton) > 256.0D && this.teleportTime++ >= 30 && this.enderSkeleton.teleportToEntity(this.targetEntity)) {
						this.teleportTime = 0;
					}
				}

				super.updateTask();
			}
		}
	}

}
