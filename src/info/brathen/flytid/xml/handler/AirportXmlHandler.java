/**
 * 
 */
package info.brathen.flytid.xml.handler;

import info.brathen.flytid.domain.Airport;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Christoffer
 *
 * This class handles the parsing of airports
 */
public class AirportXmlHandler extends FlytidXmlHandler<Airport> {
	private ArrayList<Airport> airports ;
	private Airport airport;
	
	//TAG keys
	private static final String TAG_AIRPORT = "airportName";
	private static final String TAG_AIRPORT_CODE = "code";
	private static final String TAG_AIRPORT_NAME = "name";

	public AirportXmlHandler() {
		super();
		airports = new ArrayList<Airport>();
	}
	
	public ArrayList<Airport> getElements() {
		return airports;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals(TAG_AIRPORT) || qName.equals(TAG_AIRPORT)) {
			String code = attributes.getValue(TAG_AIRPORT_CODE);
			String name = attributes.getValue(TAG_AIRPORT_NAME);
			airport = new Airport(code, name);
			
			airports.add(airport);
		}
	}

}
