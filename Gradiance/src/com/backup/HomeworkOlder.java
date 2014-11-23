package com.backup;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;

class HomeworkOlder {

	private String courseid;
	private Scanner reader;
	private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "gaurav";
	private static final String PASS = "gaurav38";
	private Connection conn;
	private Statement stmt;
	private static Date currentdate;
	private static Calendar currDtCal;
	
	public HomeworkOlder(String courseid) throws IOException {
		this.courseid = courseid;
		reader = new Scanner(System.in);
		conn = null;
		stmt = null;
		currDtCal = Calendar.getInstance();
	    currDtCal.set(Calendar.HOUR_OF_DAY, 0);
	    currDtCal.set(Calendar.MINUTE, 0);
	    currDtCal.set(Calendar.SECOND, 0);
	    currDtCal.set(Calendar.MILLISECOND, 0);
	    currentdate = currDtCal.getTime();
	    SetupDatabaseConnections();
		DisplayCourseMenu();
	}

	private void SetupDatabaseConnections() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void DisplayCourseMenu() throws IOException {
		// TODO Auto-generated method stub
		int choice;
		while(true) {
			Utility.ClearScreen();
			System.out.println("======================================================================");
			System.out.println("For "+courseid);
			System.out.println("======================================================================");
			System.out.println("1. Add homework");
			System.out.println("2. Add/Remove questions to homework");
			System.out.println("3. Edit homework");
			System.out.println("4. View homework");
			System.out.println("5. View Notification");
			System.out.println("6. Reports");
			System.out.println("7. Back");
			System.out.println("======================================================================");
			System.out.println("Enter choice:");
			choice = reader.nextInt();
			reader.nextLine();
			
			while(choice > 7 || choice <= 0) {
				System.out.println("Enter a valid choice");
				choice = reader.nextInt();
				reader.nextLine();
			}
			
			if(choice == 1) AddHomework();
			else if(choice == 2) AddRemoveQuestions();
			else if(choice == 3) EditHomework();
			else if(choice == 4) ViewHomework();
			else if(choice == 5) ViewNotification();
			else if(choice == 6) Reports();
			else break;
		} 
		reader.close();
	}

	private void Reports() {
		// TODO Auto-generated method stub
		
	}

	private void ViewNotification() {
		// TODO Auto-generated method stub
		
	}

	private void ViewHomework() {
		// TODO Auto-generated method stub
		int rows = 0; int option = 0; int counter = 0;
		String sql;
		try {
			ResultSet rs = null;
			
			do {
				sql = "SELECT * FROM ASSESSMENT WHERE COURSE_ID = '" + courseid + "'";
				rs = stmt.executeQuery(sql);
				rows = Utility.GetResultSetNumRows(rs);
				if(rows == 0) {
					System.out.println("No questions found for this criteria");
					return;
				}
				counter = 1;
				option = DisplayHomeworkMenu(rs, rows);
				if(option <= rows) {
					counter = 1;
					rs.beforeFirst();
					while(rs.next()) {
						if(counter == option) {
							//Modify homework parameteres here
							Utility.ClearScreen();
							System.out.println("======================================================================");
							System.out.println("Here are the details of the homework "+ rs.getString("EXERCISE_ID"));
							System.out.println("======================================================================");
							System.out.println("Start date = "+ Utility.GetFormatDate(rs.getDate("START_DATE")));
							System.out.println("End date = "+ Utility.GetFormatDate(rs.getDate("END_DATE")));
							System.out.println("Topic = "+ rs.getString("TOPIC"));
							System.out.println("Score Selection Method = "+ rs.getString("SCORE_SELECTION_METHOD"));
							System.out.println("Number of attempts allowed = " + rs.getInt("RETRIES"));
							System.out.println("Difficulty = " + rs.getString("DIFFICULTY"));
							System.out.println("Points per correct answer = " + rs.getInt("POINTS_CORRECT"));
							System.out.println("Points per incorect answer = " + rs.getInt("POINTS_WRONG"));
							int question_count = 1;
							String[] questions = rs.getString("QUESTIONS_LIST").split(",");
							System.out.println("\nHere are the questions:");
							for(String q : questions) {
								sql = "SELECT TEXT FROM QUESTIONS WHERE QUESTION_ID = '" + q + "'";
								ResultSet temp = stmt.executeQuery(sql);
								if(temp.next()) {
									System.out.println(question_count + ") " + temp.getString("TEXT"));
									temp.close();
								}
							}
							System.out.println("======================================================================");
							System.out.println("Press Enter to continue");
							reader.nextLine();
							break;
						}
						counter++;
					}
				}
			} while(option != (rows+1));
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}
	}

