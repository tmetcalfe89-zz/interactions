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
		return this.targetBlockId.equals(other.targetBlockId) && IdUtil.itemIdsMatch(heldItemId, other.heldItemId);
	}
}
