package us.timinc.interactions.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.timinc.interactions.recipe.InteractRecipe;

public class InteractionHandler {
	private HashMap<String, ArrayList<InteractRecipe>> recipes = new HashMap<>();
	private Gson gson;

	public InteractionHandler() {
		gson = new Gson();
	}

	public void addRecipe(InteractRecipe recipe) {
		if (!recipes.containsKey(recipe.targetBlockId)) {
			recipes.put(recipe.targetBlockId, new ArrayList<InteractRecipe>());
		}
		recipes.get(recipe.targetBlockId).add(recipe);
	}

	public void addRecipesFrom(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		InteractRecipe[] newRecipes = gson.fromJson(new FileReader(file), InteractRecipe[].class);
		for (int i = 0; i < newRecipes.length; i++) {
			addRecipe(newRecipes[i]);
		}
	}

	public void checkForInteractions(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getEntityPlayer().canPlayerEdit(event.getPos(), event.getFace(),
				event.getEntityPlayer().getHeldItem(event.getHand())))
			return;

		String targetBlockId = getBlockId(event.getWorld().getBlockState(event.getPos()));
		String heldItemId = getItemId(event.getEntityPlayer().getHeldItem(event.getHand()));

		InteractRecipe match = findMatch(targetBlockId, heldItemId);
		if (match != null) {
			if (match.changesBlock() && match.rollForChangeBlock()) {
				event.getWorld().setBlockState(event.getPos(), match.getIntoBlock());
			}
			if (match.dropsItem() && match.rollForDropItem()) {
				event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(),
						event.getPos().getY(), event.getPos().getZ(), match.createDrop()));
			}
			if (match.damagesItem() && match.rollForDamageItem()) {
				ItemStack heldItem = event.getEntityPlayer().getHeldItem(event.getHand());
				if (heldItem.isItemStackDamageable()) {
					heldItem.damageItem(match.getDamage(), event.getEntityPlayer());
				} else {
					heldItem.shrink(match.getDamage());
				}
			}
		}
	}

	public InteractRecipe findMatch(String targetBlockId, String heldItemId) {
		InteractRecipe match = null;
		if (recipes.containsKey(targetBlockId)) {
			ArrayList<InteractRecipe> possibleMatches = recipes.get(targetBlockId);

			int i = 0;
			while (match == null && i < possibleMatches.size()) {
				InteractRecipe testRecipe = possibleMatches.get(i);
				if (testRecipe.matches(targetBlockId, heldItemId)) {
					match = testRecipe;
				}
				i++;
			}
		}
		return match;
	}

	public String getBlockId(IBlockState blockState) {
		Block block = blockState.getBlock();
		return block.getRegistryName().toString() + ":" + block.getMetaFromState(blockState);
	}

	public String getItemId(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return item.getRegistryName().toString() + ":" + itemStack.getMetadata();
	}

	@SubscribeEvent
	public void interaction(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote)
			return;

		checkForInteractions(event);
	}

	public void loadRecipes() {
		File globalDir = new File("interactions");
		if (!globalDir.exists())
			globalDir.mkdirs();
		String[] files = globalDir.list();
		files = Arrays.stream(files).filter(x -> x.endsWith(".json")).toArray(String[]::new);
		for (int i = 0; i < files.length; i++) {
			try {
				addRecipesFrom(new File(globalDir, files[i]));
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
