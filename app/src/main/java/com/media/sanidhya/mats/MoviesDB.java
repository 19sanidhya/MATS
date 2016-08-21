package com.media.sanidhya.mats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sanidhya on 1/7/16.
 */
public class MoviesDB extends SQLiteOpenHelper {


    public MoviesDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Movies", null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + MovieDBKeys.SINGLE_MOVIE_TABLE_NAME + "(" + MovieDBKeys.ATTRIBUTE_ID + " LONG PRIMARY KEY," +
                MovieDBKeys.ATTRIBUTE_TITLE + " TEXT," + MovieDBKeys.ATTRIBUTE_OVERVIEW + " TEXT," + MovieDBKeys.ATTRIBUTE_POSTER + " TEXT," +
                MovieDBKeys.ATTRIBUTE_BACKDROP + " TEXT," + MovieDBKeys.ATTRIBUTE_RELEASE_DATE + " TEXT," +
                MovieDBKeys.ATTRIBUTE_STATUS + " TEXT," + MovieDBKeys.ATTRIBUTE_RUNTIME + " INTEGER," + MovieDBKeys.ATTRIBUTE_REVENUE + " TEXT," +
                MovieDBKeys.ATTRIBUTE_VOTES_AVERAGE + " TEXT," + MovieDBKeys.ATTRIBUTE_VOTES + " INTEGER," + MovieDBKeys.ATTRIBUTE_IMDB_ID + " TEXT);");

        db.execSQL("create table " + MovieDBKeys.POPULAR_MOVIES_TABLE_NAME + "(" + MovieDBKeys.ATTRIBUTE_ID + " LONG PRIMARY KEY," + MovieDBKeys.ATTRIBUTE_POPULAR_POSTER + " BLOB);");

        db.execSQL("create table " + MovieDBKeys.SINGLE_MOVIE_TRAILERS_TABLE + "(" + MovieDBKeys.ATTRIBUTE_ID + " LONG," + MovieDBKeys.ATTRIBUTE_TRAILER_1 + " TEXT," +
                MovieDBKeys.ATTRIBUTE_TRAILER_2 + " TEXT," + MovieDBKeys.ATTRIBUTE_TRAILER_3 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
