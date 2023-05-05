package com.sawan.recipeone;

public class Recipe {
    String recipeName;
    String preparationTime;
    String description;
    public Recipe(String recipeName, String preparationTime, String description) {
        this.recipeName = recipeName;
        this.preparationTime = preparationTime;
        this.description = description;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getRecipeName() {
        return this.recipeName;
    }
    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
    public String getPreparationTime() {
        return this.preparationTime;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
}
