package baka943.realmtweaks.common.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class EntitySwampSpider extends EntityMob {

	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntitySwampSpider.class, DataSerializers.BYTE);

	public EntitySwampSpider(World world) {
		super(world);
		this.setSize(0.7F, 0.5F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntitySwampSpider.AISpiderAttack(this));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntitySwampSpider.AISpiderTarget(this, EntityPlayer.class));
		this.targetTasks.addTask(3, new EntitySwampSpider.AISpiderTarget(this, EntityIronGolem.class));
	}

	@Override
	public double getMountedYOffset() {
		return (double) (this.height * 0.5F);
	}

	@Nonnull
	@Override
	protected PathNavigate createNavigator(@Nonnull World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CLIMBING, (byte) 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!this.world.isRemote) {
			this.setBesideClimbableBlock(this.collidedHorizontally);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public boolean isOnLadder() {
		return this.isBesideClimbableBlock();
	}

	@Nonnull
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isPotionApplicable(@Nonnull PotionEffect effect) {
		return effect.getPotion() != MobEffects.POISON && super.isPotionApplicable(effect);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if(super.attackEntityAsMob(entityIn)) {
			if(entityIn instanceof EntityLivingBase) {
				int i = 0;

				if(this.world.getDifficulty() == EnumDifficulty.NORMAL) {
					i = 7;
				} else if(this.world.getDifficulty() == EnumDifficulty.HARD) {
					i = 15;
				}

				if(i > 0) {
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, i * 20, 0));
				}
			}

			return true;
		} else return false;
	}

	public boolean isBesideClimbableBlock() {
		return (this.dataManager.get(CLIMBING) & 1) != 0;
	}

	public void setBesideClimbableBlock(boolean climbing) {
		byte b = this.dataManager.get(CLIMBING);

		if(climbing) {
			b = (byte) (b | 1);
		} else b = (byte) (b & -2);

		this.dataManager.set(CLIMBING, b);
	}

	@Override
	public float getEyeHeight() {
		return 0.45F;
	}

	static class AISpiderAttack extends EntityAIAttackMelee {

		public AISpiderAttack(EntitySwampSpider spider) {
			super(spider, 1.0D, true);
		}

		@Override
		public boolean shouldContinueExecuting() {
			float f = this.attacker.getBrightness();

			if(f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
				this.attacker.setAttackTarget((EntityLivingBase) null);

				return false;
			} else return super.shouldContinueExecuting();
		}

		@Override
		protected double getAttackReachSqr(EntityLivingBase attackTarget) {
			return (double) (4.0F + attackTarget.width);
		}

	}

	static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

		public AISpiderTarget(EntitySwampSpider spider, Class<T> classTarget) {
			super(spider, classTarget, true);
		}

		@Override
		public boolean shouldExecute() {
			float f = this.taskOwner.getBrightness();

			return !(f >= 0.5F) && super.shouldExecute();
		}

	}

	public static class GroupData implements IEntityLivingData {

		public Potion effect;

		public void setRandomEffect(Random rand) {
			int i = rand.nextInt(5);

			if(i <= 1) {
				this.effect = MobEffects.SPEED;
			} else if(i <= 2) {
				this.effect = MobEffects.STRENGTH;
			} else if(i <= 3) {
				this.effect = MobEffects.REGENERATION;
			} else if(i <= 4) {
				this.effect = MobEffects.INVISIBILITY;
			}
		}

	}

}
