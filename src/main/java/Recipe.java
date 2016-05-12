import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Recipe {
  private int id;
  private int rating;
  private String name;
  private String ingredients;
  private String instructions;

  public Recipe(String name, String ingredients, String instructions, Integer rating) {
    this.name = name;
    this.ingredients = ingredients;
    this.instructions = instructions;
    this.rating = rating;
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

  public int getRating() {
    return rating;
  }

  public static List<Recipe> all() {
    String sql = "SELECT * FROM recipes";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (name, ingredients, instructions, rating) VALUES (:name, :ingredients, :instructions, :rating)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("ingredients", this.ingredients)
      .addParameter("instructions", this.instructions)
      .addParameter("rating", this.rating)
      .executeUpdate()
      .getKey();
    }
  }

  @Override
  public boolean equals(Object passedInRecipe) {
    if(!(passedInRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) passedInRecipe;
      return this.getName().equals(newRecipe.getName());
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id=:id";
      return con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Recipe.class);
    }
  }

  public void addCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_recipes (category_id, recipe_id) VALUES (:category_id, :recipe_id)";
      con.createQuery(sql)
      .addParameter("recipe_id", this.getId())
      .addParameter("category_id", category.getId())
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
