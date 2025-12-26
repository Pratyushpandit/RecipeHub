package controller;

import entity.Recipe;
import model.RecipeModel;
import view.*; 
import javax.swing.*;
import java.util.List;
import java.util.Queue;

public class RecipeController {
    private RecipeModel model;
    private MainView mainView; // Home + List
    private AddEditView addEditView; // Form for add/edit

    public RecipeController(RecipeModel model) {
        this.model = model;
        this.mainView = new MainView(this);
        this.addEditView = new AddEditView(this);

        // Initial load
        refreshRecipeList();
        refreshRecentRecipes();
        refreshStats();
    }

    // CRUD Operations
    public void addRecipe(Recipe recipe) {
        try {
            model.addRecipe(recipe);
            JOptionPane.showMessageDialog(mainView.getFrame(), "Recipe added successfully!");
            refreshRecipeList();
            refreshRecentRecipes();
            refreshStats();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(mainView.getFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateRecipe(int selectedIndex, Recipe recipe) {
        if (selectedIndex >= 0) {
            model.updateRecipe(selectedIndex, recipe);
            JOptionPane.showMessageDialog(mainView.getFrame(), "Recipe updated!");
            refreshRecipeList();
            refreshStats();
        }
    }

    public void deleteRecipe(int selectedIndex) {
        if (selectedIndex >= 0) {
            int confirm = JOptionPane.showConfirmDialog(mainView.getFrame(), "Delete this recipe?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.deleteRecipe(selectedIndex);
                refreshRecipeList();
                refreshRecentRecipes();
                refreshStats();
            }
        }
    }

    // Search (Linear Search with partial match)
    public void searchRecipes(String query) {
        if (query.isEmpty()) {
            refreshRecipeList();
            return;
        }
        List<Recipe> results = model.searchByTitleOrCuisine(query);
        mainView.updateRecipeTable(results);
    }

    // Sort by Cooking Time
    public void sortRecipes(boolean ascending) {
        model.sortByCookingTime(ascending);
        refreshRecipeList();
    }

    // Refresh Methods
    public void refreshRecipeList() {
        mainView.updateRecipeTable(model.getAllRecipes());
    }

    public void refreshRecentRecipes() {
        Queue<Recipe> recent = model.getRecentRecipes();
        mainView.updateRecentRecipesPanel(recent);
    }

    public void refreshStats() {
        int total = model.getAllRecipes().size();
        // Simple average cooking time
        double avgTime = model.getAllRecipes().stream()
                .mapToInt(Recipe::getCookingTime)
                .average().orElse(0.0);
        mainView.updateStats(total, (int) avgTime);
    }

    // Show Add Form
    public void showAddForm() {
        addEditView.showAddForm();
    }

    // Show Edit Form
    public void showEditForm(int selectedIndex) {
        if (selectedIndex >= 0) {
            Recipe recipe = model.getAllRecipes().get(selectedIndex);
            addEditView.showEditForm(recipe, selectedIndex);
        } else {
            JOptionPane.showMessageDialog(mainView.getFrame(), "Please select a recipe to edit.");
        }
    }

    // Getters
    public JFrame getMainFrame() {
        return mainView.getFrame();
    }

    // Launch the app
    public void start() {
        SwingUtilities.invokeLater(() -> mainView.getFrame().setVisible(true));
    }
}