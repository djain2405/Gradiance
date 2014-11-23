package com.backup;

import java.sql.*;

import java.util.Random;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloWorld_Old {
	
	  static Scanner in = new Scanner(System.in);
	 // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";

	   //  Database credentials
	   static final String USER = "system";
	   static final String PASS = "apple";
	   
	   public static int[] RandomizeArray(int a, int b){
			Random rgen = new Random();  // Random number generator		
			int size = b-a+1;
			int[] array = new int[size];
	 
			for(int i=0; i< size; i++){
				array[i] = a+i;
			}
	 
			for (int i=0; i<array.length; i++) {
			    int randomPosition = rgen.nextInt(array.length);
			    int temp = array[i];
			    array[i] = array[randomPosition];
			    array[randomPosition] = temp;
			}
	 
	//		for(int s: array)
		//		System.out.println(s);
	 
			return array;
		}
	   
		public static boolean CompareDates(String startdate, String enddate) {
			try{
				 
	    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
	        	Date date1 = sdf.parse(startdate);
	        	Date date2 = sdf.parse(enddate);
	 
	        	if(date2.compareTo(date1) > 0){
	        		
	        	System.out.println("Apple");
	        	System.exit(0);
	        	return true;
	        	}
	        	else{
	        		System.out.println("Apple1");
	        		System.exit(0);
	        		return false;
	        		
	        	}
	    	}catch(ParseException ex){
	    		return false;
	    	}
		}

	   
	  static void Attempt_hw(String exerID1, String userID1, String couID1){
		//  String exerID="HW1";
		//  String userID="jmick";
		//  String couID="CSC540";
		  
		  
		  String exerID=exerID1;
		  String userID=userID1;
		  String couID=couID1;
		  
		  
		  String[] exer = new String[10];
			int exe=0;
			String qlist=null;
			String exermain=null;
		  int[][] assesQ=new int[20][7];
		  int asses=0;
		  int[] array,array1=new int[10];
		  int[][] assesment=new int[10][2];
		  int index=0,index1=0,m=0;
		  String[] question=new String[20];
		  int[] qid=new int[10];
		  String[][] answer=new String[20][20];
		  int[][] aid= new int[20][20];
		  Connection conn = null;
		   Statement stmt = null;
		   Statement stmt1 = null;
		   Statement stmt2=null;
		   try{
		 
		//STEP 2: Register JDBC driver
	      Class.forName("oracle.jdbc.driver.OracleDriver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);

	      //STEP 4: Execute a query
	      System.out.println("Creating statement...");
	      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	              ResultSet.CONCUR_READ_ONLY);
	      stmt1 = conn.createStatement();
	      String sql;
	   //   sql = "SELECT * FROM ASSESMENT";
	    //  ResultSet rs = stmt.executeQuery(sql);
	   //   int pc=rs.getInt("POINTS_CORRECT");
       //   int pw=rs.getInt("POINTS_WRONG");
        //  int ret=rs.getInt("RETRIES");
	      
	      
	      sql="SELECT * FROM ASSESSMENT WHERE COURSE_ID='"+couID+"'";
	      ResultSet rs = stmt.executeQuery(sql);
	      while(rs.next()){
	      Date d1=rs.getDate("START_DATE");
	      String d3=new SimpleDateFormat("dd-MM-yy").format(d1);
	      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
	      Date date3 = sdf.parse(d3);
	      Date d4=rs.getDate("END_DATE");
	      String d5=new SimpleDateFormat("dd-MM-yy").format(d4);
	      sdf = new SimpleDateFormat("dd-MM-yy");
	      Date date4 = sdf.parse(d5);
	   //   Date date1 = sdf.parse(d1);
	   //-   System.out.println(d3.toString());
	      String d2=new SimpleDateFormat("dd-MM-yy").format(new Date());
	   //-   System.out.println(d2.toString());
	      Date date2 = sdf.parse(d2);
	      
	      if(  ((date2.after(date3))  && (date2.before(date4))) || date2.equals(date3) || date2.equals(date4) )
	    	  
	      exer[exe++]=rs.getString("EXERCISE_ID");
	      }
	      int i =0;
	      System.out.println("Open Homework:");
	      while(i<exe){
	    	  System.out.print((i+1)+": ");
	    	  System.out.println(exer[i]);
	       i++;
	      }
	      while(true){
	    	  System.out.println("Enter your option:");
			   int option=in.nextInt();
			   if( option < 1 || option > i)
				   System.out.println("Incorrect option");
			   else{
				   exermain=exer[option-1];
				   break;
			   }
			   
	      }
	      sql= "SELECT * FROM ASSESSMENT where EXERCISE_ID='"+exermain+"'";
	      rs = stmt.executeQuery(sql);
	      while(rs.next()){
	    	  qlist=rs.getString("QUESTIONS_LIST");
	      }
	      int k=0;
	      String where1=null, where2="WHERE ";
	      String where="QUESTION_ID=";
	      
	      String[] parts = qlist.split(",");
	        while(k<parts.length){
	        	where1 = where + "'" +parts[k]+"' ";
	        	if(k != 0)
	        	where2 = where2 + "OR " +  where1;
	        	else
	        		where2 = where2 + where1;
	        k++;
	        }
	  //-      System.out.println(where2);
	      sql = "SELECT * FROM QUESTIONS "+where2;
	      rs = stmt.executeQuery(sql);
	      
          
	      rs.last();
	      int size = rs.getRow();
	      rs.beforeFirst();
	     // System.out.print(arra[1]);
	      //STEP 5: Extract data from result set
	      if(size>0)
	    	  array=RandomizeArray(0,size-1);
	      
	      while(rs.next()){
	         //Retrieve by column name
	    	  
	    		  m++;
	    		  index1=0;
	          qid[index]  = rs.getInt("QUESTION_ID");
	         question[index++] = rs.getString("TEXT");
	         String first = rs.getString("EXPLANATION");
	         int last = rs.getInt("CHAPTER_ID");
	         if(index1==0)
	        	 sql = "SELECT * FROM ANSWERS WHERE QUESTION_ID="+qid[index-1]+"AND IS_CORRECT='Y' AND ROWNUM<=1";
	         else
	        	 sql = "SELECT  * FROM ANSWERS WHERE QUESTION_ID="+qid[index-1]+"AND IS_CORRECT='N' AND ROWNUM<=3";
	        ResultSet rs1= stmt1.executeQuery(sql);
	         //Display values
	       //-  System.out.print("QID: " + qid[index-1]);
	      //-   System.out.print(", Text: " + question[index-1]);
	        //- System.out.print(", EXP: " + first);
	       //-  System.out.println(", Lev: " + last);
	         i=1;
	      //-   System.out.println("Options :");
	         while(rs1.next()){
	      //-  System.out.print(i++);
	      //-  System.out.print(" :");
	        answer[index-1][index1++] = rs1.getString("ANSWER_TEXT");
	        aid[index-1][index1-1]  = rs1.getInt("ANSWER_ID");
	     //-   System.out.print("Answers: " + answer[index-1][index1-1]);
	    //-    System.out.println("");
	        if(index1-1==0){
	        	sql = "SELECT  * FROM ANSWERS WHERE QUESTION_ID="+qid[index-1]+"AND IS_CORRECT='N' AND ROWNUM<=3";
	        	rs1= stmt1.executeQuery(sql);
	        }
	         
	         }
	         
	      }   
	         
	         array=RandomizeArray(0,index-1);
	         
	         i=0;
	         int j=0;
	         int option1;
	         while(i<index){
	        	 System.out.println("Question "+ (i+1) +": "+ question[array[i]]);
	        	 array1=RandomizeArray(0,3);
	        	 while(j<4){	 
	        		 System.out.println("Option "+ (j+1) +":"+ answer[array[i]][array1[j]] );
	        		 assesQ[asses][j+1]=aid[array[i]][array1[j]];
	             j++;	        	 
	        	 }
	        	 assesment[i][0]=qid[array[i]];
	   //-     	 System.out.println("QID: "+ assesment[i][0] );
	        	 assesQ[asses][0]=qid[array[i]];
	        	 
	        	 
	        	  while(true){
	    	    	  System.out.println("Enter your answer number:");
	    			   option1=in.nextInt();
	    			   if( option1 < 1 || option1 > 4)
	    				   System.out.println("Incorrect number");
	    			   else{
	    				  // exermain=exer[option-1];
	    				   break;
	    			   }
	    			   
	    	      }
	       //- 	 System.out.println("Enter your answer number:");
	       //- 	 option1=in.nextInt();
	        	 assesment[i][1]=aid[array[i]][array1[option1-1]];
	        	 assesQ[asses][5]=assesment[i][1];
	  	//-	     System.out.println("AID: "+ assesment[i][1] );
	         i++;
	         j=0;
	         asses++;
	         }
	         i=0;
	         j=0;
	     //-    System.out.println(asses);
	     
	       i=0;
	       stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
		              ResultSet.CONCUR_READ_ONLY);
	       int kml=0;  
	       while(i<asses){
	         
	    	   String sql4= "SELECT * FROM ASSESSMENT WHERE EXERCISE_ID="+exerID;
	         String sql3 = "SELECT * FROM ANSWERS WHERE QUESTION_ID='"+assesQ[i][0]+ "' AND IS_CORRECT='Y' AND ROWNUM<=1";
		     ResultSet rs3 = stmt2.executeQuery(sql3);
		     //rs3.last();
		     //System.out.println(rs3.getRow());
		     rs3.beforeFirst();
		     while(rs3.next()){
		     kml=rs3.getInt("ANSWER_ID");
		     assesQ[i][6]=kml;
	         
		     }
		     i++;
	         }
	       i=0;
	       j=0;
	       int correct=0,co=0,wrong=0,wo,marks,hmarks;
	       String assStr=null;
	       String assSt=null;
	         while(i<asses){
	        	 while(j<7){
	        		 assSt=Integer.toString(assesQ[i][j]);
	        		 if(assStr!=null)
	        		 assStr=assStr+assSt;
	        		 else
	        	     assStr=assSt;
	        	     if(j!=6)
	        	    	 assStr=assStr+",";
	        		 j++;
	        	 }
	        	 assStr=assStr+";";
	            i++; 
	            j=0;
	            if(assesQ[i][5]==assesQ[i][6])
	            correct++;
	            else
	            wrong++;
	         }
	     //-    System.out.println(assStr);
	     //-    System.out.println(correct);
	     //-    System.out.println(wrong);
	         String sql5= "SELECT * FROM ASSESSMENT WHERE EXERCISE_ID='"+exerID+"'";
	         ResultSet rs3 = stmt2.executeQuery(sql5);
	         while(rs3.next()){
	        	 co=rs3.getInt("POINTS_CORRECT");
	        	 correct=correct*co;
	        	 wo=rs3.getInt("POINTS_WRONG");
	             wrong=wrong*wo;
	         }
	         marks=correct+wrong;
	         hmarks=(asses)*co;
	         
	    //-     System.out.println(marks);
	    //-     System.out.println(hmarks);
	         sql5="SELECT * FROM ATTEMPT WHERE EXERCISE_ID='"+exermain+"' AND USERID='"+userID+"'";
	          rs3 = stmt2.executeQuery(sql5);
	          rs3.last();
		      int att = rs3.getRow();
		      att++;
	         sql5="INSERT INTO ATTEMPT VALUES ( '" + exermain +"' , '"+userID+"', '"+assStr+"', "+marks+", "+hmarks+", "+att+", '"+couID+"' )";
	         stmt2.executeUpdate(sql5);
	         
	         
	         
	        
	         rs.close();
		      stmt.close();
		      conn.close();
	         while(true){
	        	  System.out.println("1. Back");
		    	  System.out.println("Enter your option:");
				   int option=in.nextInt();
				   if( option != 1)
					   System.out.println("Incorrect option");
				   else{
					   return;
				   }
				   
		      }
	         
	         
	         //STEP 6: Clean-up environment
	    //-   rs.close();
	   //-   stmt.close();
	   //-   conn.close();
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }// nothing we can do
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
		return;
	  }
	  
	static void  viewMarks(String exerID1, String userID1, String couID1){
	//	 String exerID="HW1";
//		  String userID="jmick";
	//	  String couID="CSC540";
		  String exerID=exerID1;
		  String userID=userID1;
		  String couID=couID1;
		Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("oracle.jdbc.driver.OracleDriver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		     


		         String sql6="SELECT * FROM ATTEMPT WHERE USERID='"+userID+"'"+"AND COURSE_ID='"+couID+"' ORDER BY ATTEMPT_NO";
		         ResultSet rs = stmt.executeQuery(sql6);
		         while(rs.next()){
		        	 System.out.println("");
	   	        	 System.out.print(rs.getString("EXERCISE_ID"));
	   	        	 System.out.print("   Attempt ");
	   	        	 System.out.print(rs.getString("ATTEMPT_NO"));
	   	        	System.out.print("   |   ");
	   	        	System.out.print(rs.getInt("MARKS"));
	   	        	System.out.print("/");
	   	        	System.out.println(rs.getInt("HIGHEST"));
	   	        	
		         }
		      //STEP 6: Clean-up environment
		      rs.close();
		      stmt.close();
		      conn.close();
		      
		      while(true){
	        	  System.out.println("1. Back");
		    	  System.out.println("Enter your option:");
				   int option=in.nextInt();
				   if( option != 1)
					   System.out.println("Incorrect option");
				   else{
					   return;
				   }
				   
		      }
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try  
		  
		  
	  }
	
	
	  static void viewPastSubs(String exerID1, String userID1, String couID1){
		//  String exerID="HW1";
		//  String userID="jmick";
		//  String couID="CSC540";
		  String exerID=exerID1;
		  String userID=userID1;
		  String couID=couID1;
		  String[] exer = new String[20];
		  String[] exer1 = new String[20];
			int exe=0,exe1=0,ex=0;
			String qlist=null;
			String exermain=null;
			String exermain1=null;
		  Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("oracle.jdbc.driver.OracleDriver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      
		      
		      sql="SELECT * FROM ASSESSMENT WHERE COURSE_ID='"+couID+"'";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		      Date d1=rs.getDate("START_DATE");
		      String d3=new SimpleDateFormat("dd-MM-yy").format(d1);
		      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		      Date date3 = sdf.parse(d3);
		      Date d4=rs.getDate("END_DATE");
		      String d5=new SimpleDateFormat("dd-MM-yy").format(d4);
		      sdf = new SimpleDateFormat("dd-MM-yy");
		      Date date4 = sdf.parse(d5);
		   //   Date date1 = sdf.parse(d1);
		   //-   System.out.println(d3.toString());
		      String d2=new SimpleDateFormat("dd-MM-yy").format(new Date());
		   //-   System.out.println(d2.toString());
		      Date date2 = sdf.parse(d2);
		      
		  if(  ((date2.after(date3))  && (date2.before(date4))) || date2.equals(date3) || date2.equals(date4) )
		    	  
		      exer[exe++]=rs.getString("EXERCISE_ID");
		      }
		      
		      sql="SELECT * FROM ASSESSMENT WHERE COURSE_ID='"+couID+"'";
		      rs = stmt.executeQuery(sql);
		      while(rs.next()){
			      Date d1=rs.getDate("START_DATE");
			      String d3=new SimpleDateFormat("dd-MM-yy").format(d1);
			      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
			      Date date3 = sdf.parse(d3);
			      Date d4=rs.getDate("END_DATE");
			      String d5=new SimpleDateFormat("dd-MM-yy").format(d4);
			      sdf = new SimpleDateFormat("dd-MM-yy");
			      Date date4 = sdf.parse(d5);
			   //   Date date1 = sdf.parse(d1);
			   //-   System.out.println(d3.toString());
			      String d2=new SimpleDateFormat("dd-MM-yy").format(new Date());
			   //-   System.out.println(d2.toString());
			      Date date2 = sdf.parse(d2);
			      
			  if( ! ( ((date2.after(date3))  && (date2.before(date4))) || date2.equals(date3) || date2.equals(date4) ) )
			    	  
			      exer1[exe1++]=rs.getString("EXERCISE_ID");
			      }
		      
		      
		      
		      int i =0, dt=0,dt1=0;; 
		     String[][] dt123 = new String[500][500];
		      System.out.println("Open Homework:");
		      while(i<exe){
		    	  String sql7="SELECT * FROM ATTEMPT WHERE USERID='"+userID+"'"+"AND COURSE_ID='"+couID+"' AND EXERCISE_ID='"+exer[i]+"'";
			         rs = stmt.executeQuery(sql7);
			         while(rs.next()){
			        System.out.print((dt+1)+": ");
		    	    System.out.print(exer[i] + ", Attemp No. ");
		    	    System.out.println(rs.getString("ATTEMPT_NO"));
			         dt123[dt][0]=exer[i];
			         dt123[dt][1]=rs.getString("ATTEMPT_NO");
			         dt++;
			         }
		    	    i++;
		      }
		      dt1=dt;
		      System.out.println("Closed Homework:");
		      int j=0;
		      while(i<exe+exe1){
		    	  String sql7="SELECT * FROM ATTEMPT WHERE USERID='"+userID+"'"+"AND COURSE_ID='"+couID+"' AND EXERCISE_ID='"+exer1[j++]+"'";
			         rs = stmt.executeQuery(sql7);
			         while(rs.next()){
			        System.out.print((dt+1)+": ");
		    	    System.out.print(exer1[j-1] + ", Attemp No. ");
		    	    System.out.println(rs.getString("ATTEMPT_NO"));
			         dt123[dt][0]=exer1[j-1];
			         dt123[dt][1]=rs.getString("ATTEMPT_NO");
			         dt++;
			         }
		    	    i++;
		      }
		      int option;
		      while(true){
		    	  System.out.println("Enter your option:");
				  option=in.nextInt();
				   if( option < 1 || option > dt)
					   System.out.println("Incorrect option");
				   else{
					   exermain=dt123[option-1][0];
					   exermain1=dt123[option-1][1];
					   break;
				   }
				   
		      }
		      /*
		      sql= "SELECT * FROM ASSESSMENT where EXERCISE_ID='"+exermain+"'";
		      rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  qlist=rs.getString("QUESTIONS_LIST");
		      }
		      int k=0;
		      String where1=null, where2="WHERE ";
		      String where="QUESTION_ID=";
		      
		      String[] parts = qlist.split(",");
		        while(k<parts.length){
		        	where1 = where + "'" +parts[k]+"' ";
		        	if(k != 0)
		        	where2 = where2 + "OR " +  where1;
		        	else
		        		where2 = where2 + where1;
		        k++;
		        }
		      */
		     // ResultSet rs ;
		      String aString=null;
		         String sql7="SELECT * FROM ATTEMPT WHERE USERID='"+userID+"'"+"AND COURSE_ID='"+couID+"' AND ATTEMPT_NO="+exermain1+ " AND EXERCISE_ID='"+exermain+"'";
		         rs = stmt.executeQuery(sql7);
		         while(rs.next()){
		         aString=rs.getString("ATTEMPT_STRING");
		         break;
		         }
		         
		        String[] parts = aString.split(";");
		        String[] part = null; 
		         i=0;
		         while(  i < parts.length){
		      //-  	 System.out.println(parts[i]) ;
		        	 part = parts[i].split(",");
		        	 j = 0;
		        	 while(  j < part.length){
		        		 if(j==0){
		        		 System.out.print("Question "+(i+1)+":");
		        			 sql="SELECT  * FROM QUESTIONS WHERE QUESTION_ID="+part[j];
		        			 rs = stmt.executeQuery(sql);
		        			 while(rs.next()){
		        			 System.out.println(rs.getString("TEXT"));
		        			 }
		        		 }
		        		 
		        		 else
		        		 {
		        			 if(j<5){
		        			 sql="SELECT  * FROM ANSWERS WHERE ANSWER_ID="+part[j];
		        			 rs = stmt.executeQuery(sql);
		        			 System.out.print("Option "+j+":");
		        			 while(rs.next()){
		        			 System.out.println(rs.getString("ANSWER_TEXT"));
		        			 }
		        			 } 
		        			 
		        			 
		        			 if(j==5){
		        				 sql="SELECT  * FROM ANSWERS WHERE ANSWER_ID="+part[j];
			        			 rs = stmt.executeQuery(sql);
		        				 System.out.print("Submitted Answer : ");
		        				 while(rs.next()){
		    	        			 System.out.println(rs.getString("ANSWER_TEXT"));
		    	        			 }
		        //-				 System.out.println(part[j]);
		        	//-			 System.out.println(part[j+1]);
		        			   if(part[j].equals(part[j+1]))
		        				   System.out.println("You answered the question correctly");
		        			   else{
		        				   System.out.println("You answered the question incorrectly");
		        				   System.out.print("Explanation : ");
		  	        			  sql="SELECT  * FROM ANSWERS WHERE ANSWER_ID="+part[j];
		  	        			  rs = stmt.executeQuery(sql);
		  	        			while(rs.next()){
		    	        			 System.out.println(rs.getString("EXPLANATION"));
		    	        			 }
		        			       
		        			   }
		        			   }
		        			 
		        			 
		        		}
		        		 
		        		
		        		//- System.out.println();
		        		 
		        		 j++;
		        	 }
		        	 
		        	 if(option>dt1)
		        		 System.out.print("Question Explanation : ");
		        		 else
		        	 System.out.print("Question Hint : ");
	    			 sql="SELECT  * FROM QUESTIONS WHERE QUESTION_ID="+part[0];
	    			 rs = stmt.executeQuery(sql);
	    			 while(rs.next()){
	    				 if(option>dt1)
	        			 System.out.println(rs.getString("EXPLANATION"));
	    				 else
	    				 System.out.println(rs.getString("HINT")); 
	        			 }
		            i++;
		            System.out.println();
		            
		         }
		      //STEP 6: Clean-up environment
		      rs.close();
		      stmt.close();
		      conn.close();
		      while(true){
	        	  System.out.println("1. Back");
		    	  System.out.println("Enter your option:");
				   option=in.nextInt();
				   if( option != 1)
					   System.out.println("Incorrect option");
				   else{
					   return;
				   }
				   
		      }
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	  }
	
	
	   public static void main(String[] args) throws Exception {
		   
		   
		
		   for(;;) {
		   System.out.println("Enter your option:");
		   int option=in.nextInt();
		   switch(option) {
		   case 1:
			   Attempt_hw("HW1","jmick","CSC540");
	           break;
		   case 2:
			   viewMarks("HW1","jmick","CSC540");
	           break;
		   case 3:
			   viewPastSubs("HW1","jmick","CSC540");
	           break;
	        default:
	        System.out.println("Goodbye!");	
		    System.exit(0);
		   }
		   }
	}//end main

}