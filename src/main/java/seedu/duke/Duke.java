package seedu.duke;

import java.util.ArrayList;

import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        Recipe recipeA = new Recipe("Caprese Salad");
        recipeA.setTimes(10, 0);
        recipeA.addIngredient(new Ingredient("2 tomatoes", 0.80));
        recipeA.addIngredient(new Ingredient("Fresh mozzarella", 1.50));
        recipeA.addIngredient(new Ingredient("Olive oil", 0.10));
        recipeA.addIngredient(new Ingredient("Salad leaves", 0.20));
        recipeA.addStep("Slice tomatoes and mozzarella into 1 inch slices");
        recipeA.addStep("Lay alternating on a plate");
        recipeA.addStep("Dress with salad leaves");
        recipeA.addStep("Drizzle with olive oil");
        recipeA.setCalories(200);
        recipeA.addTag("Vegan");

        Cookbook cookbook = new Cookbook();
        cookbook.addRecipe(recipeA);

        Recipe recipeB = new Recipe("Ice Lemon Tea");
        recipeB.setTimes(5, 0);
        recipeB.addIngredient(new Ingredient("Half a lemon", 0.20));
        recipeB.addIngredient(new Ingredient("Tea bag", 0.10));
        recipeB.addIngredient(new Ingredient("1 tsp sugar", 0.05));
        recipeB.addIngredient(new Ingredient("Ice cubes", 0));
        recipeB.addStep("Boil water and steep tea bags, adding sugar");
        recipeB.addStep("Add ice to glass");
        recipeB.addStep("Pour tea into glass");
        recipeB.addStep("Dress with lemon slice");
        recipeB.setCalories(50);
        recipeB.addTag("Vegan");
        cookbook.addRecipe(recipeB);

        Recipe recipeC = new Recipe("An apple");
        recipeC.setTimes(5, 0);
        recipeC.addIngredient(new Ingredient("Apple", 1));
        recipeC.setCalories(20);
        recipeC.addTag("Vegan");
        cookbook.addRecipe(recipeC);

        Recipe recipeD = new Recipe("Fried Rice");
        recipeD.setTimes(10, 20);
        recipeD.addIngredient(new Ingredient("Rice", 2));
        recipeD.addIngredient(new Ingredient("Meat", 4));
        recipeD.addIngredient(new Ingredient("Eggs", 1));
        recipeD.addIngredient(new Ingredient("Vegetables", 0.4));
        recipeD.addIngredient(new Ingredient("Oil", 0.1));
        recipeD.addStep("Cut vegetables");
        recipeD.addStep("Add oil to pan on medium low");
        recipeD.addStep("Fry meat for 5 min");
        recipeD.addStep("Add eggs and fry for 2 min");
        recipeD.addStep("And vegetables and saute for 1 min");
        recipeD.setCalories(230);
        cookbook.addRecipe(recipeD);

        cookbook.checkRecipe("ice lemon tea");

        cookbook.sortByPrice();
        System.out.println("Sorted by price: ");
        System.out.println(cookbook);

        cookbook.sortByID();
        System.out.println("Sorted by ID: ");
        System.out.println(cookbook);

        cookbook.sortByCalories();
        System.out.println("Sorted by calories: ");
        System.out.println(cookbook);

        Cookbook filteredCBook = new Cookbook();
        ArrayList<Recipe> filteredR = cookbook.filterByTag("vegan");
        filteredCBook.recipes.addAll(filteredR);
        System.out.println("Recipes containing the 'Vegan' tag: ");
        System.out.println(filteredCBook);


        System.out.println("\n\n\n");
        System.out.println("You can now add your own recipes!");

        Parser parser = new Parser();

        Scanner in = new Scanner(System.in);
        String line  = in.nextLine();
        String nameRecipe;
        ArrayList<String> ingredientsList = null;
        ArrayList<String> stepsList = null;

        while (true) {
            if (parser.parseCommand(line).equalsIgnoreCase("add")) {
                nameRecipe = parser.parseName(line);
                System.out.println("What are the Ingredients for this dish? Input them separated by “+”.");
                line = in.nextLine();
                ingredientsList = parser.parseIngredients(line);
                System.out.println("What are the steps for this dish? Input them separated by “+”.");
                line = in.nextLine();
                stepsList = parser.parseSteps(line);
                System.out.println("Added " + nameRecipe + " recipe! Yum!");
            }
            if (parser.parseCommand(line).equalsIgnoreCase("delete")) {
                nameRecipe = parser.parseName(line);
            }
            if (parser.parseCommand(line).equalsIgnoreCase("check")) {
                nameRecipe = parser.parseName(line);
            }
            if (parser.parseCommand(line).equalsIgnoreCase("exit")) {
                System.out.println("Bye bye!");
                break;
            }
            line = in.nextLine();
        }

    }
}
