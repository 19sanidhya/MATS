package com.media.sanidhya.mats;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopularMovieDataDownloader.PopularMovieDataDownloaderListener, ThumbnailLoader.ThumbnailLoaderListener {
    ArrayList<PopularMovies> movieArrayList = new ArrayList<>();
    ArrayList<String> moviesPosterPath = new ArrayList<>();
    MovieThumbnailAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    GridView gridView;
    ByteArrayOutputStream byteArrayOutputStream;
    ProgressDialog progressDialog;
    int netFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_thumbnails_gridview);
        byteArrayOutputStream = new ByteArrayOutputStream();
        gridView = (GridView) findViewById(R.id.gridView);


        MoviesDB moviesDB = new MoviesDB(this, "Movies", null, 1);
        SQLiteDatabase readDatabase = moviesDB.getReadableDatabase();
        Cursor cursor = readDatabase.query(MovieDBKeys.POPULAR_MOVIES_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            movieArrayList.add(new PopularMovies(cursor.getLong(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_ID)),
                    cursor.getBlob(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_POPULAR_POSTER)).toString()));
            long movieiddb = cursor.getLong(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_ID));
            byte[] b = cursor.getBlob(cursor.getColumnIndex(MovieDBKeys.ATTRIBUTE_POPULAR_POSTER));
            bitmaps.add(BitmapFactory.decodeByteArray(b, 0, b.length));
            netFlag = 1;

        }

        adapter = new MovieThumbnailAdapter(MainActivity.this, bitmaps);
        // gridView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        if (netFlag == 0) {
            PopularMovieDataDownloader dataDownloader = new PopularMovieDataDownloader(this);
            dataDownloader.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=07e6c50063d6e16423e7afcdb0cb8b05");
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieInfo.class);
                intent.putExtra("movieid", movieArrayList.get(position).getPopularMovieId());
                startActivity(intent);
            }
        });

    }


    @Override
    public void postResults(PopularMovies[] movie) {

        if (movie == null) {
            progressDialog.dismiss();
            return;
        }
        for (PopularMovies m : movie) {
            movieArrayList.add(m);
        }
        for (int i = 0; i < movieArrayList.size(); i++) {
            moviesPosterPath.add("http://image.tmdb.org/t/p/w342" + movieArrayList.get(i).getPopularMovieThumbnail());
        }

        ThumbnailLoader loader = new ThumbnailLoader(MainActivity.this, MainActivity.this);
        loader.execute(moviesPosterPath);

    }

    @Override
    public void preResults() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading Popular Movies");
        progressDialog.show();
    }

    @Override
    public void postThumbnailLoaderResults(ArrayList<Bitmap> bitmap) {

        if (bitmap == null) {
            progressDialog.dismiss();
            return;
        }
        if (bitmaps.size() != 0)
            bitmaps.clear();
        for (Bitmap b : bitmap)
            bitmaps.add(b);

        MoviesDB moviesDB = new MoviesDB(this, "Movies", null, 1);
        SQLiteDatabase writeDatabase = moviesDB.getWritableDatabase();

        writeDatabase.delete(MovieDBKeys.POPULAR_MOVIES_TABLE_NAME, null, null);

        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < bitmap.size(); i++) {
            contentValues.put(MovieDBKeys.ATTRIBUTE_ID, movieArrayList.get(i).getPopularMovieId());
            byteArrayOutputStream.reset();
            bitmap.get(i).compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] posterBytes = byteArrayOutputStream.toByteArray();
            contentValues.put(MovieDBKeys.ATTRIBUTE_POPULAR_POSTER, posterBytes);
            writeDatabase.insert(MovieDBKeys.POPULAR_MOVIES_TABLE_NAME, null, contentValues);
        }

        adapter.notifyDataSetChanged();


        progressDialog.dismiss();
    }

}
