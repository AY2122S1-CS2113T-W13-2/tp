package gordon.kitchen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IngredientTest {
    @Test
    public void getDescriptionTest() {
        Ingredient i = new Ingredient("Salt");
        assertEquals("Salt", i.getDescription());
    }
}