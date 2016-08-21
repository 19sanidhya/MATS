package com.media.sanidhya.mats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by sanidhya on 7/7/16.
 */
public class MovieReviewAdapter extends BaseAdapter {


    Context context;
    ArrayList<MovieReviewResponse.Review> reviewList;

    public MovieReviewAdapter(Context context, ArrayList<MovieReviewResponse.Review> reviewlist) {

        this.context = context;
        reviewList = reviewlist;
        Log.i("adapter's reviewlist", "" + reviewList.size());
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public MovieReviewResponse.Review getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(reviewList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.movie_review_layout, parent, false);
            TextView authorTv = (TextView) v.findViewById(R.id.author_tv);
            TextView contentTv = (TextView) v.findViewById(R.id.content_tv);
            ReviewHolder holder = new ReviewHolder(authorTv, contentTv);
            v.setTag(holder);
        }

        ReviewHolder holder = (ReviewHolder) v.getTag();
        holder.holderAuthor.setText(reviewList.get(position).getAuthor());
        holder.holderContent.setText(reviewList.get(position).getContent());

        return v;
    }

    class ReviewHolder {
        TextView holderAuthor;
        TextView holderContent;

        public ReviewHolder(TextView holderAuthor, TextView holderContent) {
            this.holderAuthor = holderAuthor;
            this.holderContent = holderContent;
        }
    }
}
