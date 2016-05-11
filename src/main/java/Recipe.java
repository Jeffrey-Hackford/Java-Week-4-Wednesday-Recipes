import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Recipe {
  private String name;
  private int id;
  private String ingredients;
  private String instructions;

  public Recipe(String name, String ingredients, String instructions) {
    this.ingredients = ingredients;
    this.name = name;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }

  public String getIngredients() {
    return ingredients;
  }

  public String getInstructions() {
    return instructions;
  }

  public int getId() {
    return id;
  }

  public static List<Recipe> all() {
    String sql = "SELECT * FROM recipes";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (ingredients, instructions) VALUES (:ingredients, :instructions)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("ingredients", this.ingredients)
      .addParameter("instructions", this.instructions)
      .executeUpdate()
      .getKey();
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE ingredients=:ingredients AND instructions=:instructions";
      Recipe recipe = con.createQuery(sql)
      .addParameter("ingredients", ingredients)
      .addParameter("instructions", instructions)
      .executeAndFetchFirst(Recipe.class);
      return recipe;
    }
  }

  public void addCategory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_recipes (category_id, recipes_id) VALUES (:category_id, :recipe_id)";
      con.creatQuery(sql)
      .addParameter("category_id", category.getId())
      .addParameter("recipe_id", this.getId())
      .executeUpdate();
    }
  }

    public List<Category> getCategories() {
      try(Connection con = DB.sql2o.open()){
        String joinQuery = "SELECT category_id FROM categories_recipes WHERE recipe_id = :recipe_id";
        List<Integer> categoryIds = con.createQuery(joinQuery)
          .addParameter("recipe_id", this.getId())
          .executeAndFetch(Integer.class);

        List<Category> categories = new ArrayList<Category>();

        for (Integer categoryId : categoryIds) {
          String recipeQuery = "SELECT * FROM categories WHERE id = :categoryId";
          Category category = con.createQuery(recipeQuery)
            .addParameter("categoryId", categoryId)
            .executeAndFetchFirst(Category.class);
          categories.add(category);
        }
        return categories;
      }
    }
}
