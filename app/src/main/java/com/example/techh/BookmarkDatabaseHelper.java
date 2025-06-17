package com.example.techh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bookmarks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "bookmarks";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ARTICLE_URL = "article_url";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";

    public BookmarkDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ARTICLE_URL + " TEXT PRIMARY KEY, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBookmark(Bookmark bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, bookmark.getUsername());
        values.put(COLUMN_ARTICLE_URL, bookmark.getArticleUrl());
        values.put(COLUMN_TITLE, bookmark.getTitle());
        values.put(COLUMN_DATE, bookmark.getDate());
        values.put(COLUMN_CATEGORY, bookmark.getCategory());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Delete bookmark by articleUrl & username
    public void deleteBookmark(String username, String articleUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ARTICLE_URL + " = ? AND " + COLUMN_USERNAME + " = ?", new String[]{articleUrl, username});
        db.close();
    }

    public void debugPrintBookmarks() {
        List<Bookmark> bookmarks = getAllBookmarks("Ayushri");
        for (Bookmark b : bookmarks) {
            System.out.println(b.getTitle() + " - " + b.getCategory());
        }
    }


    public boolean isBookmarked(String articleUrl, String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_ARTICLE_URL + "=? AND " + COLUMN_USERNAME + "=?",
                new String[]{articleUrl, username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Get all bookmarks for a particular username
    public List<Bookmark> getAllBookmarks(String username) {
        List<Bookmark> bookmarks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                bookmarks.add(new Bookmark(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTICLE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookmarks;
    }
}
