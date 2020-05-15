package baka943.realmtweaks.common.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntitySwampSpider extends EntitySpider {

	public EntitySwampSpider(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 0.5F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
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
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, i * 20, 0));
				}
			}

			return true;
		} else return false;
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		return livingdata;
	}

	@Override
	public float getEyeHeight() {
		return 0.45F;
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

}
