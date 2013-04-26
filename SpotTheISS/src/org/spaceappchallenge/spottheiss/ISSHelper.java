/**
 * 
 */
package org.spaceappchallenge.spottheiss;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

/**
 * @author Gerardino
 *
 */
public class ISSHelper {

	public static class FetchForecast extends AsyncTask<Float[], Void, LinkedList<String>> {
		private static final int TIMEOUT_MILLISEC = 1000;
		private static final String URL = "http://api.open-notify.org/iss/v1/";
		private static final int ATTEMPTS = 10;

		private ArrayAdapter<String> adapter;

		public FetchForecast(ArrayAdapter<String> adapter){
			this.adapter = adapter;
		}

		/*
		 * We're going to get the forecast from here
		 */
		public LinkedList<String> AsyncTask(Float[] lat_long) {
			LinkedList<String> responseData = new LinkedList<String>();
			responseData.add("Starting...");
			try {
				// http://androidarabia.net/quran4android/phpserver/connecttoserver.php

				// Log.i(getClass().getSimpleName(), "send  task - start");
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						TIMEOUT_MILLISEC);
				HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				//
				//HttpParams p = new BasicHttpParams();
				// p.setParameter("name", pvo.getName());
				//p.setParameter("user", "1");

				// Instantiate an HttpClient
				HttpClient httpclient = new DefaultHttpClient();
				String url = URL 
						+ "?lat=" + Float.toString(lat_long[0]) 
						+ "&lon=" + Float.toString(lat_long[1])
						+ "&n=" + ATTEMPTS;
				HttpPost httppost = new HttpPost(url);
				HttpGet httpget = new HttpGet(url);

				responseData.add("Fetching: " + url);

				// Instantiate a GET HTTP method
				try {
					//Log.i(getClass().getSimpleName(), "send  task - start");
					//
					/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	                    2);
	            nameValuePairs.add(new BasicNameValuePair("user", "1"));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String responseBody = httpclient.execute(httpget,
							responseHandler);
					responseData.add("Fetched: " + responseBody);
					// Parse
					JSONObject json = new JSONObject(responseBody);
					JSONArray jArray = json.getJSONArray("response");
					responseData.add("Fetched: " + json.toString());

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject e = jArray.getJSONObject(i);
						String duration = e.getString("duration");
						String risetime = e.getString("risetime");

						responseData.add("Duration: " + duration + ", Risetime: "+ risetime);
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Log.i(getClass().getSimpleName(), "send  task - end");

			//} catch (Throwable t) {
			} catch (Exception e) {
				/*Toast.makeText(this, "Request failed: " + t.toString(),
	                Toast.LENGTH_LONG).show();*/
				//responseData.add("Oh oh: " + t.getMessage());
				responseData.add("Oh oh: " + e.getMessage());
				//t.printStackTrace();
			}
			return responseData;
		}

		@Override
		protected void onPostExecute(LinkedList<String> resultData){
			if (adapter != null){
				adapter.clear();
				adapter.addAll(resultData);
			}
		}

		@Override
		protected LinkedList<String> doInBackground(Float[]... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
