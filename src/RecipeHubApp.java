import controller.RecipeController;
import model.RecipeModel;

public class RecipeHubApp {
    public static void main(String[] args) {
        RecipeModel model = new RecipeModel();
        RecipeController controller = new RecipeController(model);
        controller.start();
    }
}