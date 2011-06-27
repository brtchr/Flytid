/**
 * 
 */
package info.brathen.flytid.xml.handler;

import info.brathen.flytid.domain.Airline;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Christoffer
 *
 */
public class AirlineXmlHandler extends FlytidXmlHandler<Airline> {
	private ArrayList<Airline> airlines ;
	private Airline airline;
	
	//TAG keys
	private static final String TAG_AIRLINE = "airlineName";
	private static final String TAG_AIRLINE_CODE = "code";
	private static final String TAG_AIRLINE_NAME = "name";

	public AirlineXmlHandler() {
		super();
		airlines = new ArrayList<Airline>();
	}
	
	public ArrayList<Airline> getElements() {
		return airlines;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals(TAG_AIRLINE) || qName.equals(TAG_AIRLINE)) {
			String code = attributes.getValue(TAG_AIRLINE_CODE);
			String name = attributes.getValue(TAG_AIRLINE_NAME);
			airline = new Airline(code, name);
			
			airlines.add(airline);
		}
	}
}
