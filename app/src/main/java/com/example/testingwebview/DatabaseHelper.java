package com.example.testingwebview;

/**
 * Created by Anchal on 25-Nov-18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bookmark_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(BookmarkList.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + BookmarkList.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insertBookmark(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(BookmarkList.COLUMN_URL, note);

        // insert row
        db.insert(BookmarkList.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
    }

    public List<BookmarkList> getAllBoomark() {
        List<BookmarkList> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + BookmarkList.TABLE_NAME + " ORDER BY " +
                BookmarkList.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BookmarkList note = new BookmarkList();
                note.setId(cursor.getInt(cursor.getColumnIndex(BookmarkList.COLUMN_ID)));
                note.setUrl(cursor.getString(cursor.getColumnIndex(BookmarkList.COLUMN_URL)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(BookmarkList.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getBookmarkCount() {
        String countQuery = "SELECT  * FROM " + BookmarkList.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

}
