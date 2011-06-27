package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.util.adapter.DeparturesAdapter;

import java.util.ArrayList;

import android.os.Bundle;

@SuppressWarnings("unchecked")
public class Arrivals extends FlytidActivity {
	
	private ArrayList<Flight> flights;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.arrivals);
    	super.onCreate(savedInstanceState);
    	
    	Object object = savedInstanceState != null ? savedInstanceState.get("flights") : null;
    	if(object != null) {
    		flights = (ArrayList<Flight>) object;
    	}

    	initAdapter();
    	tracker.trackPageView("/" + Arrivals.class.getSimpleName());
    }

	@Override
	protected void initAdapter() {
		final Object oldFlights = getLastNonConfigurationInstance();
        if(flights != null) {
        	adapter = new DeparturesAdapter(this, flights);
        	list.setAdapter(adapter);
        }
        else if(oldFlights != null) {
        	adapter = new DeparturesAdapter(this, (ArrayList<Flight>)oldFlights);
        	list.setAdapter(adapter);
        }
        else {
        	adapter = new DeparturesAdapter(this, new ArrayList<Flight>());
        	list.setAdapter(adapter);
        	if(!loadingBaseData) {
        		downloadFlights();
        	}
        }
	}
}