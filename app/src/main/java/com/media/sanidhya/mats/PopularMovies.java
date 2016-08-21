package com.media.sanidhya.mats;

/**
 * Created by sanidhya on 4/7/16.
 */
public class PopularMovies {

    String popularMovieThumbnail;
    long popularMovieId;

    public PopularMovies(long popularMovieId, String popularMovieThumbnail) {
        this.popularMovieId = popularMovieId;
        this.popularMovieThumbnail = popularMovieThumbnail;
    }

    public String getPopularMovieThumbnail() {
        return popularMovieThumbnail;
    }

    public void setPopularMovieThumbnail(String popularMovieThumbnail) {
        this.popularMovieThumbnail = popularMovieThumbnail;
    }

    public long getPopularMovieId() {
        return popularMovieId;
    }

    public void setPopularMovieId(long popularMovieId) {
        this.popularMovieId = popularMovieId;
    }
}
