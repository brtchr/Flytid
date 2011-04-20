/**
 * 
 */
package info.brathen.flytid.xml.handler;

import info.brathen.flytid.domain.Airline;
import info.brathen.flytid.domain.Airport;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.enums.ArrDep;
import info.brathen.flytid.enums.FlightStatus;
import info.brathen.flytid.util.AirlineProvider;
import info.brathen.flytid.util.AirportProvider;
import info.brathen.flytid.util.DateFormatter;
import info.brathen.flytid.util.Settings;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;

/**
 * @author Christoffer
 * 
 */
public class FlightXmlHandler extends FlytidXmlHandler<Flight> {

	// TAG keys
	private static final String TAG_FLIGHT = "flight";
	private static final String TAG_AIRLINE = "airline";
	private static final String TAG_FLIGHT_ID = "flight_id";
	private static final String TAG_SCHEDULE_TIME = "schedule_time";
	private static final String TAG_ARR_DEP = "arr_dep";
	private static final String TAG_AIRPORT = "airport";
	private static final String TAG_VIA_AIRPORT = "via_airport";
	private static final String TAG_CHECK_IN = "check_in";
	private static final String TAG_GATE = "gate";
	private static final String TAG_STATUS = "status";
	private static final String TAG_BELT = "belt";
	private static final String ATTRIBUTE_STATUS_CODE = "code";
	private static final String ATTRIBUTE_STATUS_TIME = "time";
	
	private static final long TIME_LIMIT = 1000*60*15;

	private AirlineProvider airlineProvider;
	private AirportProvider airportProvider;

	private List<Flight> flights;
	private Flight flight;

	private String currentValue;

	public FlightXmlHandler(Context context) {
		super();
		
		flights = new ArrayList<Flight>();
		airlineProvider = new AirlineProvider(Airline.class, context);
		airportProvider = new AirportProvider(Airport.class, context);
	}


	/**
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals(TAG_FLIGHT) || qName.equals(TAG_FLIGHT)) {
			flight = new Flight();
		} 
		else if (localName.equals(TAG_STATUS) || qName.equals(TAG_STATUS)) {
			String code = attributes.getValue(ATTRIBUTE_STATUS_CODE);
			String time = attributes.getValue(ATTRIBUTE_STATUS_TIME);

			flight.setFlightStatus(FlightStatus.valueOf(code));
			flight.setStatusTime(DateFormatter.convertToDate(time));
			
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
			if(!isFlightDeparted(flight) && !isFlightDead(flight)) {
				if(!flights.contains(flight)) {
					flights.add(flight);
					flight = null;
				}
			}
		} 
		else if (localName.equals(TAG_AIRLINE) || qName.equals(TAG_AIRLINE)) {
			Airline airline = airlineProvider.findAirlineByCode(currentValue);
			flight.setAirline(airline);
		} 
		else if (localName.equals(TAG_FLIGHT_ID) || qName.equals(TAG_FLIGHT_ID)) {
			flight.setFlightId(currentValue);
		} 
		else if (localName.equals(TAG_SCHEDULE_TIME) || qName.equals(TAG_SCHEDULE_TIME)) {
			String time = currentValue;
			if (time != null) {
				flight.setScheduledTime(DateFormatter.convertToDate(time));
			}
		} 
		else if (localName.equals(TAG_ARR_DEP) || qName.equals(TAG_ARR_DEP)) {
			flight.setArrDep(ArrDep.valueOf(currentValue));
		} 
		else if (localName.equals(TAG_AIRPORT) || qName.equals(TAG_AIRPORT)) {
			if (!currentValue.equals("\n")) {
				Airport airport = airportProvider.findAirportByCode(currentValue);
				flight.setAirport(airport);
			}
		}
		else if (localName.equals(TAG_VIA_AIRPORT) || qName.equals(TAG_VIA_AIRPORT)) {
			if (!currentValue.equals("\n")) {
				Airport airport = getViaAirport(currentValue);
				flight.setViaAirport(airport);
			}
		}
		else if (localName.equals(TAG_CHECK_IN) || qName.equals(TAG_CHECK_IN)) {
			flight.setCheckInArea(currentValue);
		} 
		else if (localName.equals(TAG_GATE) || qName.equals(TAG_GATE)) {
			flight.setGate(currentValue);
		} 
		else if (localName.equals(TAG_BELT) || qName.equals(TAG_BELT)) {
			flight.setBaggageBand(currentValue);
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
		airlineProvider.close();
		airlineProvider = null;
		airportProvider.close();
		airportProvider = null;
		super.endDocument();
	}
	
	private boolean isFlightDead(Flight flight) {
		long time = System.currentTimeMillis();
		
		if(flight.getStatusTime() != null) {
			time = flight.getStatusTime().getTime();
		} else if(flight.getScheduledTime() != null) {
			time = flight.getScheduledTime().getTime();
		}
		return time < (System.currentTimeMillis() - TIME_LIMIT);
	}

	private boolean isFlightDeparted(Flight flight) {
		if(flight.getFlightStatus() == FlightStatus.D) {
			return true;
		}
		return false;
	}
	
	private Airport getViaAirport(String viaAirportString) {
		if(viaAirportString.indexOf(",") != -1) {
			//Dersom avganger hent første
			if(Settings.arrivalOrDeparture == ArrDep.D) {
				return airportProvider.findAirportByCode(currentValue.substring(0, viaAirportString.indexOf(",")));
			}
			//Dersom ankomster hent siste
			else {
				return airportProvider.findAirportByCode(currentValue.substring(0, currentValue.lastIndexOf(",")));
			}
		}
		
		return null;
	}

	@Override
	public List<Flight> getElements() {
		return flights;
	}
	
}
