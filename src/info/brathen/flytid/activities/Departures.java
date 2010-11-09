package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.util.adapter.DeparturesAdapter;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Departures extends FlytidActivity {
	
	private ProgressBar progressBar = null;
	private DeparturesAdapter adapter;
	private ScheduledExecutorService scheduler;
	private DownloaderTask downloaderTask;

	/** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.departures);
    	
    	downloaderTask = new DownloaderTask();
    	
    	ListView list = (ListView) findViewById(R.id.flight_list);
    	progressBar = (ProgressBar) findViewById(R.id.progress_small);
//    	updateButton = (ImageButton) findViewById(R.id.update);

//    	updateButton.setOnClickListener(new OnClickListener() {
//    		@Override
//    		public void onClick(View v) {
//    			downloadFlights();
//    		}
//    	});
    	
    	TextView selectedAirportView = (TextView)this.findViewById(R.id.selected_airport);
        selectedAirportView.setText(Settings.selectedAirport.getNavn());
    	
        final Object oldFlights = getLastNonConfigurationInstance();
        if(oldFlights != null) {
        	adapter = new DeparturesAdapter(this, (List<Map<String, String>>)oldFlights);
        	list.setAdapter(adapter);
        }
        else {
        	adapter = new DeparturesAdapter(this, new ArrayList<Map<String, String>>());
        	list.setAdapter(adapter);
        }
        
        
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(downloaderTask, 0, 180, TimeUnit.SECONDS);
    }
    
	public DownloaderTask downloadFlights() {
		progressBar.setVisibility(ProgressBar.VISIBLE);
		clearOldFlights();
		downloaderTask.execute(AvinorWebService.FLIGHT_URL);
		return downloaderTask;
	}

	private void clearOldFlights() {
		if(adapter.getFlights() != null || adapter.getFlights().size() != 0) {
			adapter.getFlights().removeAll(adapter.getFlights());
			adapter.notifyDataSetChanged();
		}
	}

	protected void updateFlights(List<Map<String, String>> newFlights) {
		adapter.setFlights(newFlights);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void selectedAirportEvent() {
		downloadFlights();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter.getFlights();
	}
	
	class DownloaderTask extends AsyncTask<String, Integer, List<Map<String, String>>> implements Runnable {
		@Override
	    // Actual download method, run in the task thread
	    protected List<Map<String, String>> doInBackground(String... params) {
			this.publishProgress(null);
			Log.i("DownloaderTask", "Starter nedlasting av flytider");
	    	// params comes from the execute() call: params[0] is the url.
	    	return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(Departures.this));
	    }

	    @Override
	    protected void onPostExecute(List<Map<String, String>> newFlights) {
	    	updateFlights(newFlights);
//	    	updateButton.setVisibility(ImageButton.VISIBLE);
			progressBar.setVisibility(ProgressBar.GONE);
			Log.i("DownloaderTask", "Avslutter nedlasting av flytider");
	        super.onPostExecute(newFlights);
	    }
	    
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... arg0) {
			progressBar.setVisibility(ProgressBar.VISIBLE);
			super.onProgressUpdate(arg0);
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			new DownloaderTask().execute(AvinorWebService.FLIGHT_URL);
		}
	}
}