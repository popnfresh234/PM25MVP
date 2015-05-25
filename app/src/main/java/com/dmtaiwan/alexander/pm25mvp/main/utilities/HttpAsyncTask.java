package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexander on 5/22/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, String, JSONArray> {

    @Override
    protected JSONArray doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseString = readStream(urlConnection.getInputStream());
                response = new JSONArray((responseString));
            } else {
                Log.v("PMClient", String.valueOf(responseCode));
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        AsyncTaskResultEvent event = new AsyncTaskResultEvent(jsonArray);
        EventBus.getInstance().post(event);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

