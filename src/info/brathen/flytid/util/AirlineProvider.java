package info.brathen.flytid.util;

import info.brathen.flytid.domain.Airline;
import android.content.Context;

import com.db4o.ObjectSet;

public class AirlineProvider extends Db4oProvider<Airline> {

	public AirlineProvider(Class<Airline> persistentClass, Context ctx) {
		super(persistentClass, ctx);
	}
	
	public Airline findAirlineByCode(String code) {
		Airline ex = new Airline();
		ex.setCode(code);
		
		ObjectSet<Airline> result = db().queryByExample(ex);
		if(result.size() == 1) {
			return result.get(0);
		}
		else {
			return null;
//			throw new IllegalArgumentException("Multiple hits on primary key");
		}
	}
}
