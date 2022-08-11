/** Made with Blockbench 4.1.5
 * Exported for Minecraft version 1.7 - 1.12
 * Paste this class into your mod and generate all required imports
 */
package baka943.realmtweaks.client.model.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nonnull;

public class ModelSnowneee extends ModelBiped {

	protected final EntityEquipmentSlot slot;

	private final ModelRenderer Head;
	private final ModelRenderer Chest;
	private final ModelRenderer ArmLeft;
	private final ModelRenderer ArmRight;
	private final ModelRenderer Ball;
	private final ModelRenderer LegLeft;
	private final ModelRenderer LegRight;
	private final ModelRenderer BootsLeft;
	private final ModelRenderer BootsRight;

	ModelRenderer Zipper;
	ModelRenderer cube_r1;
	ModelRenderer cube_r2;

	public ModelSnowneee(EntityEquipmentSlot slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 64;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.cubeList.add(new ModelBox(Head, 5, 2, -2.0F, -7.0F, -4.5F, 1, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 5, 2, 2.0F, -6.0F, -4.5F, 1, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 6, 0, -3.0F, -4.0F, -4.5F, 1, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 5, 2, -2.0F, -3.0F, -4.5F, 1, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 0, 3, -1.0F, -2.0F, -4.5F, 2, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 0, 3, 1.0F, -3.0F, -4.5F, 2, 1, 1, 0.0F, false));
		Head.cubeList.add(new ModelBox(Head, 32, 36, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.1F, false));

		Chest = new ModelRenderer(this);
		Chest.setRotationPoint(0.0F, 0.0F, 0.0F);
		Chest.cubeList.add(new ModelBox(Chest, 0, 44, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.26F, false));
		Chest.cubeList.add(new ModelBox(Chest, 0, 24, -5.0F, 0.0F, -5.0F, 10, 10, 10, 0.25F, false));
		Chest.cubeList.add(new ModelBox(Chest, 0, 0, -1.0F, 1.0F, -6.0F, 2, 2, 1, 0.0F, false));
		Chest.cubeList.add(new ModelBox(Chest, 0, 0, -1.0F, 4.0F, -6.0F, 2, 2, 1, 0.0F, false));
		Chest.cubeList.add(new ModelBox(Chest, 0, 0, -1.0F, 7.0F, -6.0F, 2, 2, 1, 0.0F, false));

		Zipper = new ModelRenderer(this);
		Zipper.setRotationPoint(0.0F, 0.0F, 0.0F);
		Chest.addChild(Zipper);
		Zipper.cubeList.add(new ModelBox(Zipper, 7, 4, -1.5F, -0.3F, 4.6F, 1, 1, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 7, 4, 0.5F, -0.3F, 4.6F, 1, 1, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, 0.5F, 1.1F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, -1.5F, 1.1F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, -1.5F, 3.5F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, 0.5F, 3.5F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, 0.5F, 5.9F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, -1.5F, 5.9F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, 0.5F, 8.3F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 0, 5, -1.5F, 8.3F, 4.6F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 4, 5, -0.5F, -0.1F, 4.5F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 4, 5, -0.5F, 2.2F, 4.5F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 4, 5, -0.5F, 4.5F, 4.5F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 4, 5, -0.5F, 6.8F, 4.5F, 1, 2, 1, 0.0F, false));
		Zipper.cubeList.add(new ModelBox(Zipper, 6, 0, -0.5F, 9.2F, 4.5F, 1, 1, 1, 0.0F, false));

		ArmLeft = new ModelRenderer(this);
		ArmLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
		ArmLeft.cubeList.add(new ModelBox(ArmLeft, 48, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.26F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(6.0F, -1.0F, -2.0F);
		ArmLeft.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.0436F, 0.0436F, 1.309F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 60, -2.0F, 4.0F, -1.0F, 12, 2, 2, -0.5F, false));

		ArmRight = new ModelRenderer(this);
		ArmRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
		ArmRight.cubeList.add(new ModelBox(ArmRight, 48, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.26F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-6.0F, -1.0F, -2.0F);
		ArmRight.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.0436F, -0.0436F, -1.309F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 60, -10.0F, 4.0F, -1.0F, 12, 2, 2, -0.5F, false));

		Ball = new ModelRenderer(this);
		Ball.setRotationPoint(0.0F, 0.0F, 0.0F);
		Ball.cubeList.add(new ModelBox(Ball, 0, 0, -6.0F, 10.0F, -6.0F, 12, 12, 12, 0.25F, false));
		Ball.cubeList.add(new ModelBox(Ball, 0, 44, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.26F, false));
		Ball.cubeList.add(new ModelBox(Ball, 0, 0, -1.0F, 14.0F, -7.0F, 2, 2, 1, 0.0F, false));
		Ball.cubeList.add(new ModelBox(Ball, 0, 0, -1.0F, 11.0F, -7.0F, 2, 2, 1, 0.0F, false));
		Ball.cubeList.add(new ModelBox(Ball, 0, 0, -1.0F, 17.0F, -7.0F, 2, 2, 1, 0.0F, false));
		Ball.cubeList.add(new ModelBox(Ball, 24, 52, -4.0F, 1.0F, -2.3F, 8, 8, 0, 0.0F, false));
		Ball.cubeList.add(new ModelBox(Ball, 24, 52, -4.0F, 1.0F, 2.3F, 8, 8, 0, 0.0F, false));

		LegLeft = new ModelRenderer(this);
		LegLeft.setRotationPoint(1.9F, 12.0F, 0.0F);
		LegLeft.cubeList.add(new ModelBox(LegLeft, 48, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.26F, false));

		LegRight = new ModelRenderer(this);
		LegRight.setRotationPoint(-1.9F, 12.0F, 0.0F);
		LegRight.cubeList.add(new ModelBox(LegRight, 48, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.26F, false));

		BootsLeft = new ModelRenderer(this);
		BootsLeft.setRotationPoint(1.9F, 12.0F, 0.0F);
		BootsLeft.cubeList.add(new ModelBox(BootsLeft, 32, 36, -4.0F, 5.5F, -4.0F, 8, 8, 8, -1.0F, false));

		BootsRight = new ModelRenderer(this);
		BootsRight.setRotationPoint(-1.9F, 12.0F, 0.0F);
		BootsRight.cubeList.add(new ModelBox(BootsRight, 32, 36, -4.0F, 5.5F, -4.0F, 8, 8, 8, -1.0F, false));
	}

	@Override
	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Head.showModel = slot == EntityEquipmentSlot.CHEST;
		Chest.showModel = slot == EntityEquipmentSlot.CHEST;
		ArmLeft.showModel = slot == EntityEquipmentSlot.CHEST;
		ArmRight.showModel = slot == EntityEquipmentSlot.CHEST;
		LegLeft.showModel = slot == EntityEquipmentSlot.LEGS;
		LegRight.showModel = slot == EntityEquipmentSlot.LEGS;
		Ball.showModel = slot == EntityEquipmentSlot.LEGS;
		BootsLeft.showModel = slot == EntityEquipmentSlot.FEET;
		BootsRight.showModel = slot == EntityEquipmentSlot.FEET;
		bipedHeadwear.showModel = false;

		bipedHead = Head;
		bipedLeftArm = ArmLeft;
		bipedRightArm = ArmRight;

		if(slot == EntityEquipmentSlot.LEGS) {
			bipedBody = Ball;
			bipedLeftLeg = LegLeft;
			bipedRightLeg = LegRight;
		} else {
			bipedBody = Chest;
			bipedLeftLeg = BootsLeft;
			bipedRightLeg = BootsRight;
		}

		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	protected void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}

