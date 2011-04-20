/**
 * 
 */
package info.brathen.flytid.activities;

import java.util.List;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Airline;
import info.brathen.flytid.domain.Airport;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.AirlineProvider;
import info.brathen.flytid.util.AirportProvider;
import info.brathen.flytid.xml.handler.AirlineXmlHandler;
import info.brathen.flytid.xml.handler.AirportXmlHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Christoffer
 *
 */
public class Sync extends Activity {
	
	private ProgressDialog dialog;
	private long startTime; 
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		
		dialog = ProgressDialog.show(this, "", "Loading data. Please wait...", true);
		
		WorkThread workThread = new WorkThread();
		workThread.start();
		
    }
	
	
	private class WorkThread extends Thread {
		AirlineXmlHandler airlineXmlHandler = new AirlineXmlHandler();
		AirportXmlHandler airportXmlHandler = new AirportXmlHandler();
		AirlineProvider airlineProvider = new AirlineProvider(Airline.class, Sync.this);
		AirportProvider airportProvider = new AirportProvider(Airport.class, Sync.this);
		
		@Override
        public void run() {       
        	//Hente alle airports og airlines og erstatte data
        	List<Airline> airlines = AvinorWebService.avinorXmlService(AvinorWebService.AIRLINE_URL, airlineXmlHandler);
        	List<Airport> airports = AvinorWebService.avinorXmlService(AvinorWebService.AIRPORT_URL, airportXmlHandler);
        	airlineProvider.deleteAll();
        	airlineProvider.store(airlines);
        	airportProvider.deleteAll();
        	airportProvider.store(airports);
        	dialog.dismiss();
            Log.i("Synchronize", "Time spending syncing: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
        }
    }
}
