package baka943.realmtweaks.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSnowneeeLegs extends ItemSnowneeeArmor {

	private Multimap<String, AttributeModifier> attributes;

	public ItemSnowneeeLegs() {
		super(EntityEquipmentSlot.LEGS, "snowneee_snowball_ii");
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack) {
		if(attributes == null) {
			Builder<String, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put("generic.armor", new AttributeModifier("generic.armor", 1.0D, 0));
			builder.put("generic.movementSpeed", new AttributeModifier("generic.movementSpeed", 0.15D, 1));
			attributes = builder.build();
		}

		return slot == EntityEquipmentSlot.LEGS ? attributes : super.getAttributeModifiers(slot, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flags) {
		list.add(I18n.format("tooltip.realmtweaks.snowneee_legs.desc"));
		super.addInformation(stack, world, list, flags);
	}

}
