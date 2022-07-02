package db;

import java.sql.*;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.*;
import org.jooq.util.mysql.*;

public class Ingredients {

	public static String getFood() {
		try {
			Connection conn = ConnectDb.getConnection();
			DSLContext context = DSL.using(conn, SQLDialect.MYSQL);
			Statement stmt = ConnectDb.getConnection().createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM ingredients");
			return DSL.using(conn, SQLDialect.MYSQL).fetch(rs).formatJSON();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return "";
		}
	}
}
