package gordon.kitchen;

import gordon.exception.GordonException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Cookbook {
    protected ArrayList<Recipe> recipes;

    public Cookbook() {
        recipes = new ArrayList<>();
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

    public void addRecipe(Recipe r) throws GordonException {
        try {
            for (Recipe recipe: recipes) {
                if (recipe.getName().equals(r.getName())) {
                    throw new GordonException(GordonException.DUPLICATE_RECIPE_NAME);
                }
            }
            r.setId(recipes.size() + 1);
            recipes.add(r);
        } catch (IndexOutOfBoundsException e) {
            throw new GordonException(GordonException.INDEX_OOB);
        } catch (IllegalArgumentException e) {
            throw new GordonException(GordonException.INDEX_INVALID);
        }
    }

    public void removeRecipe(int index) throws GordonException {
        try {
            recipes.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new GordonException(GordonException.INDEX_OOB);
        }
    }

    public void checkRecipe(int index) throws GordonException {
        try {
            System.out.println(recipes.get(index).toString());
        } catch (IndexOutOfBoundsException e) {
            throw new GordonException(GordonException.INDEX_OOB);
        } catch (IllegalArgumentException e) {
            throw new GordonException(GordonException.INDEX_INVALID);
        }
    }

    public void checkRecipe(String name) throws GordonException {
        System.out.println("Finding recipes called " + name + ".....");
        boolean isFound = false;
        for (Recipe recipe : recipes) {
            // (?i) enables case insensitivity
            // .* uses all characters except line break
            if (recipe.getName().matches("(?i).*" + name + ".*")) {
                isFound = true;
                System.out.println("--------------------");
                System.out.print(recipe);
                System.out.println("--------------------");
            }
        }

        if (!isFound) {
            throw new GordonException(GordonException.NO_RESULT_FOUND);
        }
    }

    public void sortByID() {
        Comparator<Recipe> compareByID = Comparator
                .comparing(Recipe::getID);
        recipes = recipes.stream()
                .sorted(compareByID)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void sortByPrice() {
        Comparator<Recipe> compareByPrice = Comparator.comparing(Recipe::getTotalPrice)
                .thenComparing(Recipe::getID);
        recipes = recipes.stream()
                .sorted(compareByPrice)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void sortByCalories() {
        Comparator<Recipe> compareByCalories = Comparator.comparing(Recipe::getCalories)
                .thenComparing(Recipe::getID);
        recipes = recipes.stream()
                .sorted(compareByCalories)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Recipe> filterByTag(String tag) {
        Comparator<Recipe> compareByID = Comparator
                .comparing(Recipe::getID);
        return recipes.stream()
                .filter(r -> r.getTags().contains(tag.toLowerCase()))
                .sorted(compareByID)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
