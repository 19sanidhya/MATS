package com.media.sanidhya.mats;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sanidhya on 6/7/16.
 */
public interface MovieAPIService {

    @GET("3/movie/{id}/reviews?api_key=07e6c50063d6e16423e7afcdb0cb8b05")
    Call<MovieReviewResponse> getReviews(@Path("id") long movieId);

}
