package baka943.realmtweaks.client.core.model;

import baka943.realmtweaks.common.entity.monster.EntityEnderSkeleton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelEnderSkeleton extends ModelBiped {

	public ModelEnderSkeleton() {
		this(0.0F, false);
	}

	public ModelEnderSkeleton(float modelSize, boolean flag) {
		super(modelSize, 0.0F, 64, 32);

		if(!flag) {
			this.bipedRightArm = new ModelRenderer(this, 40, 16);
			this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

			this.bipedLeftArm = new ModelRenderer(this, 40, 16);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

			this.bipedRightLeg = new ModelRenderer(this, 0, 16);
			this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);

			this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
			this.bipedLeftLeg.mirror = true;
			this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		}
	}

	@Override
	public void setLivingAnimations(@Nonnull EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.rightArmPose = ArmPose.EMPTY;
		this.leftArmPose = ArmPose.EMPTY;
		ItemStack stack = entityLivingBase.getHeldItem(EnumHand.MAIN_HAND);

		if(stack.getItem() instanceof ItemBow && ((EntityEnderSkeleton) entityLivingBase).isSwingingArms()) {
			if(entityLivingBase.getPrimaryHand() == EnumHandSide.RIGHT) {
				this.rightArmPose = ArmPose.BOW_AND_ARROW;
			} else this.leftArmPose = ArmPose.BOW_AND_ARROW;
		}

		super.setLivingAnimations(entityLivingBase, limbSwing, limbSwingAmount, partialTickTime);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

		ItemStack stack = ((EntityLivingBase) entity).getHeldItemMainhand();
		EntityEnderSkeleton skeleton = (EntityEnderSkeleton) entity;

		if(skeleton.isSwingingArms() && (stack.isEmpty() || !(stack.getItem() instanceof ItemBow))) {
			float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
			float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.F - this.swingProgress)) * (float)Math.PI);

			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
			this.bipedRightArm.rotateAngleX = -1.5707964F;

			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
			this.bipedLeftArm.rotateAngleX = 1.5707964F;

			ModelRenderer renderer = this.bipedRightArm;
			renderer.rotateAngleX -= f * 1.2F - f1 * 0.4F;

			renderer = this.bipedLeftArm;
			renderer.rotateAngleX -= f * 1.2F - f1 * 0.4F;

			renderer = this.bipedRightArm;
			renderer.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;

			renderer = this.bipedLeftArm;
			renderer.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;

			renderer = this.bipedRightArm;
			renderer.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

			renderer = this.bipedLeftArm;
			renderer.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		}
	}

	@Override
	public void postRenderArm(float scale, @Nonnull EnumHandSide side) {
		float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer renderer = this.getArmForSide(side);
		renderer.rotationPointX += f;
		renderer.postRender(scale);
		renderer.rotationPointX -= f;
	}

}
