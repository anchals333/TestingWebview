package com.example.testingwebview;

/**
 * Created by Anchal on 25-Nov-18.
 */
public class BookmarkList {

    static final String TABLE_NAME = "bookmarks";

    static final String COLUMN_ID = "id";
    static final String COLUMN_URL = "url";
    static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String url;
    private String timestamp;


    // Create table SQL query
    static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_URL + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    BookmarkList() {
    }

    BookmarkList(int id, String url, String timestamp) {
        this.id = id;
        this.url = url;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
