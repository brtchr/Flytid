package info.brathen.flytid.activities;

import info.brathen.flytid.R;
import info.brathen.flytid.enums.NorwegianAirports;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class FlytidPreferenceActivity extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        ListPreference airportPreference = (ListPreference) findPreference(getString(R.string.pref_my_airport_key));
        airportPreference.setEntries(NorwegianAirports.getFullNames());
        airportPreference.setEntryValues(NorwegianAirports.getNames());

    }
}
