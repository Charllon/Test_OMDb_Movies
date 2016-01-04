package com.charllonlobo.omdbmovies.db_comunication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.charllonlobo.omdbmovies.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "filmsManager";

    // Movies table name
    private static final String TABLE_FILMS = "films";

    // Movies Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_RELEASED = "released";
    private static final String KEY_PLOT = "plot";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_FILMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGE + " TEXT,"
                + KEY_TITLE + " TEXT," + KEY_COUNTRY + " TEXT,"
                + KEY_GENRE + " TEXT," + KEY_RUNTIME + " TEXT,"
                + KEY_RELEASED + " TEXT," + KEY_PLOT + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new movieItem
    public void addContact(MovieItem movieItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, movieItem.getImage()); // MovieItem Name
        values.put(KEY_TITLE, movieItem.getTitle()); // MovieItem Name
        values.put(KEY_COUNTRY, movieItem.getCountry()); // MovieItem Name
        values.put(KEY_GENRE, movieItem.getGenre()); // MovieItem Name
        values.put(KEY_RUNTIME, movieItem.getRuntime()); // MovieItem Name
        values.put(KEY_RELEASED, movieItem.getReleased()); // MovieItem Name
        values.put(KEY_PLOT, movieItem.getPlot()); // MovieItem Name

        // Inserting Row
        db.insert(TABLE_FILMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single movie
    public MovieItem getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FILMS, new String[]{KEY_ID,
                        KEY_IMAGE, KEY_TITLE, KEY_COUNTRY, KEY_GENRE, KEY_RUNTIME, KEY_RELEASED, KEY_PLOT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MovieItem singleMovieItem = new MovieItem();
        if (cursor.getBlob(1) != null)
            singleMovieItem.setImage(cursor.getBlob(1));
        singleMovieItem.setTitle(cursor.getString(2));
        singleMovieItem.setCountry(cursor.getString(3));
        singleMovieItem.setGenre(cursor.getString(4));
        singleMovieItem.setRuntime(cursor.getString(5));
        singleMovieItem.setReleased(cursor.getString(6));
        singleMovieItem.setPlot(cursor.getString(7));

        cursor.close(); // Closing database connection
        return singleMovieItem;
    }

    // Getting All Movies
    public List<MovieItem> getAllMovies() {

        List<MovieItem> movieItemList = new ArrayList<MovieItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieItem movieItem = new MovieItem();
                movieItem.setId(Integer.parseInt(cursor.getString(0)));

                byte[] bytes = cursor.getBlob(1);
                if (bytes != null)
                    movieItem.setImage(bytes);
                movieItem.setTitle(cursor.getString(2));
                movieItem.setCountry(cursor.getString(3));
                movieItem.setGenre(cursor.getString(4));
                movieItem.setRuntime(cursor.getString(5));
                movieItem.setReleased(cursor.getString(6));
                movieItem.setPlot(cursor.getString(7));

                // Adding movieItem to list
                movieItemList.add(movieItem);
            } while (cursor.moveToNext());
        }

        // Closing database connection
        cursor.close();

        // return contact list
        return movieItemList;
    }

    // Updating single movieItem
    public int updateContact(MovieItem movieItem) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, movieItem.getImage());
        values.put(KEY_TITLE, movieItem.getTitle());
        values.put(KEY_COUNTRY, movieItem.getCountry());
        values.put(KEY_GENRE, movieItem.getGenre());
        values.put(KEY_RUNTIME, movieItem.getRuntime());
        values.put(KEY_RELEASED, movieItem.getReleased());
        values.put(KEY_PLOT, movieItem.getPlot());

        // updating row
        return db.update(TABLE_FILMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(movieItem.getId())});
    }

    // Deleting single movieItem
    public void deleteContact(MovieItem movieItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILMS, KEY_ID + " = ?",
                new String[]{String.valueOf(movieItem.getId())});
        db.close();
    }


    // Getting movies Count
    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FILMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
