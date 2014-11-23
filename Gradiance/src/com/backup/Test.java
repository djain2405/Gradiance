package com.backup;

import java.util.Calendar;
import java.util.Date;

public class Test {

	/**
	 * @param args
	 * 
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Calendar toDateMin1 = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		Calendar nowDate = Calendar.getInstance();
		
		toDate.set(2014, 9, 18, 2, 27);
		toDateMin1.setTime(toDate.getTime());
		
		nowDate.setTime(new Date());
		
		toDateMin1.add(Calendar.DATE, -1);
		
		System.out.println(toDateMin1.getTime());
		System.out.println(toDate.getTime());
		System.out.println(nowDate.after(toDateMin1) && nowDate.before(toDate));
	}

}
