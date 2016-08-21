package com.media.sanidhya.mats;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sanidhya on 30/6/16.
 */
public class MovieTrailerInfoDownloader extends AsyncTask<String, Void, String[]> {

    MovieTrailerInfoDownloaderListener listener;
    ArrayList<String> tempKeyArrayList = new ArrayList<>();

    public MovieTrailerInfoDownloader(MovieTrailerInfoDownloaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected String[] doInBackground(String... params) {
        if (params.length == 0)
            return null;
        else {
            try {
                StringBuffer buffer = new StringBuffer();
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = (InputStream) urlConnection.getContent();
                if (inputStream == null)
                    return null;
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNext())
                    buffer.append(scanner.nextLine());

                return jasonParser(buffer.toString());

            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
    }

    private String[] jasonParser(String s) {

        try {
            JSONObject map = new JSONObject(s);
            JSONArray trailerInfoArray = map.getJSONArray("results");

            for (int i = 0; i < 3 && i < trailerInfoArray.length(); i++) {
                JSONObject trailerInfoObj = trailerInfoArray.getJSONObject(i);
                String trailer_key = trailerInfoObj.getString("key");
                tempKeyArrayList.add(trailer_key);
            }
            String[] outputKeyArray = new String[tempKeyArrayList.size()];
            for (int i = 0; i < tempKeyArrayList.size(); i++)
                outputKeyArray[i] = tempKeyArrayList.get(i);
            return outputKeyArray;
        } catch (JSONException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(String[] strings) {
        listener.postTrailerInfoResults(strings);
    }

    public interface MovieTrailerInfoDownloaderListener {
        void postTrailerInfoResults(String[] keyArray);

    }
}
