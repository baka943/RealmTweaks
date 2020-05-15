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
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
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
				((EntityTippedArrow) entityArrow).setPotionEffect(stack);
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

}
