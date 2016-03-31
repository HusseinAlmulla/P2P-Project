package hk.edu.polyu.P2pMobileApp.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import hk.edu.polyu.P2pMobileApp.gcm.GCMRegistrationService;

public class ConnectWebServiceTask extends AsyncTask<String, String, Boolean> {
	private static final String TAG = "ConnectWebServiceTask";
	
	private AsyncTaskCallback mCallback;
	
	public ConnectWebServiceTask(AsyncTaskCallback callback) {
		mCallback = callback;
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "pre-execute");
    }
	
	@Override
	protected Boolean doInBackground(String... params) {
		Boolean result = Boolean.valueOf(false);
		
		String protocol = params [0];
		Log.d(TAG, "protocol: " + protocol);
		
		String url = params[1];
		Log.d(TAG, "url: " + url);
		
		int port = Integer.parseInt(params[2]);
		Log.d(TAG, "port: " + port);
		
		String service = params [3];
		Log.d(TAG, "service: " + service);
		
		try {
			URL mUrl = new URL(protocol, url, port, service);
	        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setUseCaches(false); 
			conn.setRequestProperty("Content-Type","application/json");
			
		    if (service.equals("P2pWebServices/rest/createUser")) {
		    	// output data to server
		    	conn.setRequestMethod("POST");
		    	conn.setDoOutput(true);
		    	conn.setDoInput(true);
		    	
		    	JSONObject jsonData = new JSONObject();
		    	jsonData.put("name", params[4]);
		    	jsonData.put("email", params[5]);
		    	jsonData.put("phone", params[6]);
		    	jsonData.put("bank", params[7]);
		    	jsonData.put("account", params[8]);
		    	jsonData.put("deviceToken", params[9]);
		    	Log.d(TAG, "json data: " + jsonData);
		    	
		    	OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
		    	outputStream.write(jsonData.toString());
		    	outputStream.flush();
		    	
		    } else {
		    	// simply request from server
		    	conn.setRequestMethod("GET");
		    	conn.setDoInput(true);
		    	conn.connect();
		    }
		    
		    int responseCode = conn.getResponseCode();
		    Log.d(TAG, "The response code is: " + responseCode);
		    
			if (responseCode == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				Log.d(TAG, "server response: " + sb.toString());
				result = true;
				
			} else {
				Log.d(TAG, "error response: " + conn.getResponseMessage());
			}
		    
		} catch (Exception exc) {
			Log.e(TAG, exc.getMessage(), exc);
		}
		
		return result;
	}

    @Override
    protected void onPostExecute(Boolean result) {
    	Log.d(TAG, "exeution result: " + result);
    	mCallback.callback(result);
    }
	
}
