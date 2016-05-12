import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipelist", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String inputtedRecipeName = request.queryParams("recipeName");
      String inputtedRecipeIngredients = request.queryParams("recipeIngredients");
      String inputtedRecipeInstructions = request.queryParams("recipeInstructions");
      Integer inputtedRecipeRating = Integer.parseInt(request.queryParams("recipeRating"));
      Recipe newRecipe = new Recipe(inputtedRecipeName, inputtedRecipeIngredients,  inputtedRecipeInstructions, inputtedRecipeRating);
      newRecipe.save();
      model.put("newRecipe", newRecipe);
      model.put("recipes", Recipe.all());
      model.put("template", "templates/recipelist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipe/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("id")));
      model.put("recipe", recipe);
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
