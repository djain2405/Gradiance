package com.backup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class Utility {
	public static boolean isThisDateValid(String dateToValidate, String dateFromat){
		 
		if(dateToValidate == null){
			return false;
		}
 
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
 
		try {
			sdf.parse(dateToValidate);
			//System.out.println(date);
 
		} catch (ParseException e) {
			System.out.println("Invalid start date");
			return false;
		}
 
		return true;
	}
	
	public static boolean CompareDates(String startdate, String enddate) {
		try{
			 
    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        	Date date1 = sdf.parse(startdate);
        	Date date2 = sdf.parse(enddate);
 
        	if(date2.compareTo(date1) > 0)
        		return true;
        	else
        		return false;
 
    	}catch(ParseException ex){
    		return false;
    	}
	}
	
	public static boolean CompareTodayDate(Date currentdate, String date) {
		try{ 
    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        	Date testdate = sdf.parse(date);
 
        	if(testdate.compareTo(currentdate) > 0)
        		return true;
        	else
        		return false;
 
    	}catch(ParseException ex){
    		return false;
    	}
	}
	
	public static String GetFormatDate(Date date) {
		return(new SimpleDateFormat("dd-MMM-yy").format(date));
	}
	
	public static int GetResultSetNumRows(ResultSet rs) throws SQLException {
		int rows = 0;
		rs.last();
		rows = rs.getRow();
		rs.beforeFirst();
		return rows;
	}
	
	public static void ClearScreen() {
		for(int i = 0; i < 50; i++)
			System.out.println();
	}
}
