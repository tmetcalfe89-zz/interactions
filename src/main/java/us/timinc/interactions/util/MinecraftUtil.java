package us.timinc.interactions.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A util class for dealing with quirks between Minecraft and myself.
 * 
 * @author Tim
 *
 */
public class MinecraftUtil {
	/**
	 * Damages an item stack. If the item can take damage directly, it takes the
	 * damage, otherwise the item stack loses quantity equal to the damage.
	 * 
	 * @param itemStack
	 *            The item stack to damage.
	 * @param damage
	 *            The damage to deal.
	 * @param entity
	 *            The entity that is doing the damage.
	 */
	public static void damageItemStack(ItemStack itemStack, int damage, EntityLivingBase entity) {
		if (itemStack.isItemStackDamageable()) {
			itemStack.damageItem(damage, entity);
		} else {
			itemStack.shrink(damage);
		}
	}

	/**
	 * Convenience method for creating an entity item using a position instead
	 * of the x, y, z coordinates.
	 * 
	 * @param world
	 *            The world in which the entity will exist.
	 * @param position
	 *            The position at which the entity will exist.
	 * @param itemStack
	 *            The item stack which the entity will hold.
	 * @return A new entity item.
	 */
	public static EntityItem createEntityItem(World world, BlockPos position, ItemStack itemStack) {
		return new EntityItem(world, position.getX(), position.getY(), position.getZ(), itemStack);
	}
}
