package view;

import controller.RecipeController;
import entity.Recipe;
import javax.swing.*;
import java.awt.*;

public class AddEditView {
    private JDialog dialog;
    private JTextField titleField;
    private JTextField cuisineField;
    private JTextField timeField;
    private JTextField servingsField;
    private JTextArea ingredientsArea;

    private RecipeController controller;
    private int editIndex = -1; // -1 means Add mode

    public AddEditView(RecipeController controller) {
        this.controller = controller;
        createDialog();
    }

    private void createDialog() {
        dialog = new JDialog(controller.getMainFrame(), "Add/Edit Recipe", true);
        dialog.setSize(500, 500);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(controller.getMainFrame());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(25);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Cuisine:"), gbc);
        gbc.gridx = 1;
        cuisineField = new JTextField(25);
        formPanel.add(cuisineField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cooking Time (mins):"), gbc);
        gbc.gridx = 1;
        timeField = new JTextField(25);
        formPanel.add(timeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Servings:"), gbc);
        gbc.gridx = 1;
        servingsField = new JTextField(25);
        formPanel.add(servingsField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Ingredients (one per line):"), gbc);
        gbc.gridx = 1;
        ingredientsArea = new JTextArea(8, 25);
        ingredientsArea.setBorder(BorderFactory.createLoweredBevelBorder());
        formPanel.add(new JScrollPane(ingredientsArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveRecipe());
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveRecipe() {
        try {
            // Validation
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty!");
            }

            String cuisine = cuisineField.getText().trim();
            if (cuisine.isEmpty()) {
                throw new IllegalArgumentException("Cuisine cannot be empty!");
            }

            int cookingTime = Integer.parseInt(timeField.getText().trim());
            if (cookingTime <= 0 || cookingTime > 600) {
                throw new IllegalArgumentException("Cooking time must be between 1 and 600 minutes!");
            }

            int servings = Integer.parseInt(servingsField.getText().trim());
            if (servings <= 0) {
                throw new IllegalArgumentException("Servings must be a positive number!");
            }

            String ingredients = ingredientsArea.getText().trim();
            if (ingredients.isEmpty()) {
                throw new IllegalArgumentException("Please enter at least one ingredient!");
            }

            Recipe recipe = new Recipe(title, cuisine, cookingTime, ingredients, servings);

            if (editIndex == -1) {
                // Add mode
                controller.addRecipe(recipe);
            } else {
                // Edit mode
                controller.updateRecipe(editIndex, recipe);
            }

            dialog.dispose(); // Close dialog

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Cooking time and servings must be valid numbers!", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show for Adding new recipe
    public void showAddForm() {
        dialog.setTitle("Add New Recipe");
        clearFields();
        editIndex = -1;
        dialog.setVisible(true);
    }

    // Show for Editing existing recipe
    public void showEditForm(Recipe recipe, int index) {
        dialog.setTitle("Edit Recipe");
        titleField.setText(recipe.getTitle());
        cuisineField.setText(recipe.getCuisine());
        timeField.setText(String.valueOf(recipe.getCookingTime()));
        servingsField.setText(String.valueOf(recipe.getServings()));
        ingredientsArea.setText(recipe.getIngredients());
        editIndex = index;
        dialog.setVisible(true);
    }

    private void clearFields() {
        titleField.setText("");
        cuisineField.setText("");
        timeField.setText("");
        servingsField.setText("");
        ingredientsArea.setText("");
    }
}