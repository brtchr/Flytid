package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.util.adapter.ArrivalsAdapter;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Arrivals extends FlytidActivity {
	
	private ProgressBar progressBar = null;
	private ArrivalsAdapter adapter;
	private ImageButton updateButton;

	/** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.arrivals);
    	
    	ListView list = (ListView) findViewById(R.id.flight_list);
    	progressBar = (ProgressBar) findViewById(R.id.progress_small);
    	updateButton = (ImageButton) findViewById(R.id.update);
    	
    	TextView selectedAirportView = (TextView)this.findViewById(R.id.selected_airport);
        selectedAirportView.setText(Settings.selectedAirport.getNavn());
    	
    	updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadFlights();
			}
		});
    	
    	final Object oldFlights = getLastNonConfigurationInstance();
        if(oldFlights != null) {
        	adapter = new ArrivalsAdapter(this, (List<Flight>)oldFlights);
        	list.setAdapter(adapter);
        }
        else {
        	adapter = new ArrivalsAdapter(this, new ArrayList<Flight>());
        	list.setAdapter(adapter);
        	downloadFlights();
        }

    }
    
	public void downloadFlights() {
		updateButton.setVisibility(ImageButton.GONE);
		progressBar.setVisibility(ProgressBar.VISIBLE);
		
		clearOldFlights();
		
		AsyncTask<String, Void, List<Flight>> task = 
			new AsyncTask<String, Void, List<Flight>>() {
				@Override
			    // Actual download method, run in the task thread
			    protected List<Flight> doInBackground(String... params) {
			    	// params comes from the execute() call: params[0] is the url.
			    	return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(Arrivals.this));
			    }
	
			    @Override
			    protected void onPostExecute(List<Flight> newFlights) {
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

	protected void updateFlights(List<Flight> newFlights) {
		adapter.setFlights(newFlights);
		adapter.notifyDataSetChanged();
	}

	/* (non-Javadoc)
	 * @see info.brathen.flytid.activities.FlytidActivity#selectedAirportEvent()
	 */
	@Override
	protected void selectedAirportEvent() {
		downloadFlights();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter.getFlights();
	}
	
}