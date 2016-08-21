package com.media.sanidhya.mats;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanidhya on 29/6/16.
 */
public class MovieInfo extends AppCompatActivity implements MovieInfoDownloader.MovieInfoDownloaderListener, MovieTrailerInfoDownloader.MovieTrailerInfoDownloaderListener {

    ImageView movieThumbnailiv;
    ImageView movieBackdropiv;
    TextView titletv;
    TextView overviewtv;
    TextView voteAveragetv;
    TextView runtimetv;
    ImageView imdbLinkib;
    TextView releaseDatetv;
    TextView revenuetv;
    TextView votestv;
    TextView moreReviewstv;
    TextView noreviewtv;
    ProgressDialog progressDialog;
    ByteArrayOutputStream bitmapOutBytes;
    MoviesDB moviesDB;
    ArrayList<MovieReviewResponse.Review> reviewArrayList;
    ListView reviewListView;
    MovieReviewAdapter reviewAdapter;
    ArrayList<ImageView> trailers = new ArrayList<>();
    ArrayList<String> trailersKey = new ArrayList<>();
    long movie_id;
    int netFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info);
        movieThumbnailiv = (ImageView) findViewById(R.id.movie_thumbnail_imageView);
        movieBackdropiv = (ImageView) findViewById(R.id.movie_backdrop_iv);
        titletv = (TextView) findViewById(R.id.movie_title_tv);
        overviewtv = (TextView) findViewById(R.id.movie_overview_tv);
        voteAveragetv = (TextView) findViewById(R.id.movie_vote_average_tv);
        releaseDatetv = (TextView) findViewById(R.id.movie_release_date_tv);
        revenuetv = (TextView) findViewById(R.id.movie_revenue_tv);
        votestv = (TextView) findViewById(R.id.movie_votes_tv);
        noreviewtv = (TextView) findViewById(R.id.no_review_tv);
        trailers.add((ImageView) findViewById(R.id.movie_trailer1));
        trailers.add((ImageView) findViewById(R.id.movie_trailer2));
        trailers.add((ImageView) findViewById(R.id.movie_trailer3));
        runtimetv = (TextView) findViewById(R.id.movie_runtime_tv);
        imdbLinkib = (ImageView) findViewById(R.id.movie_imdb_link_ib);
        reviewListView = (ListView) findViewById(R.id.movie_review_lv);
        moreReviewstv = (TextView) findViewById(R.id.more_reviews_tv);
        reviewArrayList = new ArrayList<>();
        bitmapOutBytes = new ByteArrayOutputStream();
