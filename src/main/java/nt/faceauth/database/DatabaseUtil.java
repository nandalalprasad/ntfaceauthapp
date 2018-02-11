package nt.faceauth.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
	
	static Connection connection = null;
	
	public static Connection createConnection() {
		if(connection != null) {
			return connection;
		}
		return getConnection();
	}
	
	private static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/facerecog?useSSL=false", 
					"root", "password");
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
