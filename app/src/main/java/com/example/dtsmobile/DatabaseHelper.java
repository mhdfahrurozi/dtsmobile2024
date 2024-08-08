package com.example.dtsmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns for users
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_COL_1 = "ID";
    private static final String USER_COL_2 = "EMAIL";
    private static final String USER_COL_3 = "PASSWORD";

    // Table and columns for notes
    private static final String NOTES_TABLE_NAME = "notes";
    private static final String NOTES_COL_ID = "id";
    private static final String NOTES_COL_TITLE = "title";
    private static final String NOTES_COL_CONTENT = "content";
    private static final String NOTES_COL_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String createUserTable = "CREATE TABLE " + USER_TABLE_NAME + " ("
                + USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_COL_2 + " TEXT, "
                + USER_COL_3 + " TEXT)";
        db.execSQL(createUserTable);

        // Create notes table
        String createNotesTable = "CREATE TABLE " + NOTES_TABLE_NAME + " ("
                + NOTES_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOTES_COL_TITLE + " TEXT, "
                + NOTES_COL_CONTENT + " TEXT, "
                + NOTES_COL_DATE + " TEXT)";
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        onCreate(db);
    }

    // User table operations
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, email);
        contentValues.put(USER_COL_3, password);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE EMAIL = ?", new String[]{email});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE EMAIL = ? AND PASSWORD = ?", new String[]{email, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    // Notes table operations
    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTES_COL_TITLE, note.getTitle());
        values.put(NOTES_COL_CONTENT, note.getContent());
        values.put(NOTES_COL_DATE, note.getDate());
        long id = db.insert(NOTES_TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOTES_TABLE_NAME,
                new String[]{NOTES_COL_ID, NOTES_COL_TITLE, NOTES_COL_CONTENT, NOTES_COL_DATE},
                NOTES_COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_DATE)));
        cursor.close();
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + NOTES_TABLE_NAME + " ORDER BY " + NOTES_COL_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COL_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_CONTENT)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_DATE)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTES_COL_TITLE, note.getTitle());
        values.put(NOTES_COL_CONTENT, note.getContent());
        values.put(NOTES_COL_DATE, note.getDate());
        return db.update(NOTES_TABLE_NAME, values, NOTES_COL_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTES_COL_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<Note> searchNotes(String keyword) {
        List<Note> notes = new ArrayList<>();
        String searchQuery = "SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " +
                NOTES_COL_TITLE + " LIKE ? OR " + NOTES_COL_CONTENT + " LIKE ? ORDER BY " + NOTES_COL_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COL_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_CONTENT)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COL_DATE)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
}
