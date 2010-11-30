package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.enums.ArrDep;
import info.brathen.flytid.enums.NorwegianAirports;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.util.adapter.DeparturesAdapter;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FlightActivity extends Activity {
	
	private ProgressBar progressBar = null;
	private DeparturesAdapter adapter;
	private ScheduledExecutorService scheduler;
	private DownloaderTask downloaderTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); 

		downloaderTask = new DownloaderTask();

		ListView list = (ListView) findViewById(R.id.flight_list);
		progressBar = (ProgressBar) findViewById(R.id.progress_small);

		TextView selectedAirportView = (TextView) this.findViewById(R.id.selected_airport);
		selectedAirportView.setText(Settings.selectedAirport.getNavn());

		final Object oldFlights = getLastNonConfigurationInstance();
		if (oldFlights != null) {
			adapter = new DeparturesAdapter(this, (List<Flight>) oldFlights);
			list.setAdapter(adapter);
		} else {
			adapter = new DeparturesAdapter(this, new ArrayList<Flight>());
			list.setAdapter(adapter);
		}

		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(downloaderTask, 0, 180, TimeUnit.SECONDS);
	}

	private void setLayout() {
		
		setContentView(R.layout.departures);
	}

	public DownloaderTask downloadFlights() {
		progressBar.setVisibility(ProgressBar.VISIBLE);
		clearOldFlights();
		downloaderTask.execute(AvinorWebService.FLIGHT_URL);
		return downloaderTask;
	}

	private void clearOldFlights() {
		if (adapter.getFlights() != null || adapter.getFlights().size() != 0) {
			adapter.getFlights().removeAll(adapter.getFlights());
			adapter.notifyDataSetChanged();
		}
	}

	protected void updateFlights(List<Flight> newFlights) {
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.menu_airport : {
        		createAirportdialog();
        		break;
        	}
        	case R.id.menu_arrival : {
        		if(Settings.arrivalOrDeparture != ArrDep.A) {
        			Settings.arrivalOrDeparture = ArrDep.A;
        			Intent arrivalIntent = new Intent();
        			arrivalIntent.setClass(getApplicationContext(), Arrivals.class);
        			startActivity(arrivalIntent);
        		}
        		break;
        	}
        	case R.id.menu_departure : {
				if (Settings.arrivalOrDeparture != ArrDep.D) {
					Settings.arrivalOrDeparture = ArrDep.D;
					Intent departureIntent = new Intent();
					departureIntent.setClass(getApplicationContext(), Departures.class);
					startActivity(departureIntent);
				}
        		break;
        	}
        	case R.id.menu_preferences : {
        		Intent preferenceIntent = new Intent();
        		preferenceIntent.setClass(getApplicationContext(), FlytidPreferenceActivity.class);
				startActivity(preferenceIntent);
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
		    	Toast.makeText(FlightActivity.this, NorwegianAirports.valueOf(names[item]).toString(), Toast.LENGTH_SHORT).show();
		        Settings.selectedAirport = NorwegianAirports.valueOf(names[item]);
		        TextView selectedAirportView = (TextView)FlightActivity.this.findViewById(R.id.selected_airport);
		        selectedAirportView.setText(Settings.selectedAirport.getNavn());
		        selectedAirportEvent();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	class DownloaderTask extends AsyncTask<String, Integer, List<Flight>> implements Runnable {
		@Override
		// Actual download method, run in the task thread
		protected List<Flight> doInBackground(String... params) {
			this.publishProgress(0);
			// params comes from the execute() call: params[0] is the url.
			return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(FlightActivity.this));
		}

		@Override
		protected void onPostExecute(List<Flight> newFlights) {
			updateFlights(newFlights);
			progressBar.setVisibility(ProgressBar.GONE);
			super.onPostExecute(newFlights);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... arg0) {
			progressBar.setVisibility(ProgressBar.VISIBLE);
			super.onProgressUpdate(arg0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			new DownloaderTask().execute(AvinorWebService.FLIGHT_URL);
		}
	}
	
	
}
