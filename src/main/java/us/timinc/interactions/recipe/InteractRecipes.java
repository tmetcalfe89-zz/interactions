package us.timinc.interactions.recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import us.timinc.interactions.event.InteractRecipeMatcher;

/**
 * Holds the recipes as loaded from file.
 * 
 * @author Tim
 *
 */
public class InteractRecipes {
	private HashMap<String, ArrayList<InteractRecipe>> recipes;
	private Gson gson;

	/**
	 * Creates a new instance.
	 */
	public InteractRecipes() {
		this.recipes = new HashMap<>();
		this.gson = new Gson();
		loadRecipes();
	}

	/**
	 * Adds a single recipe to the recipe list.
	 * 
	 * @param recipe
	 *            The recipe to add.
	 */
	private void addRecipe(InteractRecipe recipe) {
		if (!recipes.containsKey(recipe.targetBlockId)) {
			recipes.put(recipe.targetBlockId, new ArrayList<InteractRecipe>());
		}
		recipes.get(recipe.targetBlockId).add(recipe);
	}

	/**
	 * Adds recipes from a file.
	 * 
	 * @param file
	 *            The file to load from.
	 * @throws JsonSyntaxException
	 *             If the JSON syntax is invalid.
	 * @throws JsonIOException
	 *             If there is a JSON-related IO issue.
	 * @throws FileNotFoundException
	 *             If the file is not found.
	 */
	private void addRecipesFrom(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		InteractRecipe[] newRecipes = gson.fromJson(new FileReader(file), InteractRecipe[].class);
		for (int i = 0; i < newRecipes.length; i++) {
			addRecipe(newRecipes[i]);
		}
	}

	/**
	 * Loads recipe files from the directory.
	 */
	private void loadRecipes() {
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

	/**
	 * Finds the first matching recipe for the given matcher in the recipes.
	 * 
	 * @param matcher
	 *            The given matcher.
	 * @return The first matching recipe.
	 */
	public InteractRecipe findMatch(InteractRecipeMatcher matcher) {
		InteractRecipe match = null;
		if (recipes.containsKey(matcher.targetBlockId)) {
			ArrayList<InteractRecipe> possibleMatches = recipes.get(matcher.targetBlockId);

			int i = 0;
			while (match == null && i < possibleMatches.size()) {
				InteractRecipe testRecipe = possibleMatches.get(i);
				if (matcher.matches(testRecipe.getMatcher())) {
					match = testRecipe;
				}
				i++;
			}
		}
		return match;
	}
}