//        reviewAdapter=new MovieReviewAdapter(this,reviewArrayList);
//        reviewListView.setAdapter(reviewAdapter);
        movie_id = getIntent().getLongExtra("movieid", -1);

        moviesDB = new MoviesDB(this, "Movies", null, 1);
        SQLiteDatabase readableDatabase = moviesDB.getReadableDatabase();
        Cursor cursor = readableDatabase.query(MovieDBKeys.SINGLE_MOVIE_TABLE_NAME, null, MovieDBKeys.ATTRIBUTE_ID + "=" + movie_id, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MovieInfo.this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                netFlag = 1;

                int runtime = cursor.getInt(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_RUNTIME));
                int votes = cursor.getInt(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_VOTES));
                long revenue = cursor.getLong(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_REVENUE));
                String title = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_TITLE));
                String overview = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_OVERVIEW));
                String vote_average = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_VOTES_AVERAGE));
                String poster_path = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_POSTER));
                String imdb_id = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_IMDB_ID));
                String release_date = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_RELEASE_DATE));
                String backdrop_path = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_BACKDROP));
                String status = cursor.getString(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_STATUS));


                Movie movie = new Movie(movie_id, title, overview, vote_average, poster_path, imdb_id, runtime, release_date, revenue, backdrop_path, status, votes);

                setViews(movie);
            }
        }

        Cursor cursorTrailer = readableDatabase.query(MovieDBKeys.SINGLE_MOVIE_TRAILERS_TABLE, null, MovieDBKeys.ATTRIBUTE_ID + "=" + movie_id, null, null, null, null);
        int i = 1;
        if (cursorTrailer.moveToNext()) {
            while (cursorTrailer.getString(cursorTrailer.getColumnIndex("trailer" + i)) != null) {

                final String trailerKey = cursorTrailer.getString(cursorTrailer.getColumnIndex("trailer" + i));
                Picasso.with(MovieInfo.this).load("http://img.youtube.com/vi/" + trailerKey + "/hqdefault.jpg").into(trailers.get(i - 1));
                trailers.get(i - 1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
                        startActivity(intent);
                    }
                });
                i++;
                if (i == 4)
                    break;
            }
        }

        if (netFlag == 0) {
            String movieInfoUrl = "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=07e6c50063d6e16423e7afcdb0cb8b05";
            MovieInfoDownloader movieInfoDownloader = new MovieInfoDownloader(this);
            movieInfoDownloader.execute(movieInfoUrl);
            String trailerInfoUrl = "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=07e6c50063d6e16423e7afcdb0cb8b05";
            MovieTrailerInfoDownloader trailerInfoDownloader = new MovieTrailerInfoDownloader(this);
            trailerInfoDownloader.execute(trailerInfoUrl);
        }

    }

    public void setViews(final Movie movie) {

        Picasso.with(MovieInfo.this).load("http://image.tmdb.org/t/p/w342" + movie.getBackdrop_path()).into(movieThumbnailiv);
        Picasso.with(MovieInfo.this).load("http://image.tmdb.org/t/p/w342" + movie.getPoster_path()).into(movieBackdropiv);

        titletv.setText(movie.getTitle());
        overviewtv.setText("Overview: " + movie.getOverview());
        voteAveragetv.setText("Ratings :" + movie.getVote_average() + "/10");
        runtimetv.setText("Runtime: " + movie.getRuntime() + "min");
        if (movie.getStatus().equals("Released"))
            releaseDatetv.setText("Release date:" + movie.getRelease_date());
        else
            releaseDatetv.setText("Not released yet!!");
        if (movie.getRevenue() == 0)
            revenuetv.setText("Revenue: NA");
        else
            revenuetv.setText("Revenue: $" + movie.getRevenue());
        votestv.setText("Votes: " + movie.getVotes());

        imdbLinkib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.imdb.com/title/" + movie.getImdb_id() + "/"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void postMovieInfoResults(final Movie movie) {

        if (movie == null) {
            Toast.makeText(MovieInfo.this, "No Internet Connection!!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            setContentView(null);
            return;
        }

        setViews(movie);
        SQLiteDatabase writableDatabase = moviesDB.getWritableDatabase();
        writableDatabase.delete(MovieDBKeys.SINGLE_MOVIE_TABLE_NAME, MovieDBKeys.ATTRIBUTE_ID + "=" + movie_id, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBKeys.ATTRIBUTE_BACKDROP, movie.getBackdrop_path());
        contentValues.put(MovieDBKeys.ATTRIBUTE_POSTER, movie.getPoster_path());
        contentValues.put(MovieDBKeys.ATTRIBUTE_ID, movie.getId());
        contentValues.put(MovieDBKeys.ATTRIBUTE_TITLE, movie.getTitle());
        contentValues.put(MovieDBKeys.ATTRIBUTE_OVERVIEW, movie.getOverview());
        contentValues.put(MovieDBKeys.ATTRIBUTE_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieDBKeys.ATTRIBUTE_RUNTIME, movie.getRuntime());
        contentValues.put(MovieDBKeys.ATTRIBUTE_REVENUE, movie.getRevenue());
        contentValues.put(MovieDBKeys.ATTRIBUTE_STATUS, movie.getStatus());
        contentValues.put(MovieDBKeys.ATTRIBUTE_VOTES, movie.getVotes());
        contentValues.put(MovieDBKeys.ATTRIBUTE_VOTES_AVERAGE, movie.getVote_average());
        contentValues.put(MovieDBKeys.ATTRIBUTE_IMDB_ID, movie.getImdb_id());

        writableDatabase.insert(MovieDBKeys.SINGLE_MOVIE_TABLE_NAME, null, contentValues);

    }


    @Override
    public void postTrailerInfoResults(final String[] keyArray) {


        if (keyArray == null) {
            Toast.makeText(MovieInfo.this, "No Internet Connection!!", Toast.LENGTH_LONG).show();
            return;
        }
        SQLiteDatabase writeableDatabase = moviesDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBKeys.ATTRIBUTE_ID, movie_id);
        for (String s : keyArray) {

            trailersKey.add(s);

        }
        for (int i = 0; i < trailersKey.size(); i++) {
            Picasso.with(MovieInfo.this).load("http://img.youtube.com/vi/" + keyArray[i] + "/hqdefault.jpg").into(trailers.get(i));
            final int finalI = i;
            trailers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + keyArray[finalI]));
                    startActivity(intent);
                }
            });

            contentValues.put("trailer" + (i + 1), keyArray[i]);
        }
        writeableDatabase.insert(MovieDBKeys.SINGLE_MOVIE_TRAILERS_TABLE, null, contentValues);
        progressDialog.dismiss();

        MovieAPIService apiService = MovieReviewClient.getService();
        Call<MovieReviewResponse> reviewResponseCall = apiService.getReviews(movie_id);
        reviewArrayList.clear();
        reviewResponseCall.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {

                if (response.isSuccessful()) {
                    ArrayList<MovieReviewResponse.Review> reviews = response.body().getReviewArrayList();
                    if (reviews == null)
                        return;
                    else {
                        Log.i("reviews length", "" + reviews.size());


                        for (MovieReviewResponse.Review r : reviews)
                            reviewArrayList.add(r);
                        Log.i("reviews length", "" + (reviewArrayList.size()));
                    }

                    if (reviewArrayList.size() == 1) {

                        reviewAdapter = new MovieReviewAdapter(MovieInfo.this, reviewArrayList);
                        reviewListView.setAdapter(reviewAdapter);
                        moreReviewstv.setVisibility(View.INVISIBLE);
                        moreReviewstv.setClickable(false);
                    } else if (reviewArrayList.size() != 0) {

                        reviewAdapter = new MovieReviewAdapter(MovieInfo.this, reviewArrayList);
                        reviewListView.setAdapter(reviewAdapter);
                    } else {
                        noreviewtv.setVisibility(View.VISIBLE);
                        moreReviewstv.setVisibility(View.INVISIBLE);
                        moreReviewstv.setClickable(false);
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                Toast.makeText(MovieInfo.this, "No Internet Connection!!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });

        moreReviewstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieInfo.this, ReviewNewList.class);
                intent.putExtra("movie_id", movie_id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        netFlag = 0;
    }

    @Override
    public void preMovieInfoResults() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movie data!!");
        progressDialog.show();

    }
}
