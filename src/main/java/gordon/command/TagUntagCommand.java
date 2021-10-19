package gordon.command;

import gordon.exception.GordonException;
import gordon.kitchen.Cookbook;
import gordon.util.Tag;

public class TagUntagCommand extends Command {
    private String recipeName;
    private String[] tagNames;

    public TagUntagCommand(String recipeName, String[] tagNames) {
        this.recipeName = recipeName;
        this.tagNames = tagNames;
    }

    @Override
    public void execute(Cookbook cookbook) {
        try {
            cookbook.isRecipeExist(recipeName);    // Recipe must exist to be untagged
            for (String tagName : tagNames) {
                Tag extractedTag = cookbook.extractTag(tagName.trim());       // Tag must exist to be untagged
                cookbook.deleteRecipeTag(extractedTag);                       // remove tag from Recipe
                cookbook.removeRecipeFromCookbookTag(tagName, recipeName);    // modify master-Tag in Cookbook
                System.out.println("Successfully untagged " + recipeName + " under " + tagName.trim());
            }
        } catch (GordonException e) {
            System.out.println("GordonException: " + e.getMessage());
        }
    }
}