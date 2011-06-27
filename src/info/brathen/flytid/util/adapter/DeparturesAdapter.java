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

public class DeparturesAdapter extends FlytidAdapter {
		
        public DeparturesAdapter(Context context, ArrayList<Flight> flights) {
        	super(context, flights);
        }

		/**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.flight_list_rows, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.flight = (TextView) convertView.findViewById(R.id.flight);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.gate = (TextView) convertView.findViewById(R.id.gate_belt);
                holder.destination = (TextView) convertView.findViewById(R.id.destination_from);
                holder.remarks = (TextView) convertView.findViewById(R.id.remarks);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access
                holder = (ViewHolder) convertView.getTag();
            }
            
            // Bind the data efficiently with the holder.
            Flight flight = flights.get(position);
            holder.flight.setText(flight.getFlightId());
            holder.time.setText(DateFormatter.displayDate(flight.getScheduledTime()));
            holder.gate.setText(flight.getGate());
            holder.remarks.setText(flight.getRemark());
            if(flight.getViaAirport() != null) {
            	holder.destination.setText(flight.getViaAirport().getName());
            } else {
            	holder.destination.setText(flight.getAirport().getName());
            }
            
            return convertView;
        }

        static class ViewHolder {
        	TextView flight;
        	TextView time;
        	TextView gate;
        	TextView destination;
        	TextView remarks;
        }
    }
