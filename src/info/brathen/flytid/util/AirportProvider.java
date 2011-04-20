package info.brathen.flytid.util;

import info.brathen.flytid.domain.Airport;
import android.content.Context;

import com.db4o.ObjectSet;

public class AirportProvider extends Db4oProvider<Airport> {

	public AirportProvider(Class<Airport> class1, Context ctx) {
		super(class1, ctx);
	}
	
	public Airport findAirportByCode(String code) {
		Airport ex = new Airport();
		ex.setCode(code);
		
		ObjectSet<Airport> result = db().queryByExample(ex);
		if(result.size() == 1) {
			return result.get(0);
		}
		else {
			return null;
//			throw new IllegalArgumentException("Multiple hits on primary key");
		}
	}
}
