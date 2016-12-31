package net.codejava.springmvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveDAO {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public static Connection con = null;

	public static void insertReview(int hotel_id, String message) throws SQLException {

		try {
			//loading the driver
			Class.forName(driverName);

			//making the connection
			Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/expedia", "hiveuser", "hiveuser");

			//creating the statement
			Statement stmt = con.createStatement();

			stmt.execute("use expedia");

			System.out.println("Insert data into Hotel Review Kafka");

			// describe table		
			String sql = "INSERT INTO TABLE hotel_review_kafka VALUES (" + hotel_id + ", 3, \"" + message + "\")";

			System.out.println("Running: " + sql);
			stmt.executeQuery(sql);
			System.out.println("Inserted successfully");

		}
		catch(Exception e) {
			System.out.println("Error while executing the query!");
			e.printStackTrace();
		}
		finally {
			if(con != null)
				con.close();
		};
	}
}
