package model;

import entity.Recipe;
import java.util.*;

public class RecipeModel {
    private ArrayList<Recipe> recipes;
    private Queue<Recipe> recentRecipes; // For recently added (max 5)

    public RecipeModel() {
        recipes = new ArrayList<>();
        recentRecipes = new LinkedList<>();
        loadSampleData(); // Pre-load at least 5 recipes
    }

    private void loadSampleData() {
        addRecipe(new Recipe("Spaghetti Carbonara", "Italian", 20, "Eggs, Bacon, Cheese, Pasta", 4));
        addRecipe(new Recipe("Chicken Curry", "Indian", 45, "Chicken, Curry powder, Coconut milk", 6));
        addRecipe(new Recipe("Tacos", "Mexican", 30, "Beef, Tortillas, Salsa", 4));
        addRecipe(new Recipe("Sushi Roll", "Japanese", 60, "Rice, Fish, Seaweed", 2));
        addRecipe(new Recipe("Pad Thai", "Thai", 35, "Noodles, Shrimp, Peanuts", 4));
    }

    public void addRecipe(Recipe recipe) {
        // Prevent duplicate titles
        for (Recipe r : recipes) {
            if (r.getTitle().equalsIgnoreCase(recipe.getTitle())) {
                throw new IllegalArgumentException("A recipe with this title already exists!");
            }
        }
        recipes.add(recipe);
        recentRecipes.offer(recipe);
        if (recentRecipes.size() > 5) {
            recentRecipes.poll(); // Remove oldest
        }
    }

    public void updateRecipe(int index, Recipe recipe) {
        recipes.set(index, recipe);
    }

    public void deleteRecipe(int index) {
        recipes.remove(index);
    }

    public ArrayList<Recipe> getAllRecipes() {
        return recipes;
    }

    public Queue<Recipe> getRecentRecipes() {
        return recentRecipes;
    }

    public List<Recipe> searchByTitleOrCuisine(String query) {
        List<Recipe> results = new ArrayList<>();
        query = query.toLowerCase();
        for (Recipe r : recipes) {
            if (r.getTitle().toLowerCase().contains(query) || 
                r.getCuisine().toLowerCase().contains(query)) {
                results.add(r);
            }
        }
        return results;
    }

    public void sortByCookingTime(boolean ascending) {
        recipes.sort((r1, r2) -> ascending ? 
            Integer.compare(r1.getCookingTime(), r2.getCookingTime()) :
            Integer.compare(r2.getCookingTime(), r1.getCookingTime()));
    }
}