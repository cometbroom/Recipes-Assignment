package db.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.JSONFormat;
import org.jooq.SQLDialect;
import org.jooq.JSONFormat.RecordFormat;
import org.jooq.impl.DSL;

public class RecipeIngredientsSQL extends TwoColumnSQL {

	public RecipeIngredientsSQL(String table, String col1, String col2) {
		super(table, col1, col2);
	}

	public String create(String name, String[] ingredients) {
		if (wrongName(name) || wrongIngredients(ingredients))
			return "";
		try {
			Statement stmt = conn.createStatement();
			String sql2 = createProcedure(name, ingredients, stmt);
			ResultSet rs = stmt.executeQuery(sql2);
			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	@Override
	public String read(String id) {
		if (wrongId(id))
			return "";

		try {
			Statement stmt = conn.createStatement();
			String sql = String.format(
					"SELECT recipe_name, ingredient_name FROM %s " +
							"JOIN recipes AS r ON recipe_id = r.id " +
							"JOIN ingredients AS i ON ingredient_id = i.id " +
							"WHERE %s=%s",
					TABLE_NAME, COLUMN_1, id);
			ResultSet rs = stmt.executeQuery(sql);

			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));
		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	@Override
	public String readMany(int limit, int offset) {
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format(
					"SELECT recipe_name, ingredient_name FROM %s " +
							"JOIN recipes AS r ON recipe_id = r.id " +
							"JOIN ingredients AS i ON ingredient_id = i.id " +
							"ORDER BY %s %s",
					TABLE_NAME, COLUMN_1,
					limit > 0 ? "LIMIT " + limit + " OFFSET " + offset : "");
			ResultSet rs = stmt.executeQuery(sql);

			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public String update(String id, String[] ingredients, boolean replace) {
		if (wrongId(id) || wrongIngredients(ingredients))
			return "";
		try {
			Statement stmt = conn.createStatement();
			String ingredientsFormat = getIngredientsSQLFormat(ingredients);
			if (replace)
				stmt.executeUpdate(String.format("DELETE FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_1, id));
			String sql = String.format(
					"REPLACE INTO %s " +
							"SELECT r.id, i.id " +
							"FROM recipes AS r JOIN ingredients AS i " +
							"WHERE r.id = %s AND ingredient_name IN %s",
					TABLE_NAME, id, ingredientsFormat);

			stmt.executeUpdate(sql);
			String sql2 = String.format(
					"SELECT r.id, i.ingredient_name FROM %s JOIN recipes AS r ON recipe_id = r.id JOIN ingredients AS i ON ingredient_id = i.id WHERE r.id = %s",
					TABLE_NAME, id);
			ResultSet rs = stmt.executeQuery(sql2);
			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public String delete(String id) {
		if (wrongId(id))
			return "";
		try {
			Statement stmt = conn.createStatement();
			String sqlName = String.format("SELECT * FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_1, id);
			ResultSet rs = stmt.executeQuery(sqlName);
			String jsonRepresentation = DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

			String sql = String.format("DELETE FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_1, id);
			stmt.executeUpdate(sql);
			return jsonRepresentation;
		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	private String createProcedure(String name, String[] ingredients, Statement stmt) throws SQLException {
		String sqlWrite1 = String.format("INSERT IGNORE INTO %s VALUES (%s, '%s')", TABLE_NAME, "NULL", name);
		stmt.executeUpdate(sqlWrite1);
		String ingredientsFormat = getIngredientsSQLFormat(ingredients);
		String sqlWrite2 = getCreateRISql(name, ingredientsFormat);
		stmt.executeUpdate(sqlWrite2);
		String sql2 = getCheckUpdateSql(name);
		return sql2;
	}

	private String getCheckUpdateSql(String name) {
		String sql2 = String.format(
				"SELECT recipe_id, ingredient_id " +
						"FROM %s JOIN recipes AS r ON recipe_id = r.id " +
						"JOIN ingredients AS i ON ingredient_id = i.id " +
						"WHERE recipe_name = '%s'",
				TABLE_NAME, name);
		return sql2;
	}

	private String getCreateRISql(String name, String ingredientsFormat) {
		String sqlWrite2 = String.format(
				"INSERT INTO %s " +
						"SELECT recipes.id, ingredients.id " +
						"FROM recipes JOIN ingredients " +
						"WHERE recipe_name = '%s' AND ingredient_name IN %s",
				TABLE_NAME, name, ingredientsFormat);
		return sqlWrite2;
	}

	private String getIngredientsSQLFormat(String[] ingredients) {
		String format = "(";

		for (int i = 0; i < ingredients.length; i++) {
			format += String.format("'%s'", ingredients[i]);
			if (i < ingredients.length - 1)
				format += ", ";
		}
		format += ")";
		return format;
	}

	private boolean wrongIngredients(String[] ingredients) {
		for (String ingredient : ingredients) {
			if (wrongName(ingredient))
				return true;
		}
		return false;
	}

}
