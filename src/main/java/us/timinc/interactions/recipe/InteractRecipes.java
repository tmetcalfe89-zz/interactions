package us.timinc.interactions.recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
	// private HashMap<String, ArrayList<InteractRecipe>> recipes;
	private ArrayList<InteractRecipe> recipes;
	private Gson gson;

	/**
	 * Creates a new instance.
	 */
	public InteractRecipes() {
		this.recipes = new ArrayList<>();
		this.gson = new Gson();
		loadRecipes();
	}

	/**
	 * Adds a single recipe to the recipe list.
	 * 
	 * @param recipe
	 *            The recipe to add.
	 */
	private void add(InteractRecipe recipe) {
		recipes.add(recipe);
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
			add(newRecipes[i]);
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
	 * Finds all matching recipes for the given matcher in the recipes.
	 * 
	 * @param matcher
	 *            The given matcher.
	 * @return The first matching recipe.
	 */
	public ArrayList<InteractRecipe> findMatches(InteractRecipeMatcher matcher) {
		return (ArrayList<InteractRecipe>) recipes.stream().filter(r -> r.getMatcher().matches(matcher))
				.collect(Collectors.toList());
	}

	public int getRecipeCount() {
		return recipes.size();
	}
}
