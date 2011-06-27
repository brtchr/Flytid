/**
 * 
 */
package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Airline;
import info.brathen.flytid.domain.Airport;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.enums.ArrDep;
import info.brathen.flytid.enums.NorwegianAirports;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.AirlineProvider;
import info.brathen.flytid.util.AirportProvider;
import info.brathen.flytid.util.Db4oHelper;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.util.adapter.FlytidAdapter;
import info.brathen.flytid.xml.handler.AirlineXmlHandler;
import info.brathen.flytid.xml.handler.AirportXmlHandler;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

@SuppressWarnings("unchecked")
public abstract class FlytidActivity extends Activity {

	private ProgressBar progressBar = null;
	private ProgressDialog progressDialog;
	protected FlytidAdapter adapter;
	private ImageButton updateButton;
	protected ListView list;
	private TextView selectedAirportView;
	private Db4oHelper dbHelper;
	protected boolean loadingBaseData;
	protected GoogleAnalyticsTracker tracker;
	private GestureDetector gestureDetector;
//	private OnTouchListener gestureListener;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	dbHelper = new Db4oHelper(this);
    	
    	initTracker();
    	initViews();
    	initListeners();
    	checkForDatabase();
    	initGestures();
    }

	private void initTracker() {
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start(getString(R.string.tracker_id), this);
//		tracker.setDebug(true);
//		tracker.setDryRun(true);
	}
	
	protected abstract void initAdapter() ;

	private void checkForDatabase() {
		if(dbHelper.isEmpty()) {
			progressDialog = ProgressDialog.show(this, "", "Laster grunndata data. Vennligst vent...", true);
			downloadBaseData();
		}
	}

	private void initListeners() {
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tracker.trackEvent(
			            "Clicks",  // Category
			            "Update",  // Action
			            "clicked", // Label
			            77);       // Value
				if(!loadingBaseData) {
	        		downloadFlights();
	        	}
			}
		});
		
		selectedAirportView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	private void initViews() {
		list = (ListView) findViewById(R.id.flight_list);
    	progressBar = (ProgressBar) findViewById(R.id.progress_small);
    	updateButton = (ImageButton) findViewById(R.id.update);
    	selectedAirportView = (TextView)this.findViewById(R.id.selected_airport);
        selectedAirportView.setText(Settings.selectedAirport.getNavn());
	}
    
	private void initGestures() {
		gestureDetector = new GestureDetector(new FlingDetector());
		
		list.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });
	}
	
	public void downloadFlights() {
		updateButton.setVisibility(ImageButton.GONE);
		progressBar.setVisibility(ProgressBar.VISIBLE);
		
		clearOldFlights();
		
		AsyncTask<String, Void, ArrayList<Flight>> task = 
			new AsyncTask<String, Void, ArrayList<Flight>>() {
				@Override
			    // Actual download method, run in the task thread
			    protected ArrayList<Flight> doInBackground(String... params) {
			    	// params comes from the execute() call: params[0] is the url.
			    	return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(FlytidActivity.this));
			    }
	
			    @Override
			    protected void onPostExecute(ArrayList<Flight> newFlights) {
			    	updateFlights(newFlights);
			    	updateButton.setVisibility(ImageButton.VISIBLE);
					progressBar.setVisibility(ProgressBar.GONE);
			        super.onPostExecute(newFlights);
			    }
        };

        task.execute(AvinorWebService.FLIGHT_URL);
	}

	private void clearOldFlights() {
		adapter.getFlights().removeAll(adapter.getFlights());
		adapter.notifyDataSetChanged();
	}

	protected void updateFlights(ArrayList<Flight> newFlights) {
		adapter.setFlights(newFlights);
		adapter.notifyDataSetChanged();
	}

	protected void selectedAirportEvent() {
		downloadFlights();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter.getFlights();
	}
	
	public void downloadBaseData() {
		AsyncTask<Void, Void, Void> task = 
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					loadingBaseData = true;
					AirlineXmlHandler airlineXmlHandler = new AirlineXmlHandler();
					AirportXmlHandler airportXmlHandler = new AirportXmlHandler();
					AirlineProvider airlineProvider = new AirlineProvider(Airline.class, FlytidActivity.this);
					AirportProvider airportProvider = new AirportProvider(Airport.class, FlytidActivity.this);
					//Hente alle airports og airlines og erstatte data
					ArrayList<Airline> airlines = AvinorWebService.avinorXmlService(AvinorWebService.AIRLINE_URL, airlineXmlHandler);
					ArrayList<Airport> airports = AvinorWebService.avinorXmlService(AvinorWebService.AIRPORT_URL, airportXmlHandler);
		        	airlineProvider.deleteAll();
		        	airlineProvider.store(airlines);
		        	airportProvider.deleteAll();
		        	airportProvider.store(airports);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					loadingBaseData = false;
		        	progressDialog.dismiss();
		        	downloadFlights();
					super.onPostExecute(result);
				}
        };

        task.execute();
	}
	
	@Override
	protected void onPause() {
		tracker.dispatch();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.menu_airport : {
        		createAirportdialog();
        		break;
        	}
        	case R.id.menu_arrival : {
        		tracker.trackEvent(
        	            "Menu",  // Category
        	            "Arrival",  // Action
        	            "clicked", // Label
        	            0);       // Value
        		if(Settings.arrivalOrDeparture != ArrDep.A) {
        			Settings.arrivalOrDeparture = ArrDep.A;
        			Intent arrivalIntent = new Intent();
        			arrivalIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        			arrivalIntent.setClass(getApplicationContext(), Arrivals.class);
        			arrivalIntent.putExtra("flights", adapter.getFlights());
        			startActivity(arrivalIntent);
        		}
        		break;
        	}
        	case R.id.menu_departure : {
        		tracker.trackEvent(
        	            "Menu",  // Category
        	            "Departure",  // Action
        	            "clicked", // Label
        	            0);       // Value
				if (Settings.arrivalOrDeparture != ArrDep.D) {
					Settings.arrivalOrDeparture = ArrDep.D;
					Intent departureIntent = new Intent();
					departureIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					departureIntent.setClass(getApplicationContext(), Departures.class);
					departureIntent.putExtra("flights", adapter.getFlights());
					startActivity(departureIntent);
				}
        		break;
        	}
        }
        return true;
    }
	
	public void createAirportdialog() {
		final String[] names = NorwegianAirports.getNames();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.chooseAirport);
		builder.setItems(NorwegianAirports.getFullNames(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
		    	Settings.selectedAirport = NorwegianAirports.valueOf(names[item]);
		    	trackSelectAirport();
		    	Toast.makeText(FlytidActivity.this, Settings.selectedAirport.toString(), Toast.LENGTH_SHORT).show();
		        selectedAirportView.setText(Settings.selectedAirport.getNavn());
		        selectedAirportEvent();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void trackSelectAirport() {
		tracker.setCustomVar(1, "airport", Settings.selectedAirport.toString(), 3);
		tracker.trackEvent(
		        "Menu",  // Category
		        "Select airport",  // Action
		        "clicked", // Label
		        0);       // Value
	}

	private void changeActivity() {
		tracker.trackEvent("Fling", "Change Activity", "Gesture", 0);

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("flights", adapter.getFlights());
		if(ArrDep.A.equals(Settings.arrivalOrDeparture)) {
			Settings.arrivalOrDeparture = ArrDep.D;
			intent.setClass(getApplicationContext(), Departures.class);
		}
		else {
			Settings.arrivalOrDeparture = ArrDep.A;
			intent.setClass(getApplicationContext(), Arrivals.class);
		}
		startActivity(intent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			changeActivity();
			return true;
		}
		
		return super.onTouchEvent(event);
	}

	class FlingDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 150;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 100;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                return false;
	            // right to left swipe
	            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	            	changeActivity();
	            	return true;
	            }  
	            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	            	changeActivity();
	            	return true;
	            }
	        } catch (Exception e) {
	            // nothing
	        }
	        return false;
		}
	}
}
