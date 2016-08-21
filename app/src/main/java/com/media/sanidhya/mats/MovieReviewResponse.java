package com.media.sanidhya.mats;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sanidhya on 6/7/16.
 */
public class MovieReviewResponse {

    @SerializedName("results")
    private ArrayList<Review> reviewArrayList;

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }

    public void setReviewArrayList(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    public static class Review {
        private String id;
        private String author;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
