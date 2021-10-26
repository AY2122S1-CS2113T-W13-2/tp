package gordon.kitchen;

import gordon.exception.GordonException;
import gordon.util.Difficulty;
import gordon.util.Tag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cookbook {
    protected ArrayList<Recipe> recipes;
    protected ArrayList<Tag> cookbookTags;

    public Cookbook() {
        recipes = new ArrayList<>();
        cookbookTags = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < recipes.size(); i++) {
            output.append(i + 1).append(". ");
            output.append(recipes.get(i).getName());
            output.append(System.lineSeparator());
        }
        return output.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cookbook cookbook = (Cookbook) o;
        return Objects.equals(recipes, cookbook.recipes)
                && Objects.equals(cookbookTags, cookbook.cookbookTags);
    }

    public String saveString(int index) {
        return recipes.get(index).toString();
    }

    public int numRecipes() {
        return recipes.size();
    }

    public void addRecipe(Recipe r) throws GordonException {
        boolean contains = recipes.stream()
                .map(Recipe::getName)
                .collect(Collectors.toCollection(ArrayList::new))
                .contains(r.name);

        if (contains) {
            throw new GordonException(GordonException.DUPLICATE_RECIPE_NAME);
        }

        recipes.add(r);
    }

    public void removeRecipe(int index) throws GordonException {
        try {
            assert (index >= 0);
            recipes.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new GordonException(GordonException.INDEX_OOB);
        }
    }

    public void isRecipeExist(String name) throws GordonException {
        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
                return;
            }
        }
        throw new GordonException(GordonException.NO_RECIPE_FOUND);
    }

    public void checkRecipe(String name) throws GordonException {
        boolean isRecipeFound = false;

        System.out.println("Finding recipes called " + name + ".....");
        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
                isRecipeFound = true;
                System.out.println("====================");
                System.out.print(recipe);
                System.out.println("====================");
            }
        }

        if (!isRecipeFound) {
            throw new GordonException(GordonException.NO_RESULT_FOUND);
        }
    }

    public void setCalories(String name, int newCalories) throws GordonException {
        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
                recipe.setCalories(newCalories);
                return;
            }
        }

        throw new GordonException(GordonException.NO_RESULT_FOUND);
    }

    public void setTimes(String name, int prepTime, int cookTime) throws GordonException {
        for (Recipe recipe : recipes) {
            // (?i) enables case insensitivity
            // .* uses all characters except line break
            if (recipe.getName().matches("(?i).*" + name + ".*")) {
                recipe.setTimes(prepTime, cookTime);
                return;
            }
        }

        throw new GordonException(GordonException.NO_RESULT_FOUND);
    }

    public void setPrice(String name, float newPrice) throws GordonException {
        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
                recipe.setTotalPrice(newPrice);
                return;
            }
        }

        throw new GordonException(GordonException.NO_RESULT_FOUND);
    }

    public void setDifficulty(String name, Difficulty newDifficulty) throws GordonException {
        for (Recipe recipe : recipes) {
            // (?i) enables case insensitivity
            // .* uses all characters except line break
            if (recipe.getName().matches("(?i).*" + name + ".*")) {
                recipe.setDifficulty(newDifficulty);
                return;
            }
        }

        throw new GordonException(GordonException.NO_RESULT_FOUND);
    }
  

    /////////////////////////// TAGGING FUNCTIONALITIES ///////////////////////////
    public void addCookbookTag(Tag tag) {
        // Prevent duplicate master-Tags at Cookbook level
        if (!doesCookbookTagExists(tag.getTagName())) {
            cookbookTags.add(tag);
        }
    }

    public void deleteCookbookTag(Tag tag) {
        cookbookTags.remove(tag);
    }

    public void appendRecipeToCookbookTag(String tagName, String recipeName) {
        // only if tag already exists in Cookbook
        try {
            Tag extractedTag = extractCookbookTag(tagName);
            extractedTag.addAssociatedRecipeName(recipeName);
        } catch (GordonException e) {
            System.out.println("GordonException: " + e.getMessage());
        }
    }

    public void deleteRecipeFromCookbookTag(String tagName, String recipeName) {
        // only if tag already exists in Cookbook
        try {
            Tag extractedTag = extractCookbookTag(tagName);
            extractedTag.removeAssociatedRecipeName(recipeName);
        } catch (GordonException e) {
            System.out.println("GordonException: " + e.getMessage());
        }
    }

    public void addTagToRecipes(Tag tag) {
        for (Recipe recipe : recipes) {
            // ensure that Tag corresponds to correct recipe
            if (tag.containsAssociatedRecipeNames(recipe.getName())) {
                recipe.addTagToRecipe(tag, recipe.getName(), false);
            }
        }
    }

    public void untagTagFromRecipe(Tag tag, String recipeName) {
        for (Recipe recipe : recipes) {
            // ensure that we fetch the correct Recipe
            // ensure that Tag corresponds to correct recipe
            if (recipe.getName().equalsIgnoreCase(recipeName) && tag.containsAssociatedRecipeNames(recipe.getName())) {
                recipe.deleteTagFromRecipe(tag);
                tag.removeAssociatedRecipeName(recipe.getName());
                return;
            }
        }
    }

    public void deleteTagFromRecipes(Tag tag) {
        for (Recipe recipe : recipes) {
            // ensure that Tag corresponds to correct recipe
            if (tag.containsAssociatedRecipeNames(recipe.getName())) {
                recipe.deleteTagFromRecipe(tag);
                tag.removeAssociatedRecipeName(recipe.getName());
            }
        }
    }

    public String listCookbookTags() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < cookbookTags.size(); i++) {
            output.append(i + 1).append(". ");
            output.append(cookbookTags.get(i).getTagName());
            output.append(System.lineSeparator());
        }
        return output.toString();
    }

    public Tag extractCookbookTag(String tagName) throws GordonException {
        for (Tag tag : cookbookTags) {
            if (tag.getTagName().toLowerCase().contains(tagName.toLowerCase())) {
                return tag;
            }
        }
        throw new GordonException(GordonException.NO_TAG_FOUND);
    }

    public boolean doesCookbookTagExists(String tagName) {
        for (Tag cookbookTag : cookbookTags) {
            if (cookbookTag.getTagName().toLowerCase().contains(tagName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////// FILTER FUNCTIONALITIES ///////////////////////////
    public ArrayList<Recipe> filterByIngredients(ArrayList<String> ingredients) {
        return recipes.stream()
                .filter(r -> r.containsIngredients(ingredients))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByTags(ArrayList<String> tags) {
        return recipes.stream()
                .filter(r -> r.containsTags(tags))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByDifficulty(Difficulty difficulty) {
        return recipes.stream()
                .filter(r -> r.difficulty == difficulty)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByPrice(float price) {
        Comparator<Recipe> compareByPrice = Comparator.comparing(Recipe::getTotalPrice);
        return recipes.stream()
                .filter(r -> r.totalPrice <= price)
                .sorted(compareByPrice.reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByCalories(int cal) {
        Comparator<Recipe> compareByCalories = Comparator.comparing(Recipe::getCalories);
        return recipes.stream()
                .filter(r -> r.calories <= cal)
                .sorted(compareByCalories.reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByTime(int time) {
        Comparator<Recipe> compareByTime = Comparator.comparing(Recipe::getTotalTime);
        return recipes.stream()
                .filter(r -> r.getTotalTime() <= time)
                .sorted(compareByTime.reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
