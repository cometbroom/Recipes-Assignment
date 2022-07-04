package db.sql;

import java.sql.*;
import java.util.List;

import org.jooq.JSONFormat;
import org.jooq.SQLDialect;
import org.jooq.JSONFormat.RecordFormat;
import org.jooq.impl.DSL;

public class TwoColumnSQL {

	protected final String ALPHABET_P = "^[a-zA-Z ]*$";
	protected final String DIGITS_P = "^[0-9]{1,5}$";
	protected String TABLE_NAME;
	protected String COLUMN_1;
	protected String COLUMN_2;
	protected Connection conn = db.ConnectDb.getConnection();

	public TwoColumnSQL(String table, String col1, String col2) {
		TABLE_NAME = table;
		COLUMN_1 = col1;
		COLUMN_2 = col2;
	}

	public String create(String name) {
		if (wrongName(name))
			return "";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					String.format("INSERT INTO %s VALUES (%s, '%s')", TABLE_NAME, "NULL", name));
			String sql2 = String.format("SELECT id FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_2, name);
			ResultSet rs = stmt.executeQuery(sql2);
			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	public String createMany(List<String> names) {
		String list = "[";

		for (String name : names) {
			list += create(name);
			list += ",";
		}
		list += "]";
		return list;
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

	public String update(String id, String name) {
		if (wrongId(id) || wrongName(name))
			return "";
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", TABLE_NAME, COLUMN_2, name, COLUMN_1, id);
			stmt.executeUpdate(sql);
			String sql2 = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_1, id);
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
			String sqlName = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_1, id);
			ResultSet rs = stmt.executeQuery(sqlName);
			String jsonRepresentation = DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));
			String sql = String.format("DELETE FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_1, id);
			stmt.executeUpdate(sql);
			return jsonRepresentation;
		} catch (SQLException e) {
			System.out.println(e);
			return "";
		}
	}

	protected boolean wrongId(String id) {
		if (id.matches(DIGITS_P) != true) {
			System.out.println("Incorrect id " + id);
			return true;
		}
		return false;
	}

	protected boolean wrongName(String name) {
		if (name.matches(ALPHABET_P) != true) {
			System.out.println("Incorrect name " + name);
			return true;
		}
		return false;
	}

}
