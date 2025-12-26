package entity;

public class Recipe {
    private String title;
    private String cuisine;
    private int cookingTime; // in minutes
    private String ingredients; // multiline string
    private int servings;

    public Recipe(String title, String cuisine, int cookingTime, String ingredients, int servings) {
        this.title = title;
        this.cuisine = cuisine;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.servings = servings;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    @Override
    public String toString() {
        return title + " (" + cuisine + ", " + cookingTime + " mins)";
    }
}