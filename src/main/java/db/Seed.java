package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Seed {
	private static final String INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS ingredients (" +
			"id integer auto_increment primary key," +
			"ingredient_name varchar(25));";

	private static final String RECIPES_TABLE = "CREATE TABLE IF NOT EXISTS recipes (" +
			"id integer auto_increment primary key," +
			"recipe_name varchar(25));";

	private static final String RECIPE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
			"recipe_id int," +
			"ingredient_id int," +
			"constraint FOREIGN KEY(recipe_id) references recipes(id) on update cascade on delete cascade," +
			"constraint FOREIGN KEY(ingredient_id) references ingredients(id) on update cascade on delete cascade," +
			"constraint PRIMARY KEY(recipe_id, ingredient_id));";

	public static void run() {
		Connection conn = ConnectDb.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(INGREDIENTS_TABLE);
			stmt.executeUpdate(RECIPES_TABLE);
			stmt.executeUpdate(RECIPE_INGREDIENTS_TABLE);
		} catch (SQLException e) {
			System.out.println(e);
		}

	}
}
