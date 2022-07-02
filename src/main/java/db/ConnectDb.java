package db;

import java.sql.*;

public class ConnectDb {
	static Connection connection = null;

	public static Connection getConnection() {
		if (connection == null) {
			String db = server.Config.getProp("db_name");
			String user = server.Config.getProp("db_user");
			String pass = server.Config.getProp("db_pass");
			return getConnection(db, user, pass);
		}
		return connection;
	}

	private static Connection getConnection(String db, String user, String pass) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + db, user, pass);
			return connection;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}
}
