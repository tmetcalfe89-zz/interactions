package us.timinc.interactions.recipe;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import us.timinc.interactions.event.InteractRecipeMatcher;
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
	 * The chance (x in y represented as x:y) for an item to be dropped.
	 */
	public String dropChance = "";
	/**
	 * The damage to be dealt to the held item.
	 * 
	 * @see us.timinc.interactions.util.MinecraftUtil#damageItemStack(ItemStack, int, net.minecraft.entity.EntityLivingBase)
	 */
	public String damage = "";
	/**
	 * The chance (x in y represented as x:y) for the held item to be damaged.
	 */
	public String damageChance = "";

	private InteractRecipeMatcher matcher = null;
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
	 * @return Whether or not this recipe drops an item.
	 */
	public boolean dropsItem() {
		return !dropItemId.isEmpty();
	}

	/**
	 * Returns whether or not this recipe damages the held item.
	 * 
	 * @return Whether or not this recipe damages the held item.
	 */
	public boolean damagesHeldItem() {
		return !damage.isEmpty();
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
		ItemStack dropped = IdUtil.createItemStackFrom(dropItemId);
		return dropped;
	}

	/**
	 * Turns the damage string into an integer. Caches the result after the
	 * first use.
	 * 
	 * @return The amount of damage this recipe deals.
	 */
	public int getDamage() {
		if (damageInt == -1) {
			damageInt = Integer.parseInt(damage);
		}
		return damageInt;
	}

	/**
	 * Returns a matcher for this recipe for the purposes of matching it with
	 * another. Caches the result after the first use.
	 * 
	 * @return A matcher for this recipe.
	 */
	public InteractRecipeMatcher getMatcher() {
		if (matcher == null) {
			matcher = new InteractRecipeMatcher(targetBlockId, heldItemId);
		}
		return matcher;
	}
}
