/**
 * 
 */
package info.brathen.flytid.util.adapter;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.util.DateFormatter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArrivalsAdapter extends FlytidAdapter {
	
	public ArrivalsAdapter(Context context, ArrayList<Flight> flights) {
		super(context, flights);
	}

	/**
	 * Make a view to hold each row.
	 * 
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.flight_list_rows, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.flight = (TextView) convertView.findViewById(R.id.flight);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.belt = (TextView) convertView.findViewById(R.id.gate_belt);
			holder.from = (TextView) convertView.findViewById(R.id.destination_from);
			holder.remarks = (TextView) convertView.findViewById(R.id.remarks);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		holder.flight.setText(flights.get(position).getFlightId());
		holder.time.setText(DateFormatter.displayDate(flights.get(position).getScheduledTime()));
		holder.belt.setText(flights.get(position).getBaggageBand());
		holder.from.setText(flights.get(position).getAirport() == null ? "" : flights.get(position).getAirport()
				.getName());
		holder.remarks.setText(flights.get(position).getRemark());

		return convertView;
	}
	
	static class ViewHolder {
		TextView flight;
		TextView time;
		TextView belt;
		TextView from;
		TextView remarks;
	}
}
