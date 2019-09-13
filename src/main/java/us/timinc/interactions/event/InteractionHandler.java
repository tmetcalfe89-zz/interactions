package us.timinc.interactions.event;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.timinc.interactions.Interactions;
import us.timinc.interactions.recipe.InteractRecipe;
import us.timinc.interactions.recipe.InteractRecipes;
import us.timinc.interactions.util.MinecraftUtil;

/**
 * Implements the logic in recipes matching events.
 * 
 * @author Tim
 *
 */
public class InteractionHandler {
	public InteractRecipes interactRecipes;

	/**
	 * Creates a new interaction handler. Initializes recipes.
	 */
	public InteractionHandler() {
		interactRecipes = new InteractRecipes();
	}

	/**
	 * Reloads the recipes.
	 */
	public void reload() {
		interactRecipes = new InteractRecipes();
	}

	/**
	 * Subscribes to the player right clicking a block. Checks that it's a valid
	 * event, then processes it.
	 * 
	 * @param event
	 *            The interaction event.
	 */
	@SubscribeEvent
	public void onInteraction(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote)
			return;

		if (!event.getEntityPlayer().canPlayerEdit(event.getPos(), event.getFace(),
				event.getEntityPlayer().getHeldItem(event.getHand())))
			return;

		checkForInteractions(event);
	}

	/**
	 * The core of the event processing. We look for an interact recipe that
	 * matches the event, check to see what it does, roll for the modifications
	 * it makes, and execute the changes.
	 * 
	 * @param event
	 *            The interaction event.
	 */
	private void checkForInteractions(RightClickBlock event) {
		// Create a matcher from the event to compare against existing recipes.
		InteractRecipeMatcher eventMatcher = InteractRecipeMatcher.buildFromEvent(event);

		// Find any recipes that match the event and process them.
		ArrayList<InteractRecipe> matches = interactRecipes.findMatches(eventMatcher);
		matches.forEach(r -> processInteraction(r, event));
	}

	/**
	 * Process an interaction recipe given an event context.
	 * 
	 * @param recipe
	 *            The interaction recipe to process.
	 * @param event
	 *            The event context.
	 */
	private void processInteraction(InteractRecipe recipe, RightClickBlock event) {
		World world = event.getWorld();
		BlockPos targetPosition = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItem(event.getHand());

		// If the recipe changes the target block, roll for it, and do it if
		// successful.
		if (recipe.changesTargetBlock() && recipe.rollForChangeBlock()) {
			world.setBlockState(targetPosition, recipe.getChangeBlockState());
		}

		// If the recipe drops an item from the target block, roll for it,
		// and do it if successful.
		if (recipe.dropsItem() && recipe.rollForDropItem()) {
			EntityItem itemDropEntity = MinecraftUtil.createEntityItem(world, targetPosition, recipe.createDrop());

			world.spawnEntity(itemDropEntity);
		}

		// If the recipe damages the held item, roll for it, and do it if
		// successful.
		if (recipe.damagesHeldItem() && recipe.rollForDamageItem()) {
			MinecraftUtil.damageItemStack(heldItem, recipe.getDamage(), player);
		}
	}
}
