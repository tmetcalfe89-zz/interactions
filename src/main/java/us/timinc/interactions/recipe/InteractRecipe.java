package us.timinc.interactions.recipe;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import us.timinc.interactions.util.IdUtil;
import us.timinc.interactions.util.RandUtil;

/**
 * Holds all of the details about a particular interact recipe.
 * 
 * @author Tim
 *
 */
public class InteractRecipe {
	/**
	 * The ID of the block that has been interacted with in the world.
	 */
	@SerializedName("targetBlock")
	public String targetBlockId = "";
	/**
	 * The ID of the item held in the player's hand. Note that we check both the
	 * main hand and the off hand.
	 */
	@SerializedName("heldItem")
	public String heldItemId = "";
	/**
	 * The ID of the block to replace the target block into in the world.
	 */
	@SerializedName("replacementBlock")
	public String replacementBlockId = "";
	/**
	 * The chance (x in y represented as x:y) for the target block to be
	 * replaced.
	 */
	public String replacementChance = "";
	/**
	 * The ID of the item to drop.
	 */
	@SerializedName("dropItem")
	public String dropItemId = "";
	/**
	 * Whether or not an item being dropped is dependent on the block
	 * successfully changing.
	 */
	public String dropOnlyOnSuccess = "false";
	/**
	 * The chance (x in y represented as x:y) for an item to be dropped.
	 */
	public String dropChance = "";
	/**
	 * The count (from x to y represented as x:y) of items to be dropped.
	 */
	public String dropCount = "1";
	/**
	 * The damage to be dealt to the held item.
	 * 
	 * @see us.timinc.interactions.util.MinecraftUtil#damageItemStack(ItemStack,
	 *      int, net.minecraft.entity.EntityLivingBase)
	 */
	public String damage = "";
	/**
	 * Whether or not damage being dealt to the held item is dependent on the
	 * block successfully changing.
	 */
	public String damageOnlyOnSuccess = "false";
	/**
	 * The chance (x in y represented as x:y) for the held item to be damaged.
	 */
	public String damageChance = "";
	/**
	 * The name of the particle to be emitted.
	 */
	public String particleType = "";
	/**
	 * The number of particles (from x to y represented as x:y) to be emitted.
	 */
	public String particleCount = "15";

	private int damageInt = -1;

	/**
	 * Returns whether or not this recipe changes the target block.
	 * 
	 * @return Whether or not this recipe changes the target block.
	 */
	public boolean changesTargetBlock() {
		return !replacementBlockId.isEmpty();
	}

	/**
	 * Returns whether or not this recipe drops an item.
	 * 
	 * @param success
	 *            Whether or not the recipe iteration succeeded.
	 * 
	 * @return Whether or not this recipe drops an item.
	 */
	public boolean dropsItem(boolean success) {
		if (dropRequiresSuccess() && !success) {
			return false;
		}
		return !dropItemId.isEmpty();
	}

	/**
	 * Returns whether or not this recipe requires the block change to be
	 * successful in order to drop an item.
	 * 
	 * @return Whether this recipe requires block change for drop.
	 */
	public boolean dropRequiresSuccess() {
		return Boolean.parseBoolean(this.dropOnlyOnSuccess);
	}

	/**
	 * Returns whether or not this recipe damages the held item.
	 * 
	 * @param success
	 *            Whether or not the recipe iteration succeeded.
	 * 
	 * @return Whether or not this recipe damages the held item.
	 */
	public boolean damagesHeldItem(boolean success) {
		if (damageRequiresSuccess() && !success) {
			return false;
		}
		return !damage.isEmpty();
	}

	public boolean damageRequiresSuccess() {
		return Boolean.parseBoolean(this.damageOnlyOnSuccess);
	}

	/**
	 * Rolls to determine whether a particular execution of a recipe changes the
	 * block.
	 * 
	 * @return Whether a particular execution of a recipe changes the block.
	 */
	public boolean rollForChangeBlock() {
		return replacementChance.isEmpty() || rollFor(replacementChance);
	}

	/**
	 * Rolls to determine whether a particular execution of a recipe drops an
	 * item.
	 * 
	 * @return Whether a particular execution of a recipe drops an item.
	 */
	public boolean rollForDropItem() {
		return dropChance.isEmpty() || rollFor(dropChance);
	}

	/**
	 * Rolls to determine whether a particular execution of a recipe damages the
	 * held item.
	 * 
	 * @return Whether a particular execution of a recipe damages the held item.
	 */
	public boolean rollForDamageItem() {
		return damageChance.isEmpty() || rollFor(damageChance);
	}

	/**
	 * Given a valid string denoting x in y chances ("x:y"), this rolls a
	 * theoretical dice with y sides and returns true if the rolled value is
	 * less than x.
	 * 
	 * @param chance
	 *            A string denoting x:y chances.
	 * @return Whether the rolled value (up to y) is lower than x.
	 */
	private boolean rollFor(String chance) {
		String[] splitChance = chance.split(":");
		return RandUtil.rollSuccess(Integer.parseInt(splitChance[1]), Integer.parseInt(splitChance[0]) + 1);
	}

	/**
	 * Turns the change block ID into a block state.
	 * 
	 * @return A block state for the change block.
	 */
	public IBlockState getChangeBlockState() {
		return IdUtil.getBlockStateFrom(replacementBlockId);
	}

	/**
	 * Turns the drop item ID into an item stack.
	 * 
	 * @return An item stack for the drop item.
	 */
	public ItemStack createDrop() {
		ItemStack dropped = IdUtil.createItemStackFrom(dropItemId, rollForDropCount());
		return dropped;
	}

	public int rollForDropCount() {
		if (!dropCount.contains(":"))
			return Integer.parseInt(dropCount);

		String[] splitCount = dropCount.split(":");
		return RandUtil.roll(Integer.parseInt(splitCount[0]), Integer.parseInt(splitCount[1]));
	}

	/**
	 * Turns the damage string into an integer. Caches the result after the
	 * first use.
	 * 
	 * @return The amount of damage this recipe deals.
	 */
	public int getDamage() {
		if (!this.damagesHeldItem(true)) {
			return 0;
		}
		if (damageInt == -1) {
			damageInt = Integer.parseInt(damage);
		}
		return damageInt;
	}

	public boolean spawnsParticles() {
		return !particleType.isEmpty();
	}

	public int rollForParticleCount() {
		if (!particleCount.contains(":"))
			return Integer.parseInt(particleCount);

		String[] splitCount = particleCount.split(":");
		return RandUtil.roll(Integer.parseInt(splitCount[0]), Integer.parseInt(splitCount[1]));
	}

	public String getParticleName() {
		String[] splitParticleType = particleType.split(":");
		return splitParticleType[0];
	}

	public String getParticleParam() {
		String[] splitParticleType = particleType.split(":");
		if (splitParticleType.length == 1) {
			return "";
		}
		return (splitParticleType[1] + ":" + splitParticleType[2] + ":" + splitParticleType[3]);
	}

	public boolean matches(RightClickBlock event) {
		return IdUtil.matches(this.heldItemId, IdUtil.getItemId(event.getItemStack())) && IdUtil
				.matches(this.targetBlockId, IdUtil.getBlockId(event.getWorld().getBlockState(event.getPos())));
	}
}
