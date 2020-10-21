package at.ac.uibk.ps_swa.ws19.gr7_2.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * The type Time.
 *
 * @author Philipp Schießl
 */
public class Time {

	private Time(){}

	/**
	 * Start of week date.
	 *
	 * @param date the date
	 * @return a date object set to 0:00:00 on the previous Monday.
	 */
	public static Date startOfWeek (Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal.getTime();
	}

	/**
	 * End of week date.
	 *
	 * @param date the date
	 * @return a date object set to 0:00:00 on the next Monday.
	 */
	public static Date endOfWeek (Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		cal.add(Calendar.DAY_OF_YEAR, 7);
		return cal.getTime();
	}

	/**
	 * Hours to milliseconds long.
	 *
	 * @param hours the hours
	 * @return the long
	 */
	public static long hoursToMilliseconds (long hours) {
		return hours * 3600000;
	}

	/**
	 * Milliseconds to hours long.
	 *
	 * @param milliseconds the milliseconds
	 * @return the long
	 */
	public static long millisecondsToHours (long milliseconds) {
		return milliseconds / 3600000 + (milliseconds%3600000 > 0 ? 1 : 0);
	}

	/**
	 * Add hours date.
	 *
	 * @param date  the date
	 * @param hours the hours
	 * @return the date
	 */
	public static Date addHours(Date date, long hours) {
		return new Date(date.getTime()+ hoursToMilliseconds(hours));
	}
}
