package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.service.AvinorWebService;
import info.brathen.flytid.util.Settings;
import info.brathen.flytid.util.adapter.ArrivalsAdapter;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Arrivals extends FlytidActivity {
	
//	private ProgressDialog progressDialog = null;
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
        	adapter = new ArrivalsAdapter(this, (List<Map<String, String>>)oldFlights);
        	list.setAdapter(adapter);
        }
        else {
        	adapter = new ArrivalsAdapter(this, new ArrayList<Map<String, String>>());
        	list.setAdapter(adapter);
        	downloadFlights();
        }

    }
    
	public void downloadFlights() {
//		progressDialog = ProgressDialog.show(this, "", this.getText(R.string.loading), true);
		updateButton.setVisibility(ImageButton.GONE);
		progressBar.setVisibility(ProgressBar.VISIBLE);
		
		clearOldFlights();
		
		AsyncTask<String, Integer, List<Map<String, String>>> task = 
			new AsyncTask<String, Integer, List<Map<String, String>>>() {
				@Override
			    // Actual download method, run in the task thread
			    protected List<Map<String, String>> doInBackground(String... params) {
			    	// params comes from the execute() call: params[0] is the url.
			    	return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(Arrivals.this));
			    }
	
			    @Override
			    protected void onPostExecute(List<Map<String, String>> newFlights) {
			    	updateFlights(newFlights);
//			    	progressDialog.dismiss();
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

	protected void updateFlights(List<Map<String, String>> newFlights) {
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