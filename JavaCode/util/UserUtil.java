package com.location.reminder.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserUtil {

	public static int ageRange(String dateofbirth) {

		try {
			Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(dateofbirth);
			Date date2 = new Date();

			long diff = date2.getTime() - date1.getTime();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

			int range = 2;
			if (days <= 15) {
				range = 2;
			} else if (days <= 30) {
				range = 3;
			} else if (days <= 45) {
				range = 4;
			} else {
				range = 5;
			}

			return range;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 2;

	}

	public static int genderRange(String gender) {
		int range = 0;
		if (gender.toLowerCase().equals("male")) {
			range = 1;
		}
		return range;
	}

	public static int timeRange(int time) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		int hourofday = calendar.get(Calendar.HOUR_OF_DAY) + 1;
		int range = 6;
		if (hourofday >= 6 && hourofday < 12) {
			range = 6;
		} else if (hourofday >= 12 && hourofday < 18) {
			range = 7;
		} else if (hourofday >= 18 && hourofday < 23) {
			range = 8;
		} else if (hourofday >= 0 && hourofday < 6) {
			range = 9;
		}

		return range;
	}
}
