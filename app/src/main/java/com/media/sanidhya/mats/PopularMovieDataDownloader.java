package com.media.sanidhya.mats;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by sanidhya on 29/6/16.
 */
public class PopularMovieDataDownloader extends AsyncTask<String, Void, PopularMovies[]> {

    PopularMovieDataDownloaderListener listener;

    public PopularMovieDataDownloader(PopularMovieDataDownloaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected PopularMovies[] doInBackground(String... params) {

        StringBuffer buffer;
        if (params.length == 0)
            return null;
        else {
            buffer = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                if (inputStream == null) {
                    return null;
                }
                while (scanner.hasNext()) {
                    buffer.append(scanner.nextLine());
                }
                Log.i("jasondata", buffer.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return parseJason(buffer.toString());
    }

    private PopularMovies[] parseJason(String s) {

        PopularMovies[] movieoutput;

        try {
            JSONObject map = new JSONObject(s);
            JSONArray moviesarray = map.getJSONArray("results");
            movieoutput = new PopularMovies[moviesarray.length()];
            for (int i = 0; i < moviesarray.length(); i++) {
                JSONObject movie = moviesarray.getJSONObject(i);
                long id = movie.getLong("id");
                String poster_path = movie.getString("poster_path");
                movieoutput[i] = new PopularMovies(id, poster_path);

            }

        } catch (JSONException e) {
            return null;
        }

        return movieoutput;
    }

    @Override
    protected void onPostExecute(PopularMovies[] movies) {
        listener.postResults(movies);
    }

    @Override
    protected void onPreExecute() {
        listener.preResults();
    }

    public interface PopularMovieDataDownloaderListener {
        public void postResults(PopularMovies[] movie);

        void preResults();
    }
}
