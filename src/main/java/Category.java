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
    String sql = "SELECT * FROM categories";
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
      con.createQuery(sql, true)
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


}
