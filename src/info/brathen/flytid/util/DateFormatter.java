/**
 * 
 */
package info.brathen.flytid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
