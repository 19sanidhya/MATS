package com.media.sanidhya.mats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sanidhya on 29/6/16.
 */
public class ThumbnailLoader extends AsyncTask<ArrayList<String>, String, ArrayList<Bitmap>> {

    ThumbnailLoaderListener listener;
    Context context;


    public ThumbnailLoader(ThumbnailLoaderListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(ArrayList<String>... params) {
        if (params.length == 0)
            return null;
        ArrayList<Bitmap> bitmap = new ArrayList<>();
        try {
            for (int i = 0; i < params[0].size(); i++) {
                InputStream inputStream = (InputStream) new URL(params[0].get(i)).getContent();
                Log.i("thumbnail", inputStream.toString());
                bitmap.add(BitmapFactory.decodeStream(inputStream));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmap) {
        listener.postThumbnailLoaderResults(bitmap);
    }

    public interface ThumbnailLoaderListener {
        void postThumbnailLoaderResults(ArrayList<Bitmap> bitmap);
//        void preThumbnailLoaderResults();
    }
}
