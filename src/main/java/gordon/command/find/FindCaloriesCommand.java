package gordon.command.find;

import gordon.command.Command;
import gordon.kitchen.Cookbook;
import gordon.kitchen.Recipe;

import java.util.ArrayList;

public class FindCaloriesCommand extends Command {
    int calories;

    public FindCaloriesCommand(int calories) {
        this.calories = calories;
    }

    @Override
    public void execute(Cookbook cookbook) {
        System.out.println("Searching by calories...");
        ArrayList<Recipe> calFilter = cookbook.filterByCalories(calories);
        for (int i = 0; i < calFilter.size(); i++) {
            int calGet = calFilter.get(i).getCalories();
            if (calGet > 0) {
                System.out.println((i + 1) + ". " + calFilter.get(i).getName()
                        + " (Calories (kcal): " + calGet + ")");
            } else {
                System.out.println((i + 1) + ". " + calFilter.get(i).getName()
                        + " (Calories (kcal): Not set)");
            }
        }
    }
}
