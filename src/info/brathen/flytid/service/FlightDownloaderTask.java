package info.brathen.flytid.service;

import info.brathen.flytid.R;
import info.brathen.flytid.domain.Flight;
import info.brathen.flytid.xml.handler.FlightXmlHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class FlightDownloaderTask extends AsyncTask<String, Void, List<Flight>> {
	public static final long TIMEOUT = 10000;
	public static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
	
	private Context context;
	private ProgressDialog progressDialog;
	
	public FlightDownloaderTask(Context context) {
        this.context = context;
        progressDialog = ProgressDialog.show(context, "", context.getText(R.string.loading), true);
    }

    @Override
    // Actual download method, run in the task thread
    protected List<Flight> doInBackground(String... params) {
    	// params comes from the execute() call: params[0] is the url.
    	return AvinorWebService.avinorXmlService(params[0], new FlightXmlHandler(context));
    }

    @Override
    protected void onPostExecute(List<Flight> flights) {
    	progressDialog.dismiss();
    	
        super.onPostExecute(flights);
    }

}