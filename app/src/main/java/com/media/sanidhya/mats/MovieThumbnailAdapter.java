package com.media.sanidhya.mats;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by sanidhya on 29/6/16.
 */
public class MovieThumbnailAdapter extends RecyclerView.Adapter<MovieThumbnailAdapter.SimpleViewHolder> {

    Context context;
    ArrayList<Bitmap> bitmaps;

    public MovieThumbnailAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }


//
//    @Override
//    public Bitmap getItem(int position) {
//        return bitmaps.get(position);
//    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.movie_thumbnail_for_gridview, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    static class OurHolder {
        ImageView thumbnailImageView;


    }

    static public class MovieThumbnailHolder {
        ImageView thumbnail;

        public MovieThumbnailHolder(ImageView thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v=convertView;
//        if(v==null)
//        {
//            v= LayoutInflater.from(context).inflate(R.layout.movie_thumbnail_for_gridview,parent,false);
//            ImageView thumbnail=(ImageView)v.findViewById(R.id.movies_thumbnail_imageView);
//            MovieThumbnailHolder holder=new MovieThumbnailHolder(thumbnail);
//            v.setTag(holder);
//        }
//
//        MovieThumbnailHolder holder=(MovieThumbnailHolder)v.getTag();
//     holder.thumbnail.setImageBitmap(bitmaps.get(position));
//
//        return v;
//    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {


        public SimpleViewHolder(View itemView) {
            super(itemView);
        }


    }
}
