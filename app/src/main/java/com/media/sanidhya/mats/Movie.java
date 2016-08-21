package com.media.sanidhya.mats;


import java.io.Serializable;

/**
 * Created by sanidhya on 28/6/16.
 */
public class Movie implements Serializable {

    private long id;
    private String title;
    private String overview;
    private String vote_average;
    private String poster_path;
    private String imdb_id;
    private int runtime;
    private String release_date;
    private long revenue;
    private String backdrop_path;
    private String status;
    private int votes;


    public Movie(long id, String title, String overview, String vote_average, String poster_path, String imdb_id, int runtime, String release_date, long revenue, String backdrop_path, String status, int votes) {
        super();
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.imdb_id = imdb_id;
        this.runtime = runtime;
        this.release_date = release_date;
        this.revenue = revenue;
        this.backdrop_path = backdrop_path;
        this.status = status;
        this.votes = votes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
