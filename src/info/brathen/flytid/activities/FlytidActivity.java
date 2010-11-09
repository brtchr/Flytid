/**
 * 
 */
package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.enums.ArrDep;
import info.brathen.flytid.enums.NorwegianAirports;
import info.brathen.flytid.util.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Christoffer
 *
 */
public abstract class FlytidActivity extends Activity {

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
//        	case R.id.menu_updateInterval : {
//        		break;
//        	}
        	case R.id.menu_arrival : {
        		if(Settings.arrivalOrDeparture != ArrDep.A) {
        			Settings.arrivalOrDeparture = ArrDep.A;
        			Intent arrivalIntent = new Intent();
        			arrivalIntent.setClass(getApplicationContext(), Arrivals.class);
        			//arrivalIntent.putExtra(name, value);
        			startActivity(arrivalIntent);
        		}
        		break;
        	}
        	case R.id.menu_departure : {
				if (Settings.arrivalOrDeparture != ArrDep.D) {
					Settings.arrivalOrDeparture = ArrDep.D;
					Intent departureIntent = new Intent();
					departureIntent.setClass(getApplicationContext(), Departures.class);
					// departureIntent.putExtra(name, value);
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
		    	Toast.makeText(FlytidActivity.this, NorwegianAirports.valueOf(names[item]).toString(), Toast.LENGTH_SHORT).show();
		        Settings.selectedAirport = NorwegianAirports.valueOf(names[item]);
		        TextView selectedAirportView = (TextView)FlytidActivity.this.findViewById(R.id.selected_airport);
		        selectedAirportView.setText(Settings.selectedAirport.getNavn());
		        selectedAirportEvent();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected abstract void selectedAirportEvent();	
}
