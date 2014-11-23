package com.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class CopyOfLogin {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";

	// Database credentials
	static final String USER = "system";
	static final String PASS = "1234";

	public static String UserAccessLevel = "";
	public static String UserId = "";

	public static void main(String[] args) throws Exception {

		Connection conn = null;
		Statement stmt = null;
		Scanner reader = new Scanner(System.in);

		try {
			// STEP 2: Register JDBC driver
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		boolean loop = true;
		do {
			loop = loginScreen(conn, stmt, reader);
			if (!UserAccessLevel.equals("")) {
				String name = "";
				String sql = "select name from users where userid = '" + UserId
						+ "'";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next())
					name = rs.getString("Name");

				System.out.println("Hello " + name);

				switch (UserAccessLevel) {
				case "1":
					student(conn, stmt, reader);
					break;
				case "2":
					TA(conn, stmt, reader);
					break;
				case "3":
					prof(conn, stmt, reader);
					break;
				}
			}
		} while (loop);
		System.out.println("\n\nGoodbye!");
	}// end main

	private static void student(Connection conn, Statement stmt, Scanner reader) {
		// TODO Auto-generated method stub

	}

	private static void TA(Connection conn, Statement stmt, Scanner reader) {
		// TODO Auto-generated method stub

	}

	private static void prof(Connection conn, Statement stmt, Scanner reader)
			throws SQLException {

		int userOption;

		System.out.println("What would you like to do today?");
		System.out
				.println("\n 1. Update TA information \n 2. Blah blah \n 3. Exit");

		do {
			userOption = Integer.parseInt(reader.nextLine());
			if (userOption >= 1 && userOption <= 3)
				break;
			else
				System.out.println("Please enter correct option!");
		} while (true);

		switch (userOption) {
		case 1:
			updateTA(conn, stmt, reader);
			break;
		case 2:
			System.out.println("Case 2");
			break;
		case 3:
			break;
		}

	}

	/**
	 * @param conn
	 * @param stmt
	 * @param reader
	 * @throws SQLException
	 */
	private static boolean loginScreen(Connection conn, Statement stmt,
			Scanner reader) throws SQLException {

		do {
			System.out.println("\n 1. Login \n 2. Create User \n 3. Exit");
			int userOption = Integer.parseInt(reader.nextLine());

			switch (userOption) {
			case 1:
				login(stmt, reader);
				return true;
			case 2:
				createUser(conn, stmt, reader);
				break;
			case 3:
				return false;
			}
			/*
			 * if (userOption == 1) { login(stmt, reader); return true; } else
			 * if (userOption == 2) { createUser(conn, stmt, reader); } else
			 * if(userOption == 3) return false;
			 */
		} while (true);
		
		
	}

	/**
	 * @param stmt
	 * @param reader
	 * @throws SQLException
	 */
	private static void login(Statement stmt, Scanner reader)
			throws SQLException {

		String sql;
		ResultSet rs;
		String pw;

		do {
			System.out.println("Enter the User id:");
			UserId = reader.nextLine();
			System.out.println("Enter the password:");
			pw = reader.nextLine();

			sql = "SELECT USERID, PASSWORD, USERLEVEL, ACCESSLEVEL FROM USERS WHERE USERID ='"
					+ UserId + "' AND PASSWORD ='" + pw + "'";
			// System.out.println(sql);
			rs = stmt.executeQuery(sql);

			if (!rs.next())
				System.out
						.println("Incorrect credentials! Try again? (y/n) : ");
			else {
				UserAccessLevel = rs.getString("accesslevel");
				return;
			}

			char ip = reader.nextLine().charAt(0);

			if (ip == 'n')
				return;

		} while (true);

	}

	/**
	 * @param conn
	 * @param stmt
	 * @param reader
	 * @throws SQLException
	 */
	private static void createUser(Connection conn, Statement stmt,
			Scanner reader) throws SQLException {

		String sql = "";
		String name = "";
		String uId;
		String pwd;
		float ul;

		System.out.println("Enter your Name:");
		name = reader.nextLine();
		System.out.println("Enter your User Id:");
		uId = reader.nextLine();
		System.out.println("Enter the password:");
		pwd = reader.nextLine();

		do {
			System.out
					.println("Enter your study level: 1 (Undergrad) , 2(Grad)");
			ul = Float.parseFloat(reader.nextLine());

			if (ul != 1 && ul != 2)
				System.out.println("Please enter correct value dumbass! \n");
			else
				break;
		} while (true);

		if (ul == 1)
			sql = "INSERT INTO USERS VALUES ('" + name + "', '" + uId + "', '"
					+ pwd + "','Undergrad', '1')";
		else if (ul == 2)
			sql = "INSERT INTO USERS VALUES ('" + name + "', '" + uId + "', '"
					+ pwd + "', 'Grad' , '1')";

		// System.out.println(sql);

		try {
			stmt.executeQuery(sql);
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			// e.printStackTrace();
			System.out
					.println("\n\nUsername already exists, enter different value dude! \n");
			return;
		}
		// }
		// while(loop);

		conn.commit();

		System.out.println("User created successfully!");
	}

	private static void updateTA(Connection conn, Statement stmt, Scanner reader)
			throws SQLException {

		String sql = "";
		ResultSet rs;
		String Id_TA;

		do {
			System.out
					.println("Enter the User ID of the student you want to upgrade to TA:");
			Id_TA = reader.nextLine();
			sql = "select * from users where userid = '" + Id_TA
					+ "' and USERLEVEL = 'Grad' ";
			rs = stmt.executeQuery(sql);
			if (!rs.next()) {
				System.out
						.println("User Id does not exist or user is not a Grad student! Try again? (y/n) : ");
				char ip = reader.nextLine().charAt(0);

				if (ip == 'n')
					return;
			} else
				break;

		} while (true);

		sql = "select course_id from courses where professor_id = '" + UserId
				+ "'";
		rs = stmt.executeQuery(sql);

		int crs;
		System.out
				.println("Enter the Course for which the TA is to be assigned :");

		HashMap<Integer, String> courses = new HashMap<>();
		String courseChosen = "";
		int i = 1;
		while (rs.next()) {
			String courseID = rs.getString("course_id");
			System.out.println("\n" + i + ". " + courseID);
			courses.put(i++, courseID);
		}

		do {
			crs = Integer.parseInt(reader.nextLine());
			courseChosen = courses.get(crs);
			if (courseChosen != null)
				break;
			else
				System.out.println("Invalid choice! Enter again");
		} while (true);

		sql = "update users set accesslevel = '2' where userid = '" + Id_TA
				+ "'";
		stmt.executeQuery(sql);

		try {
			sql = "INSERT INTO TA VALUES ('" + courseChosen + "', '" + Id_TA
					+ "')";
			stmt.executeQuery(sql);
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			System.out.println("\n" + Id_TA + " is already a TA for course "
					+ courseChosen + "!!");
			return;
		}

		System.out.println("\n" + Id_TA
				+ " was successfully made TA for course " + courseChosen);
	}

}
