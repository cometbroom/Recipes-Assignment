package db;

import java.sql.*;
import org.jooq.JSONFormat;
import org.jooq.SQLDialect;
import org.jooq.JSONFormat.RecordFormat;
import org.jooq.impl.DSL;

public class Ingredients {

	public static String getIngredients() {
		try {
			Connection conn = ConnectDb.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ingredients");

			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs)
					.formatJSON(new JSONFormat().header(false).recordFormat(RecordFormat.OBJECT));

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return "";
		}
	}
}
