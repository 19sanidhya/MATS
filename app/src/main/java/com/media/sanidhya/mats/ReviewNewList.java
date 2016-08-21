package com.media.sanidhya.mats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanidhya on 7/7/16.
 */
public class ReviewNewList extends AppCompatActivity {

    ArrayList<MovieReviewResponse.Review> reviewArrayList;
    MovieReviewAdapter reviewAdapter;
    ListView reviewListView;
    long movie_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_new_list_view);
        movie_id = getIntent().getLongExtra("movie_id", -1);
        reviewListView = (ListView) findViewById(R.id.review_new_list_view);
        reviewArrayList = new ArrayList<MovieReviewResponse.Review>();
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
                    reviewAdapter = new MovieReviewAdapter(ReviewNewList.this, reviewArrayList);
                    reviewListView.setAdapter(reviewAdapter);
                }

            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                Toast.makeText(ReviewNewList.this, "No Internet Connection!!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
