/**
 * 
 */
package info.brathen.flytid.xml.handler;

import info.brathen.flytid.enums.FlightStatuses;
import info.brathen.flytid.util.DataBaseHelper;
import info.brathen.flytid.util.DateFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.database.SQLException;

/**
 * @author Christoffer
 * 
 */
public class FlightXmlHandler extends DefaultHandler {

	// TAG keys
	private static final String TAG_FLIGHT = "flight";
	private static final String TAG_AIRLINE = "airline";
	private static final String TAG_FLIGHT_ID = "flight_id";
	private static final String TAG_SCHEDULE_TIME = "schedule_time";
	private static final String TAG_ARR_DEP = "arr_dep";
	private static final String TAG_AIRPORT = "airport";
	private static final String TAG_CHECK_IN = "check_in";
	private static final String TAG_GATE = "gate";
	private static final String TAG_STATUS = "status";
	private static final String TAG_BELT = "belt";
	private static final String ATTRIBUTE_STATUS_CODE = "code";
	private static final String ATTRIBUTE_STATUS_TIME = "time";

	private static final String REMARKS = "remarks";
	private static final String DESTINATION = "from";
	private static final String FLIGHT = "flight";
	private static final String TIME = "time";

	private DataBaseHelper myDbHelper;

	private List<Map<String, String>> flights;
	private Map<String, String> flightRow;

	private String currentValue;

	public FlightXmlHandler(Context context) {
		super();
		
		flights = new ArrayList<Map<String,String>>();
		this.myDbHelper = new DataBaseHelper(context);

		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		} catch (SQLException sqle) {
			throw sqle;
		}
	}

	/**
	 * @return the flights
	 */
	public List<Map<String, String>> getFlights() {
		return flights;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals(TAG_FLIGHT) || qName.equals(TAG_FLIGHT)) {
			flightRow = new HashMap<String, String>();
		} else if (localName.equals(TAG_AIRLINE) || qName.equals(TAG_AIRLINE)) {

		} else if (localName.equals(TAG_STATUS) || qName.equals(TAG_STATUS)) {
			String code = attributes.getValue(ATTRIBUTE_STATUS_CODE);
			String time = attributes.getValue(ATTRIBUTE_STATUS_TIME);

			FlightStatuses flightStatus = FlightStatuses.valueOf(code);

			if (time != null) {
				time = DateFormatter.convertDate(time);
			} else {
				time = "";
			}

			flightRow.put(REMARKS, flightStatus.getTextNo() + " " + time);
			flightRow.put(TAG_STATUS, flightStatus.name());			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals(TAG_FLIGHT) || qName.equals(TAG_FLIGHT)) {
			flights.add(flightRow);
		} else if (localName.equals(TAG_AIRLINE) || qName.equals(TAG_AIRLINE)) {

		} else if (localName.equals(TAG_FLIGHT_ID) || qName.equals(TAG_FLIGHT_ID)) {
			flightRow.put(FLIGHT, currentValue);
		} else if (localName.equals(TAG_SCHEDULE_TIME) || qName.equals(TAG_SCHEDULE_TIME)) {
			String time = currentValue;
			if (time != null) {
				flightRow.put(TIME, DateFormatter.convertDate(time));
			}
		} else if (localName.equals(TAG_ARR_DEP) || qName.equals(TAG_ARR_DEP)) {
			flightRow.put(TAG_ARR_DEP, currentValue);
		} else if (localName.equals(TAG_AIRPORT) || qName.equals(TAG_AIRPORT)) {
			if (!currentValue.equals("\n")) {
				flightRow.put(DESTINATION, myDbHelper.getAirportName(currentValue));
			}
		} else if (localName.equals(TAG_CHECK_IN) || qName.equals(TAG_CHECK_IN)) {
			flightRow.put(TAG_CHECK_IN, currentValue);
		} else if (localName.equals(TAG_GATE) || qName.equals(TAG_GATE)) {
			flightRow.put(TAG_GATE, currentValue);
		} else if (localName.equals(TAG_BELT) || qName.equals(TAG_BELT)) {
			flightRow.put(TAG_BELT, currentValue);
		}
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		currentValue = new String(ch, start, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		for(int i =0; i < flights.size(); i++) {
			Map<String, String> map = flights.get(i);
			if(map.containsKey(TAG_STATUS) && 
					FlightStatuses.D.equals(FlightStatuses.valueOf(map.get(TAG_STATUS)))) {
				flights.remove(i);
			}
		}
		super.endDocument();
	}

}
