package seedu.duke;

import java.util.ArrayList;

public class Parser {

    protected ArrayList<String> ingredientsList;
    protected ArrayList<String> stepsList;



    public String parseCommand(String line) {
        int spaceIndex = line.indexOf(" ");
        return spaceIndex == -1 ? line : line.substring(0, spaceIndex);
    }

    public String parseName(String line) {
        int spaceIndex = line.indexOf(" ");
        String name = line.substring(spaceIndex);

        return spaceIndex == -1 ? line : name;
    }



    public ArrayList<String> parseIngredients(String line) {
        String[] ingredientsString = line.split("\\+");
        for (int i = 0; i < ingredientsString.length; i++) {
            ingredientsList.add(ingredientsString[i]);
        }
        return ingredientsList;
    }

    public ArrayList<String> parseSteps(String line) {
        String[] stepsString = line.split("\\+");
        for (int i = 0; i < stepsString.length; i++) {
            stepsList.add(stepsString[i]);
        }
        return stepsList;
    }


}
