package db.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jooq.JSONFormat;
import org.jooq.SQLDialect;
import org.jooq.JSONFormat.RecordFormat;
import org.jooq.impl.DSL;

import com.google.gson.Gson;

public class IngredientSQL {

	private final String TABLE_NAME = "ingredients";
	private final String COLUMN_1 = "id";
	private final String COLUMN_2 = "ingredient_name";
	private final String ALPHABET_P = "^[a-zA-Z ]*$";
	private final String DIGITS_P = "^[0-9]{1,5}$";

	private Connection conn = db.ConnectDb.getConnection();

	public String create(String name) {
		if (wrongName(name))
			return "";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					String.format("REPLACE INTO %s VALUES (%s, '%s')", TABLE_NAME, "NULL", name));
			ResultSet rs = stmt.executeQuery(String.format("SELECT id FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_2, name));
			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public String createMany(String[] names) {
		List<String> list = new ArrayList<String>();
		Gson g = new Gson();

		for (String name : names) {
			list.add(create(name));
		}
		return g.toJson(list);
	}

	public String read(String id) {
		if (wrongId(id))
			return "";

		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("SELECT * FROM %s WHERE %s=%s", TABLE_NAME, COLUMN_1, id);
			ResultSet rs = stmt.executeQuery(sql);

			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));
		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public String readMany(int limit, int offset) {
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("SELECT * FROM %s ORDER BY %s %s", TABLE_NAME, COLUMN_1,
					limit > 0 ? "LIMIT " + limit + " OFFSET " + offset : "");
			ResultSet rs = stmt.executeQuery(sql);

			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public void update(String id, String name) {
		if (wrongId(id) || wrongName(name))
			return;
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", TABLE_NAME, COLUMN_2, name, COLUMN_1, id);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void updateMany(HashMap<String, String> idNameMap) {
		for (String key : idNameMap.keySet()) {
			String value = idNameMap.get(key);
			if (wrongId(key) || wrongName(value))
				continue;
			update(key, value);
		}
	}

	public void delete(String id) {
		if (wrongId(id))
			return;
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("DELETE FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_1, id);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void deleteMany(String[] ids) {
		for (String id : ids) {
			if (wrongId(id))
				continue;
			delete(id);
		}
	}

	private boolean wrongId(String id) {
		if (id.matches(DIGITS_P) != true) {
			System.out.println("Incorrect id " + id);
			return true;
		}
		return false;
	}

	private boolean wrongName(String name) {
		if (name.matches(ALPHABET_P) != true) {
			System.out.println("Incorrect name " + name);
			return true;
		}
		return false;
	}

}
