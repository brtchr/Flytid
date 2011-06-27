package info.brathen.flytid.service;
/**
 * 
 */


import info.brathen.flytid.enums.UrlParameters;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.xml.handler.FlytidXmlHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

@SuppressWarnings("unchecked")
public class AvinorWebService {
	/**
	 * 
	 */
	private static final String ISO_8859_1 	= "iso-8859-1";
	public static final String STATUS_URL 	= "http://flydata.avinor.no/flightStatuses.asp?";
	public static final String AIRLINE_URL 	= "http://flydata.avinor.no/airlineNames.asp?";
	public static final String AIRPORT_URL 	= "http://flydata.avinor.no/airportNames.asp?";
	public static final String FLIGHT_URL 	= "http://flydata.avinor.no/XmlFeed.asp?TimeFrom=0&TimeTo=3&"; 

	/**
	 * @return
	 */
	private static String createFullPath(String path) {
		StringBuffer sb = new StringBuffer(path);
		if(path.equals(FLIGHT_URL)) {
			sb.append(UrlParameters.AIRPORT.getUrlParam(Settings.selectedAirport.name()));
			sb.append(UrlParameters.DIRECTION.getUrlParam(Settings.arrivalOrDeparture.name()));
		}
		
		return sb.toString();
	}


	public static ArrayList avinorXmlService(String path, FlytidXmlHandler handler) {
		try {
			/* Create a URL we want to load some xml-data from. */
			path = createFullPath(path);
			URL url = new URL(path);
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			
			/* Apply a contentHandler to the XML-Reader */
			xr.setContentHandler(handler);
			
			/* Parse the xml-data from our URL. */
			InputSource inputSource = new InputSource(url.openStream());
			inputSource.setEncoding(ISO_8859_1);
			
			xr.parse(inputSource);
		} catch (MalformedURLException e) {
			Log.e("AvinorWebService", e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			Log.e("AvinorWebService", e.getMessage(), e);
		} catch (SAXException e) {
			Log.e("AvinorWebService", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("AvinorWebService", e.getMessage(), e);
		}
		
		return handler.getElements();
	}
}
