import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Category {
  private int id;
  private String type;

  public Category(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public int getId() {
    return id;
  }

  public static List<Category> all() {
    String sql = "SELECT id, type FROM categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  @Override
  public boolean equals(Object passedInCategory) {
    if(!(passedInCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) passedInCategory;
      return this.getType().equals(newCategory.getType());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories (type) VALUES (:type)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("type", this.type)
      .executeUpdate()
      .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories WHERE id=:id";
      Category category = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Category.class);
    return category;
    }
  }


    public void update(String newCategory) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE categories SET type = :type WHERE id = :id";
        con.createQuery(sql)
          .addParameter("type", newDescription)
          .addParameter("id", this.id)
          .executeUpdate();
      }
    }

    public void delete() {
      try(Connection con = DB.sql2o.open()) {
        String deleteQuery = "DELETE FROM categories WHERE id = :id;";
          con.createQuery(deleteQuery)
            .addParameter("id", this.getId())
            .executeUpdate();

        String joinDeleteQuery = "DELETE FROM categories_recipes WHERE category_id = :categoryId";
          con.createQuery(joinDeleteQuery)
            .addParameter("taskId", this.getId())
            .executeUpdate();
      }
    }



  public void addRecipe(Recipe recipe) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_recipes (category_id, recipe_id) VALUES (:category_id, :recipe_id)";
      con.createQuery(sql)
      .addParameter("category_id", this.getId())
      .addParameter("recipe_id", recipe.getId())
      .executeUpdate();
    }
  }

  public List<Recipe> getRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT recipe_id FROM categories_recipes WHERE category_id = :category_id";
      List<Integer> recipeIds = con.createQuery(sql)
      .addParameter("category_id", this.getId())
      .executeAndFetch(Integer.class);

      List<Recipe> recipes = new ArrayList<Recipe>();

      for(Integer recipeId : recipeIds) {
        String sql2 = "SELECT * FROM recipes WHERE id = :recipeId";
        Recipe recipe = con.createQuery(sql2)
        .addParameter("recipeId", recipeId)
        .executeAndFetchFirst(Recipe.class);
        recipes.add(recipe);
      }
      return recipes;
    }
  }


}
