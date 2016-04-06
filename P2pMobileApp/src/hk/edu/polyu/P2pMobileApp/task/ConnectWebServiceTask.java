package hk.edu.polyu.P2pMobileApp.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import hk.edu.polyu.P2pMobileApp.R;
import hk.edu.polyu.P2pMobileApp.json.User;

public class ConnectWebServiceTask extends AsyncTask<String, String, Boolean> {
	private static final String TAG = "ConnectWebServiceTask";
	
	private Context mContext;
	private AsyncTaskCallback mCallback;
	
	public ConnectWebServiceTask(Context context, AsyncTaskCallback callback) {
		mContext = context;
		mCallback = callback;
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "pre-execute");
    }
	
	@Override
	protected Boolean doInBackground(String... params) {
		String service = params [3];
		Log.d(TAG, "requested webservice: " + service);
		
		if (service.equals(mContext.getString(R.string.webservice_create_user))) {
			return createUserRequest(params);
		} else if (service.equals(mContext.getString(R.string.webservice_get_user))) {
			return getUserRequest(params);
		} else if (service.equals(mContext.getString(R.string.webservice_send_money))) {
			return sendMoneyRequest(params);
		} else {
			return dummyRequest(params);
		}
	}

    @Override
    protected void onPostExecute(Boolean result) {
    	Log.d(TAG, "exeution result: " + result);
    	mCallback.callback(result);
    }
	
    protected Boolean createUserRequest(String... params) {
    	Boolean result = Boolean.valueOf(false);
    	
    	String protocol = params [0];
    	String url = params[1];
    	int port = Integer.parseInt(params[2]);
    	String service = params [3];
    	
    	BufferedReader br = null;
    	
		try {
			URL mUrl = new URL(protocol, url, port, service);
			Log.d(TAG, "connecting to: " + mUrl.toString());
	        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setUseCaches(false); 
			conn.setRequestProperty("Content-Type","application/json");
			
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
			
		    int responseCode = conn.getResponseCode();
		    Log.d(TAG, "The response code is: " + responseCode);
		    
			if (responseCode == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
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
			
		} finally {
		    // Makes sure that the reader is closed after the app is
		    // finished using it.
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					Log.e(TAG, ioe.getMessage(), ioe);
				}
			}
		}
		
		return result;
    }
    
    protected Boolean getUserRequest(String...params) {
    	Boolean result = Boolean.valueOf(false);
    	
    	String protocol = params [0];
    	String url = params[1];
    	int port = Integer.parseInt(params[2]);
    	String service = params [3];
    	
    	String phone = params[4];
    	String name = params[5];
    	
    	BufferedReader br = null;
    	
		try {
			URL mUrl = new URL(protocol, url, port, service + "/" + phone);
			Log.d(TAG, "connecting to: " + mUrl.toString());
	        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setUseCaches(false);
			
			// simply request from server
	    	conn.setRequestMethod("GET");
	    	conn.setDoInput(true);
	    	conn.connect();
	    	
		    int responseCode = conn.getResponseCode();
		    Log.d(TAG, "The response code is: " + responseCode);
		    
			if (responseCode == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				Log.d(TAG, "server response: " + sb.toString());
				
				// convert the incoming JSON message body into POJO
				ObjectMapper jackson = new ObjectMapper();
				User user = jackson.readValue(sb.toString(), User.class);
				
				if (user!=null && user.getName()!=null && !user.getName().equals("")) {
					// phone record is found from DB
					if (name!=null && !name.equals("")) {
						// compare server returned name and user input name
						if (user.getName().equals(name)) {
							result = true;
						}
					} else {
						// name is not supplied in the request, skip compare the name
						result = true;
					}
				}
				
			} else {
				Log.d(TAG, "error response: " + conn.getResponseMessage());
			}
		    
		} catch (Exception exc) {
			Log.e(TAG, exc.getMessage(), exc);
			
		} finally {
		    // Makes sure that the reader is closed after the app is
		    // finished using it.
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					Log.e(TAG, ioe.getMessage(), ioe);
				}
			}
		}
		
		return result;
    }
    
    protected Boolean sendMoneyRequest(String...params) {
    	Boolean result = Boolean.valueOf(false);
    	
    	String protocol = params [0];
    	String url = params[1];
    	int port = Integer.parseInt(params[2]);
    	String service = params [3];
    	
    	BufferedReader br = null;
    	
    	try {
    		// before submitting the transaction, validate the recipient is a registered P2P user
    		Boolean isRecipientValid = getUserRequest(new String[] {params[0], params[1], params[2], mContext.getString(R.string.webservice_get_user), params[7], ""});
    		if (!isRecipientValid) {
    			Log.e(TAG, "un-identified recipient phone number: " + params[7]);
    			// abort the transaction
    			return false;
    		}
    		
			URL mUrl = new URL(protocol, url, port, service);
			Log.d(TAG, "connecting to: " + mUrl.toString());
	        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setUseCaches(false); 
			conn.setRequestProperty("Content-Type","application/json");
			
	    	// output data to server
	    	conn.setRequestMethod("POST");
	    	conn.setDoOutput(true);
	    	conn.setDoInput(true);
	    	
	    	JSONObject jsonData = new JSONObject();
	    	jsonData.put("senderPhone", params[4]);
	    	jsonData.put("currency", params[5]);
	    	jsonData.put("amount", params[6]);
	    	jsonData.put("receiverPhone", params[7]);
	    	jsonData.put("message", params[8]);
	    	Log.d(TAG, "json data: " + jsonData);
	    	
	    	OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
	    	outputStream.write(jsonData.toString());
	    	outputStream.flush();
			
		    int responseCode = conn.getResponseCode();
		    Log.d(TAG, "The response code is: " + responseCode);
		    
			if (responseCode == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
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
    		
		} finally {
		    // Makes sure that the reader is closed after the app is
		    // finished using it.
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					Log.e(TAG, ioe.getMessage(), ioe);
				}
			}
		}
    	
    	return result;
    }
    
    protected Boolean dummyRequest(String...params) {
    	Boolean result = Boolean.valueOf(false);
    	
    	String protocol = params [0];
    	String url = params[1];
    	int port = Integer.parseInt(params[2]);
    	String service = params [3];
    	
		try {
			URL mUrl = new URL(protocol, url, port, service);
			Log.d(TAG, "connecting to: " + mUrl.toString());
	        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setUseCaches(false); 
			conn.setRequestProperty("Content-Type","application/json");
			
	    	// simply request from server
	    	conn.setRequestMethod("GET");
	    	conn.setDoInput(true);
	    	conn.connect();
	    	
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
    
}
