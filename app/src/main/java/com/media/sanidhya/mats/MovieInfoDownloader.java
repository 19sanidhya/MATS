package com.media.sanidhya.mats;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by sanidhya on 30/6/16.
 */
public class MovieInfoDownloader extends AsyncTask<String, Void, Movie> {
    MovieInfoDownloaderListener listener;

    public MovieInfoDownloader(MovieInfoDownloaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected Movie doInBackground(String... params) {
        if (params.length == 0)
            return null;
        else {

            StringBuffer buffer = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null)
                    return null;
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNext()) {
                    buffer.append(scanner.nextLine());
                }
                Log.i("MovieInfo", buffer.toString());
                return jsonParser(buffer.toString());

            } catch (IOException e) {
                return null;
            }
        }
    }

    private Movie jsonParser(String s) {

        Movie movieoutput;

        try {
            JSONObject obj = new JSONObject(s);
            long id = obj.getLong("id");
            String title = obj.getString("title");
            String overview = obj.getString("overview");
            String vote_average = obj.getString("vote_average");
            String poster_path = obj.getString("poster_path");
            String imdb_id = obj.getString("imdb_id");
            int runtime = obj.getInt("runtime");
            String release_date = obj.getString("release_date");
            long revenue = obj.getLong("revenue");
            String backdrop_path = obj.getString("backdrop_path");
            String status = obj.getString("status");
            int votes = obj.getInt("vote_count");


            movieoutput = new Movie(id, title, overview, vote_average, poster_path, imdb_id, runtime, release_date, revenue, backdrop_path, status, votes);

            return movieoutput;
        } catch (JSONException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(Movie movie) {
        listener.postMovieInfoResults(movie);
    }

    @Override
    protected void onPreExecute() {
        listener.preMovieInfoResults();

    }

    public interface MovieInfoDownloaderListener {
        void postMovieInfoResults(Movie movie);

        void preMovieInfoResults();
    }
}
