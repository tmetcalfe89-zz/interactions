package us.timinc.interactions.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import us.timinc.interactions.util.IdUtil;

/**
 * A class dedicated to figuring out whether an event matches a recipe.
 * 
 * @author Tim
 *
 */
public class InteractRecipeMatcher {
	/**
	 * The ID of the target block.
	 */
	public String targetBlockId;
	/**
	 * The ID of the held item.
	 */
	public String heldItemId;

	/**
	 * Creates a new instance from the required IDs.
	 * 
	 * @param targetBlockId
	 *            The ID of the target block.
	 * @param handItemId
	 *            The ID of the held item.
	 */
	public InteractRecipeMatcher(String targetBlockId, String handItemId) {
		this.targetBlockId = targetBlockId;
		this.heldItemId = handItemId;
	}

	/**
	 * Creates a new instance from an event.
	 * 
	 * @param event
	 *            The interaction event.
	 * @return A new matcher.
	 */
	public static InteractRecipeMatcher buildFromEvent(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();

		String targetBlockId = IdUtil.getBlockId(world.getBlockState(event.getPos()));
		String heldItemId = IdUtil.getItemId(player.getHeldItem(event.getHand()));
		return new InteractRecipeMatcher(targetBlockId, heldItemId);
	}

	/**
	 * Determines whether this matcher matches another.
	 * 
	 * @param other
	 *            The other matcher.
	 * @return Whether the two match.
	 */
	public boolean matches(InteractRecipeMatcher other) {
		return this.targetBlockId.equals(other.targetBlockId) && itemIdsMatch(heldItemId, other.heldItemId);
	}

	/**
	 * Determines whether two item IDs match. Does not take meta into account if
	 * the item stack is damageable.
	 * 
	 * @param itemId1
	 *            The first item ID.
	 * @param itemId2
	 *            The second item ID.
	 * @return Whether the two item IDs match.
	 */
	public boolean itemIdsMatch(String itemId1, String itemId2) {
		if (IdUtil.createItemStackFrom(itemId1).isItemStackDamageable()) {
			String[] split1 = itemId1.split(":");
			String[] split2 = itemId2.split(":");
			return split1[0].equals(split2[0]) && split1[1].equals(split2[1]);
		} else {
			return itemId1.equals(itemId2);
		}
	}
}
