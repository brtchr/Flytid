/**
 * 
 */
package info.brathen.flytid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

/**
 * @author Christoffer
 *
 */
public class DateFormatter {
	
	private static final SimpleDateFormat sdfOriginal = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final SimpleDateFormat sdfFitted = new java.text.SimpleDateFormat("HH:mm");
	
	public static String convertDate(String date) {
		sdfOriginal.setTimeZone(TimeZone.getTimeZone("UTC"));
    	try {
			return sdfFitted.format(sdfOriginal.parse(date));
		} catch (ParseException e) {
			Log.e("DATE", e.getMessage(), e);
		}
		return null;
	}
	
	
	public static Date convertToDate(String date) {
		if(date == null) {
			return null;
		}
		
		sdfOriginal.setTimeZone(TimeZone.getTimeZone("UTC"));
    	try {
			return sdfOriginal.parse(date);
		} catch (ParseException e) {
			Log.e("DATE", e.getMessage(), e);
		}
		
		return null;
	}
	
	/**
	 * Formats date to HH:mm pattern
	 * @param date
	 * @return string in 'HH:mm' pattern
	 */
	public static String displayDate(Date date) {
 		try {
 			return sdfFitted.format(date);
 		} catch(NullPointerException npe) {
 			return null;
 		}
	}
}