	private void EditHomework() {
		// TODO Auto-generated method stub
		int rows = 0; int option = 0; int counter = 0;
		try {
			String current = Utility.GetFormatDate(currentdate);
			String sql = "SELECT EXERCISE_ID, START_DATE FROM ASSESSMENT WHERE END_DATE > '" 
						 + current + "' AND COURSE_ID = '" + courseid + "'";
			ResultSet rs = null;
			
			do {
				rs = stmt.executeQuery(sql);
				rows = Utility.GetResultSetNumRows(rs);
				if(rows == 0) {
					System.out.println("No questions found for this criteria");
					return;
				}
				counter = 1;
				option = DisplayHomeworkMenu(rs, rows);
				if(option <= rows) {
					counter = 1;
					rs.beforeFirst();
					while(rs.next()) {
						if(counter == option) {
							//Modify homework parameteres here
							EditHomeworkParameters(rs.getString("EXERCISE_ID"));
							break;
						}
						counter++;
					}
				}
			} while(option != (rows+1));
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}	
	}

	private void EditHomeworkParameters(String exercise) {
		// TODO Auto-generated method stub
		int choice, rows = 0;
		String sql;
		ResultSet rs = null;
		
		Utility.ClearScreen();
		System.out.println("======================================================================");
		System.out.println("Edit "+exercise);
		System.out.println("Choose what to update:");
		System.out.println("======================================================================");
		System.out.println("1. Start date");
		System.out.println("2. End date");
		System.out.println("3. Number of attempts");
		System.out.println("4. Topics");
		System.out.println("5. Difficulty level");
		System.out.println("6. Score selection (latest attempt, maximum score or average score)");
		System.out.println("7. Number of questions");
		System.out.println("8. Correct answer points");
		System.out.println("9. Incorrect answer points");
		System.out.println("10. Back");
		System.out.println("======================================================================");
		System.out.println("Enter your choice:");
		choice = reader.nextInt();
		reader.nextLine();
		
		while(choice <= 0 || choice > 10) {
			System.out.println("Enter valid option");
			choice = reader.nextInt();
			reader.nextLine();
		}
		
		try {
			sql = "SELECT * FROM ASSESSMENT WHERE EXERCISE_ID = '" + exercise + "'";
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				switch (choice) {
					case 1: System.out.println("Enter the new start date.\n Current start date is "+Utility.GetFormatDate(rs.getDate("START_DATE")));
							String start_date = reader.nextLine();
							while(!Utility.isThisDateValid(start_date, "dd-MMM-yy") || !Utility.CompareDates(start_date, Utility.GetFormatDate(rs.getDate("END_DATE"))) || !Utility.CompareTodayDate(currentdate, start_date)) {
								System.out.println("Enter valid start date:");
								start_date = reader.nextLine();
							}
							sql = "UPDATE ASSESSMENT SET START_DATE = '" + start_date + "' WHERE EXERCISE_ID = '" + exercise + "'";
							rows = stmt.executeUpdate(sql);
							break;
						
					case 2: System.out.println("Enter the new end date.\n Current end date is "+Utility.GetFormatDate(rs.getDate("END_DATE")));
							String end_date = reader.nextLine();
							while(!Utility.isThisDateValid(end_date, "dd-MMM-yy") || !Utility.CompareDates(Utility.GetFormatDate(rs.getDate("START_DATE")), end_date) || !Utility.CompareTodayDate(currentdate, end_date)) {
								System.out.println("Enter valid end date:");
								start_date = reader.nextLine();
							}
							sql = "UPDATE ASSESSMENT SET END_DATE = '" + end_date + "' WHERE EXERCISE_ID = '" + exercise + "'";
							rows = stmt.executeUpdate(sql);
							break;
						
					case 3: System.out.println("Enter the new value of number of attempts:");
							System.out.println("Current value is "+ rs.getInt("RETRIES"));
							int numattempt = reader.nextInt();
							reader.nextLine();
							sql = "UPDATE ASSESSMENT SET RETRIES = " + numattempt + " WHERE EXERCISE_ID = '" + exercise + "'";
							rows = stmt.executeUpdate(sql);
							break;
						
					case 6: System.out.println("Enter the new score selection (latest attempt, maximum score or average score).");
							System.out.println("Current value is "+ rs.getString("SCORE_SELECTION_METHOD"));
							String scoring_policy = reader.nextLine();
							reader.nextLine();
							sql = "UPDATE ASSESSMENT SET SCORE_SELECTION_METHOD = '" + scoring_policy + "' WHERE EXERCISE_ID = '" + exercise + "'";
							rows = stmt.executeUpdate(sql);
							break;
						
					case 8: System.out.println("Enter the new value of correct answer points:");
				        	System.out.println("Current value is "+ rs.getInt("POINTS_CORRECT"));
				        	int correct_points = reader.nextInt();
				        	reader.nextLine();
				        	sql = "UPDATE ASSESSMENT SET POINTS_CORRECT = " + correct_points + " WHERE EXERCISE_ID = '" + exercise + "'";
				        	rows = stmt.executeUpdate(sql);
				        	break;
				        	
					case 9: System.out.println("Enter the new value of wrong answer points:");
							System.out.println("Current value is "+ rs.getInt("POINTS_WRONG"));
							int incorrect_points = reader.nextInt();
							reader.nextLine();
							sql = "UPDATE ASSESSMENT SET POINTS_WRONG = " + incorrect_points + " WHERE EXERCISE_ID = '" + exercise + "'";
							rows = stmt.executeUpdate(sql);
							break;
						
					case 10: return;
				
					default: System.out.println("Choice not applicable");
						 	return;		
				}
			}
			if(rows != 1)
				System.out.println("Homework not modified");
			else {
				System.out.println("\n*******************\nHomework modified!!\n*******************\n");
				conn.commit();
			}
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}
	}

	private int DisplayHomeworkMenu(ResultSet rs, int rows) {
		// TODO Auto-generated method stub
		int counter = 1; int option = 0;
		Utility.ClearScreen();
		System.out.println("======================================================================");
		try {
			while(rs.next()){
				String exercise_id = rs.getString("EXERCISE_ID");
				System.out.println(counter + ". " + exercise_id);
				counter++;
			}
		
			System.out.println(counter + ". Back");
			System.out.println("======================================================================");
			System.out.println("Enter your choice:");
			option = reader.nextInt();
			reader.nextLine();
			while(option > rows+1 || option == 0) {
				System.out.println("Enter valid choice:");
				option = reader.nextInt();
				reader.nextLine();
			}
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
			return(rows+1);
		}
		return option;
	}

	private void AddRemoveQuestions() {
		// TODO Auto-generated method stub
		int rows = 0, option = 0, counter = 0;
		try {
			String current = Utility.GetFormatDate(currentdate);
			String sql = "SELECT EXERCISE_ID, TOPIC, DIFFICULTY, QUESTIONS_LIST FROM ASSESSMENT WHERE END_DATE > '" + current + 
					     "' AND COURSE_ID = '" + courseid +"'";
			ResultSet rs = null;
			do {
				rs = stmt.executeQuery(sql);
				rows = Utility.GetResultSetNumRows(rs);
				
				if(rows == 0) {
					System.out.println("No homeworks found");
					return;
				}
				counter = 1;
				option = DisplayHomeworkMenu(rs, rows);
				if(option <= rows) {
					counter = 1;
					rs.beforeFirst();
					while(rs.next()) {
						if(counter == option) {
							String exercise = rs.getString("EXERCISE_ID");
							String topic = rs.getString("TOPIC");
							String difficulty = rs.getString("DIFFICULTY");
							String questions = rs.getString("QUESTIONS_LIST");
							HandleModifyHomework(exercise, topic, difficulty, questions);
							break;
						}
						counter++;
					}
				}
				rs.close();
			} while(option != (rows+1));
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}
		
	}

	private void HandleModifyHomework(String exercise, String topic, String difficulty, String questions) {
		int option = 0;
		do {
			Utility.ClearScreen();
			System.out.println("======================================================================");
			System.out.println(exercise);
			System.out.println("======================================================================");
			System.out.println("1. Search and Add question");
			System.out.println("2. Remove question");
			System.out.println("3. Back");
			System.out.println("======================================================================");
			System.out.println("Enter your choice:");
			option = reader.nextInt();
			reader.nextLine();
			while(option > 3) {
				System.out.println("Invalid choice.\nEnter your choice:");
				option = reader.nextInt();
				reader.nextLine();
			}
			if (option == 1)
				SearchAddQuestion(exercise, topic, difficulty, questions);
			else if(option == 2)
				RemoveQuestion(exercise, questions);
		} while(option != 3);
	}

	private void RemoveQuestion(String exercise, String questions) {
		// TODO Auto-generated method stub
		int counter = 1; int remove = 0;
		String sql = "";
		String[] list = questions.split(",");
		ResultSet rs = null;
		Utility.ClearScreen();
		System.out.println("======================================================================");
		System.out.println("Here are the questions currently in the homework");
		System.out.println("======================================================================");
		try {
			for(String question : list) {
				sql = "SELECT TEXT FROM QUESTIONS WHERE QUESTION_ID = '" + question + "'";
				rs = stmt.executeQuery(sql);
				while(rs.next())
					System.out.println(counter + ". " + rs.getString("TEXT"));
				counter++;
			}
			rs.close();
			System.out.println("======================================================================");
			System.out.println("Enter the question number you want to delete. Press 0 to go back:");
			remove = reader.nextInt();
			reader.nextLine();
			
			while(remove > list.length) {
				System.out.println("Enter a valid question number");
				remove = reader.nextInt();
				reader.nextLine();
			}
			if(remove == 0)
				return;
			String remove_id = list[remove-1];
			
			//Form a new list of question IDs
			String new_list = "";
			for(String question : list) {
				if(question.equals(remove_id))
					continue;
				else
					new_list = new_list + question + ",";
			}
			
			new_list = new_list.substring(0, new_list.length() - 1);
			sql = "UPDATE ASSESSMENT SET QUESTIONS_LIST = '" + new_list + "' WHERE EXERCISE_ID = '" + exercise + "'";
			int rows = stmt.executeUpdate(sql);
			if(rows != 1) {
				System.out.println("Homework not modified");
			} else {
				System.out.println("\n*******************\nHomework modified!!\n*******************\n");
				conn.commit();
			}
			System.out.println("Press Enter to continue");
			reader.nextLine();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void SearchAddQuestion(String exercise, String topic, String difficulty, String questions) {
		// TODO Auto-generated method stub
		int levelLow = Integer.parseInt(difficulty.split("-")[0]);
		int levelHigh = Integer.parseInt(difficulty.split("-")[1]);
		try {
			String sql = "SELECT Q1.QUESTION_ID, Q1.TEXT FROM QUESTIONS Q1 WHERE Q1.DIFFICULTY >= " + levelLow +
					     " AND Q1.DIFFICULTY <= " + levelHigh + " AND Q1.CHAPTER_ID IN (" +
					                         "SELECT C1.CHAPTER_ID FROM CHAPTERS C1, COURSETEXTBOOK T1" + 
					                         " WHERE T1.COURSE_ID = '" + courseid + "'" +
					                         " AND T1.ISBN = C1.ISBN" +
					                         " AND C1.CHAPTER_NAME = '" + topic + "')";
			
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			int rows = Utility.GetResultSetNumRows(rs);
			
			if(rows == 0) {
				System.out.println("No questions found for this criteria");
				return;
			}
			
			int question_num = 1;
			Utility.ClearScreen();
			System.out.println("======================================================================");
			while(rs.next()){
				   String text = rs.getString("TEXT");
				   System.out.print(question_num + ": ");
				   System.out.println(", Question: " + text);
				   question_num++;
			}
			System.out.println("======================================================================");
			
			String[] new_questions_str;
			String[] old_questions_id_str;
			System.out.println("Select the questions you want to include in the exercise.");
			System.out.println("Type the question numbers and separate them by a comma. E.g. 1,2 if you want to select questions 1 and 2.");
			String question_string = reader.nextLine();
			new_questions_str = question_string.split(",");
			old_questions_id_str = questions.split(",");
			ArrayList<Integer> new_questions = new ArrayList<Integer>();
			for (String ques : new_questions_str)
				new_questions.add(Integer.parseInt(ques));
			Collections.sort(new_questions);
			
			HashSet<Integer> new_questions_set = new HashSet<Integer>();
			for (String ques : old_questions_id_str)
				new_questions_set.add(Integer.parseInt(ques));

			question_num = 1;
			rs.beforeFirst();
			while(rs.next()) {
				if(new_questions.contains(question_num))
					new_questions_set.add(rs.getInt("QUESTION_ID"));
				question_num++;
			}
			String question_list = "";
			for(Integer id : new_questions_set) {
				question_list = question_list + id + ",";
			}
			question_list = question_list.substring(0, question_list.length() - 1);
			
			sql = "UPDATE ASSESSMENT SET QUESTIONS_LIST = '" + question_list + "' WHERE EXERCISE_ID = '" + exercise + "'";
			int row = stmt.executeUpdate(sql);
			if(row == 1) {
				System.out.println("\n*******************\nHomework modified!!\n*******************\n");
				conn.commit();
			} else {
				System.out.println("Homework not modified!!");
			}
			System.out.println("Press Enter to continue");
			reader.nextLine();
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}
		
	}

	private void AddHomework() {
		// TODO Auto-generated method stub
		Utility.ClearScreen();
		System.out.println("======================================================================");
		System.out.println("Enter start date in (dd-Month-YY) format. E.g 20-AUG-14");
		String start_date = reader.nextLine();
		while(!Utility.isThisDateValid(start_date, "dd-MMM-yy") || !Utility.CompareTodayDate(currentdate, start_date)) {
			System.out.println("Enter valid start date:");
			start_date = reader.nextLine();
		}
		System.out.println("Enter end date in (dd-Month-YY) format. E.g 20-AUG-14");
		String end_date = reader.nextLine();
		while(!Utility.isThisDateValid(end_date, "dd-MMM-yy") || !Utility.CompareDates(start_date, end_date) || !Utility.CompareTodayDate(currentdate, end_date)) {
			System.out.println("Enter valid end date:");
			end_date = reader.nextLine();
		}
		
		System.out.println("Enter number of attempts:");
		int retries = reader.nextInt();
		reader.nextLine();
		System.out.println("Enter topic:");
		String topic = reader.nextLine();
		System.out.println("Enter difficulty range (Example: 1-3):");
		String difficulty = reader.nextLine();
		int levelLow = Integer.parseInt(difficulty.split("-")[0]);
		int levelHigh = Integer.parseInt(difficulty.split("-")[1]);
		while(levelLow < 1 || levelHigh > 5) {
			System.out.println("Invalid difficulty range.\nEnter again:");
			difficulty = reader.nextLine();
			levelLow = Integer.parseInt(difficulty.split("-")[0]);
			levelHigh = Integer.parseInt(difficulty.split("-")[1]);
		}
		System.out.println("Enter the score selection scheme: (latest attempt, maximum score or average score)");
		String scoring_scheme = reader.nextLine();
		while (!(scoring_scheme.equals("latest attempt") || scoring_scheme.equals("maximum score") || scoring_scheme.equals("average score"))) {
			System.out.println("Invalid scoring scheme.\nEnter again:");
			scoring_scheme = reader.nextLine();
		}
		System.out.println("Enter number of question:");
		int num_questions = reader.nextInt();
		reader.nextLine();
		System.out.println("Enter the points per correct asnwer value");
		int points_per_correct = reader.nextInt();
		reader.nextLine();
		System.out.println("Enter the points per incorrect asnwer value");
		int points_per_incorrect = reader.nextInt();
		reader.nextLine();
		System.out.println("Give a name to the exercise (ID should be unique)");
		String exercise_id = reader.nextLine();
		Random randomGenerator = new Random();
		int seed = randomGenerator.nextInt(100);
		System.out.println("======================================================================");
		
		try {
			String sql = "SELECT Q1.QUESTION_ID, Q1.TEXT FROM QUESTIONS Q1 WHERE Q1.DIFFICULTY >= " + levelLow +
					     " AND Q1.DIFFICULTY <= " + levelHigh + " AND Q1.CHAPTER_ID IN (" +
					                         "SELECT C1.CHAPTER_ID FROM CHAPTERS C1, COURSETEXTBOOK T1" + 
					                         " WHERE T1.COURSE_ID = '" + courseid + "'" +
					                         " AND T1.ISBN = C1.ISBN" +
					                         " AND C1.CHAPTER_NAME = '" + topic + "')";
			
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			int rows = Utility.GetResultSetNumRows(rs);
			
			if(rows == 0) {
				System.out.println("No questions found for this criteria");
				return;
			}
			
			int question_num = 1;
			while(rs.next()){
				   String text = rs.getString("TEXT");
				   System.out.print(question_num + ": ");
				   System.out.println(", Question: " + text);
				   question_num++;
			}
			
			String[] questions_str;
			do {
				System.out.println("Select the questions you want to include in the exercise.");
				System.out.println("Type the question numbers and separate them by a comma. E.g. 1,2 if you want to select questions 1 and 2.");
				String question_string = reader.nextLine();
				questions_str = question_string.split(",");
			} while(questions_str.length != num_questions && num_questions <= rows);
			
			ArrayList<Integer> questions = new ArrayList<Integer>();
			for (String ques : questions_str)
				questions.add(Integer.parseInt(ques));

			question_num = 1;
			Collections.sort(questions);
			String question_list = "";
			rs.beforeFirst();
			while(rs.next()) {
				if(questions.contains(question_num))
					question_list = question_list + rs.getInt("QUESTION_ID") + ",";
				question_num++;
			}
			question_list = question_list.substring(0, question_list.length() - 1);
			
			sql = "INSERT INTO ASSESSMENT VALUES ('" + exercise_id + "', '" + question_list + "', " + points_per_correct + ", " + points_per_incorrect +
				  ", '" + start_date + "', '" + end_date + "', " + retries + ", " + seed + ", '" + scoring_scheme + "', '" + courseid + "', '" + topic + 
				  "', '" + difficulty +"')";
			rs = stmt.executeQuery(sql);
			conn.commit();
			System.out.println("\n*******************\nHomework created!!\n*******************\n");
			rs.close();
			stmt.close();
			conn.close();
			
		} catch(SQLException e) {
			System.out.println("Something bad happened here!!:(");
			e.printStackTrace();
		}
	}
	
}
