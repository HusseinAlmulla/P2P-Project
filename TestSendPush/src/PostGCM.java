import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class PostGCM {
	
	public static void main(String [] args) {
		//API key provided by Google Console
		//578005844236 <!-- Jeffrey personal sender ID -->
		String apiKey = "AIzaSyBScB61QCzaFwlPISOaP9sivsxrFSiSERw"; // Jeffrey's personal
		
		// Core OTA Test Build Sender ID 947430522853
		//String apiKey = "AIzaSyCHKX8v40OLjceF3h4DqPZIDECTa4ValYc"; // Core OTA App Test build
		// Core OTA Prod Build Sender ID 98814489477
		//String apiKey = "AIzaSyC8M9e8-hjIurQNT7CNGKpBAHKPJRp3R8A"; // Core OTA App Prod build 
		
		// C2DM RegID
		//APA91bE3YPrO3xgzhwJme1wXUNvEEglDRMqsQP08-jhCL0SZGqHORFNOQfU7pF7YMz9KlCwU-m3f7OUDK8VHd2pGlm20RChgF0VfahBTtcCdNIkrCM5IZ6M
		//APA91bHKm32sVztEjRacecc1dbQaqb-jXXLQkICQlNQqzpRsGWMaF3AWVDN-ygQcbdo6I-8Z046yc3CBkzeqBS-t2uwio-CftrNlj3EDcq9gZzCzq4U8AMc
		//APA91bHedg1NoxfjMDYJdBHsm9tjrepa7AZ6H2HKCbbYCB3jSOm3JEil09awGMoEzlg_gTpjp-758TExq9vXhSU5n6IeidL6pFk2DrL15--brQn3kzruBkshQ8Q_t1lEiGMgbRnl6Kup
		//APA91bExI1Hk45RAsBd7yzW-r11_V6b2gYTd84uw_Ih8mymzEj8qiZjXucWmeCqvSCwX-pIigc8RA7TpBCPKVbwwyxluYM90h456Yaqswy0C6CcShEqQ3u0VdIbIGkC17UmpGs4kNw4cm1YuIh9-zJyYuo7Q9Pl8qw
		
		//GCM Registration Token: 
		//etZvOxR7wG0:APA91bFUeRm-GA8wcB93qVJIJ0UAwybFdm_7pn9_c5MrzJbY_YB0usowGJDQFxlH8bXfuJa42gMPcOx-bv4YjN_Rl5PbwU0J4blDrvXRN0i5ja5tUAuZOqc1pVBZPQonztD_WSBeXtEp
		//dX9UDONYRPI:APA91bG_qmAQyhcTwcCfjBC129eXLYlZlzpvwAyS6lAlmfHw-cRyVvOrh1XbkLTXLHSeEIWh67XfgvQ_BLUn6aQjfcYYZXY9Lm3PJS1WHazOOwu1tuaWDCHzvUXtIvZBcfh3OX8vuZdn
		//c_a4A6kg-gs:APA91bGK5233Z0Uy03HHavQt5jLa3GIoVwKx9nz0C2yhM5u-q9NRq80zrvwXNxHkvpFIoCmfKWMU4sFmfJHG1I3z85d-eikg_BQhcKIQa5l5tQBQ7hdJf86yqghXBhwp_SNcR6aA9XdW
		//eS3VxiYv32A:APA91bG-Z3bXu56oM6r475YHqYXFE3bJDGLUwVPG--6W0fyT3Wv2WgCHi0dfFK8c1pcp9m8K-3oennQhemaMuNOwbKY5uuIoOqNZw2kWBJU-dcmhRSHom3KB3PfPJbj6OLV_zbY6EfL3
		//ck6ub-CX0lA:APA91bF2Z1dYjRD8c7dGSd3_nDdN5sgQD2eRwS2cBfTY9-UUTBWQJ2Mxp-HVY1OxWv_iYh5UdH4TCKoowP724q2_VFmYSYZPGVPQ2qzbZ6C4yuqIonjloZx-vvDcrtP9_3Bg-H3RET1J
		//f-OaWaI4K9U:APA91bG24C5m2SbDYYsyiF6wFGG9vxLypVk6pXF5lfjGYBehjn3MHiugqWhvaRJfD5N873ghnhl3EcEiAMoKl7TtimT2G3DG9_y4ZgNHKyanPhH3RnQnHh4LTQiA2fgUS45K3TfH40la
		//f7X9V9xkapA:APA91bEHyBQx76fpVLA8E_m8t0Vbgmq4xV9XgKbjiL1EhXvTMmrgdblG9g-NMaq_r6c3WvZm_J6CRR30P-H0fHldIy5DWxyp7NLRI93dL9rlV4kaf9t7DP3_YNM-gz19oR_F_pqXq95w
		//dECnndjv9o0:APA91bFS4ylAVGRmwE2I3FWdcDa5EusKAQwSTwZSSLGf_qqkRZRXdmF3X662E-xlxVP525Lic9fR-KriheAJUplgeTBAFUe_Q9bJ6HW1VxTNrGAfc3X4jP_QNUdCR86zkIq411Ss3Bs6
		//e9rlgVDx1no:APA91bGWNTM2lF2Aqtccdy4QIWZuzxTWGpMoepTUHJGKyQj2nn-gd6Utxm89mSl_ZPnlOr0I1yZbzANg_x7rcM3Qu6DFFHGJi7DS352rTJ_QEhl4WpPdZNs2qB0tZc2DNNl4oEvRHxEg
		//dhZ899X0j3U:APA91bGJu9EnFFIsYF0GjNITn2-KtqROAaNLnXyW5JFJndIZS_T-FZRwPdyG9p4uPIhe-62KNOecgVFIgXlNX0fYMlX0Rucv3M4SSFjJPgZr0FEZy2tBeMdoXZTI9HGYlWQjBJv5wskM
		
		//eTxMbRbyBJM:APA91bFUe4hS9Yu_GNe4UJh2B9Oo-8KFuwy_fR_Nv2_d4Kw0ggvpQIxgCIAvtCzGS9Cgxw2frff7aHCQs0CXhqZI0ugelY-nulGJGndeEqfo0Pg-jerxsEAg9YXEPzZqj7J4BDL-Mnch
		//c2Y5E4CACrM:APA91bH6O0hAcuhDsIBq9L8C2Z_rfSU58fayjfURj3jJLa8KRpMq3h6W4nq9fLYPKTuaN23XRfzG65zwYCh4slp4HJBr-gVMbKCTNhfWlUVmLM3i0I7G-mWJN76kXnJHw-VBm9dgXPzz
		//eXq9XTXuBTM:APA91bH_xnNZb3lah_yTLj2ngYs0e8qM5CwIE5-nrCGJ5ua4WtWktaGmrYf1KaWno4MYnbJhfo5IpeOm2DtPdIJGROefmsJAuAgluDB2eSWiQOqUUzbskLfgonlU59YltI4qvvWha8DX
		//eco1fodZIks:APA91bFcXEGuAi_3EIBe30vFiH5TongJfczSAMGnI4GeQxGRWdueBFrhsilQ-0jJitxHPbX58LsZ8dK8oSEstLEnWM7Y4TKQ9kRBxJ8HzA-kmPkZI0gP7Fk6nCCCCHedoDJOvAIlpE2D
		//dhBZYOjQVVU:APA91bHedg1NoxfjMDYJdBHsm9tjrepa7AZ6H2HKCbbYCB3jSOm3JEil09awGMoEzlg_gTpjp-758TExq9vXhSU5n6IeidL6pFk2DrL15--brQn3kzruBkshQ8Q_t1lEiGMgbRnl6Kup 
		
		//dEPUst5__Lc:APA91bFrwPwB70tzF9X1mmWz2kuYWMe8vf8eTNL-VSrZ_E8D4pSdgttjgLSzOB74z2cwPvscjyKKRx57MboXz6qS3GqgcW5iEnZ1QQLEbXkRiDSDzoksflq4rEaXbUZQt1UzLB0C9RuA
		
		//ehv_X2GUofs:APA91bGvjU9up-cMQw9hoRYcHQkSP3QFWabSoFuAxdMMea7CVd8bJsygTKC9D1Ix_lSbtqPmSdZvfCKBHwuAYM8Cn69AvMchZ52ap3cEFnibEoJ8Ty5_vm6M2uvbKfHAjgZ-4Mp7JIV6
		//f1LkYTGoLXM:APA91bEKmNhQUCFQgVoyOlsKJCjs4xpy4qQI01jR1JA_oLqf6ndKXfy19q7Fl4WCxHJ5eU02S3YCJ9Ony9IX8s8UGvOVAxc2Pm830liJuIYziPz_skdodyoitdxhmHAuIF643F95zEPP
		//f1LkYTGoLXM:APA91bEHlWFurndQKGDdhH-E0UAhcmFQnwOvC1cMwaDt5MXeOAfhD0mpoWJ8i8oC4nktP0PMWieEzRTarBlNsLyAAP7dkOJ5E3RkLwsHYEYrrHXeEZ5iiezNJpi6OQZ3c9sPF2qtjdjM

		
		args = new String [] {"testing msg from Eclipse", 
				"ehv_X2GUofs:APA91bGvjU9up-cMQw9hoRYcHQkSP3QFWabSoFuAxdMMea7CVd8bJsygTKC9D1Ix_lSbtqPmSdZvfCKBHwuAYM8Cn69AvMchZ52ap3cEFnibEoJ8Ty5_vm6M2uvbKfHAjgZ-4Mp7JIV6"};
		//args = new String [] {"testing msg from Eclipse"};
		
        // Prepare JSON containing the GCM message content. What to send and where to send.
        JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("message", args[0]); // fields required by new GCM endpoint
//        jData.put("id", "B2000000099"); // fields required by legacy C2DM Post Request
//        jData.put("body", "body"); // fields required by legacy C2DM Post Request
//        jData.put("param", "e:003,c:sp"); // fields required by legacy C2DM Post Request
        
        // Where to send GCM message.
        if (args.length > 1 && args[1] != null) {
        	//jGcmData.put("registration_ids", new String[] {args[1].trim()}); // legacy C2DM Post Request
            jGcmData.put("to", args[1].trim()); // new GCM Post Request
        } else {
            jGcmData.put("to", "/topics/global");
        }
        // What to send in GCM message.
        jGcmData.put("data", jData);
		System.out.println(jGcmData);
		
		try {
			URL url;
			//url = new URL("https://android.googleapis.com/gcm/send"); // legacy C2DM endpoint
			url = new URL("https://gcm-http.googleapis.com/gcm/send"); // new GCM endpoint
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key="+apiKey); 
			conn.setDoOutput(true);
			
			System.out.println("\nSending 'POST' request to URL : " + url);
			
            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());
			
			int responseCode = conn.getResponseCode();
	        System.out.println("Response Code : " + responseCode);
            
            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
            
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine; 
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        
	        // 7. Print result
	        System.out.println(response.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
