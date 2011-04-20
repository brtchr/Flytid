/**
 * 
 */
package info.brathen.flytid.util.adapter;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.util.DateFormatter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArrivalsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Flight> flights;
        
		public ArrivalsAdapter(Context context, List<Flight> flights) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            this.flights = flights;
        }
		
		public List<Flight> getFlights() {
			return flights;
		}

		public void setFlights(List<Flight> flights) {
			this.flights = flights;
		}

        /**
         * The number of items in the list
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
        	if(flights == null) {return 0;}
            return flights.size();
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }
        
        /* (non-Javadoc)
		 * @see android.widget.BaseAdapter#areAllItemsEnabled()
		 */
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		
		/* (non-Javadoc)
		 * @see android.widget.BaseAdapter#isEnabled(int)
		 */
		@Override
		public boolean isEnabled(int position) {
			return false;
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
            holder.time.setText(DateFormatter.displayDate(
            		flights.get(position).getScheduledTime()));
            holder.belt.setText(flights.get(position).getBaggageBand());
            holder.from.setText(flights.get(position).getAirport() == null ? "" : flights.get(position).getAirport().getName());
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
