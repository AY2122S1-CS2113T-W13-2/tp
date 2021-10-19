package gordon.util;

import gordon.command.AddCommand;
import gordon.command.CheckCommand;
import gordon.command.Command;
import gordon.command.DeleteCommand;
import gordon.command.FindIngredientsCommand;
import gordon.command.HelpCommand;
import gordon.command.ListCommand;
import gordon.command.NullCommand;
import gordon.command.SetCaloriesCommand;
import gordon.command.SetDifficultyCommand;
import gordon.exception.GordonException;
import gordon.kitchen.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {
    protected String nameRecipe;
    protected String line;
    protected Scanner in;

    public static final int NAME_INDEX = 0;
    public static final int INGREDIENTS_INDEX = 1;
    public static final int STEPS_INDEX = 2;
    public static final int CALORIES_INDEX = 3;
    public static final int INGREDIENTS_WORD_LENGTH = 12;
    public static final int STEPS_WORD_LENGTH = 6;

    public Parser() {
        in = new Scanner(System.in);
    }

    public Command parseMaster() {
        try {
            if (parseCommand(line).equalsIgnoreCase("add")) {
                return addParse();
            } else if (parseCommand(line).equalsIgnoreCase("delete")) {
                return deleteParse();
            } else if (parseCommand(line).equalsIgnoreCase("check")) {
                return new CheckCommand(parseName(line));
            } else if (parseCommand(line).equalsIgnoreCase("list")) {
                return new ListCommand();
            } else if (parseCommand(line).equalsIgnoreCase("set")) {
                return setParse();
            } else if (parseCommand(line).equalsIgnoreCase("find")) {
                return findParse();
            } else if (parseCommand(line).equalsIgnoreCase("help")) {
                return new HelpCommand();
            } else {
                throw new GordonException(GordonException.COMMAND_INVALID);
            }
        } catch (GordonException e) {
            System.out.println("GordonException: " + e.getMessage());
        }
        return new NullCommand();
    }

    public boolean parseNextLine() {
        line = in.nextLine();
        return !line.trim().equalsIgnoreCase("exit");
    }

    public String parseCommand(String line) throws GordonException {
        int spaceIndex = line.indexOf(" ");
        return (spaceIndex == -1) ? line : line.substring(0, spaceIndex);
    }

    public String parseName(String line) throws GordonException {
        int spaceIndex = line.indexOf(" ");

        if (spaceIndex == -1) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }

        return line.substring(spaceIndex).trim();
    }

    public void parseIngredients(String line, Recipe r) throws GordonException {
        int ingredientsIndex = line.indexOf("ingredients");
        if (ingredientsIndex == -1) {
            throw new GordonException(GordonException.INGREDIENTS_FORMAT);
        }
        String newLine = line.substring(ingredientsIndex + INGREDIENTS_WORD_LENGTH);
        String[] ingredientsList = newLine.split("\\+");
        for (int i = 0; i < ingredientsList.length; i++) {
            r.addIngredient(ingredientsList[i].trim());
        }
    }

    public void parseSteps(String line, Recipe r) throws GordonException {
        int stepsIndex = line.indexOf("steps");
        if (stepsIndex == -1) {
            throw new GordonException(GordonException.STEPS_FORMAT);
        }
        String newLine = line.substring(stepsIndex + STEPS_WORD_LENGTH);
        String[] stepsList = newLine.split("\\+");
        for (int i = 0; i < stepsList.length; i++) {
            r.addStep(stepsList[i], i);
        }
    }

    public void parseCalories(String line, Recipe r) throws GordonException {
        String[] input = line.split("calories");

        try {
            int calories = Integer.parseInt(input[1].trim());
            r.setCalories(calories);
        } catch (IndexOutOfBoundsException e) {
            throw new GordonException(GordonException.EMPTY_CALORIES);
        }
    }

    public AddCommand addParse() throws GordonException {
        String[] splitContent = line.split("/");
        if (splitContent.length < 4) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        assert splitContent.length == 4 : "Your add input should have exactly 3 '/' separating them.";
        Recipe r = new Recipe(parseName(splitContent[NAME_INDEX]));
        parseIngredients(splitContent[INGREDIENTS_INDEX], r);
        parseSteps(splitContent[STEPS_INDEX], r);
        parseCalories(splitContent[CALORIES_INDEX], r);
        return new AddCommand(r);
    }

    public DeleteCommand deleteParse() throws GordonException {
        nameRecipe = parseName(line);
        String inputIndex = line.contains(" ") ? line.substring(line.indexOf(" ") + 1) : " ";
        if (inputIndex.isEmpty() || inputIndex.equals(" ")) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        try {
            int index = Integer.parseInt(inputIndex);
            assert index > 0 : "Your input should be a number greater than 0";
            return new DeleteCommand(index - 1);
        } catch (NumberFormatException e) {
            throw new GordonException(GordonException.INDEX_INVALID);
        }
    }

    public Command setParse() throws GordonException {
        String[] splitContent = line.split("/");
        if (splitContent.length < 2) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        String recipeName = parseName(splitContent[0]);
        int spaceIndex = splitContent[1].indexOf(' ');
        if (spaceIndex < 0) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        String target = splitContent[1].substring(0, spaceIndex);
        switch (target) {
        case "calories":
            try {
                int index = Integer.parseInt(splitContent[1].substring(spaceIndex + 1).trim());
                if (index < -1) {
                    throw new GordonException(GordonException.INDEX_OOB);
                }
                return new SetCaloriesCommand(recipeName, index);
            } catch (NumberFormatException e) {
                throw new GordonException(GordonException.INDEX_INVALID);
            }
        case "difficulty":
            try {
                Difficulty newDifficulty = null;
                String difficultyString = splitContent[1].substring(spaceIndex + 1).trim();
                for (Difficulty d : Difficulty.values()) {
                    if (d.name().equalsIgnoreCase(difficultyString)) {
                        newDifficulty = d;
                    }
                }
                if (newDifficulty == null) {
                    throw new GordonException(GordonException.INVALID_DIFFICULTY);
                } else {
                    return new SetDifficultyCommand(recipeName, newDifficulty);
                }
            } catch (NumberFormatException e) {
                throw new GordonException(GordonException.INDEX_INVALID);
            }
        default:
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
    }

    public Command findParse() throws GordonException {
        String[] splitContent = line.split("/");
        if (splitContent.length < 2) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        int spaceIndex = splitContent[1].indexOf(' ');
        if (spaceIndex < 0) {
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
        String target = splitContent[1].substring(0, spaceIndex);
        switch (target) {
        case "ingredients":
            ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(splitContent[1]
                    .substring(spaceIndex + 1).split("\\+")));
            return new FindIngredientsCommand(ingredients);
        case "tags":
        default:
            throw new GordonException(GordonException.COMMAND_INVALID);
        }
    }
}
