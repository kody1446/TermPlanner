package com.example.termplanner;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBProvider  extends ContentProvider {
    // auth path and strings
    private static final String AUTHORITY = "com.example.termplanner.DBProvider";
    private static final String TERM_PATH = "termTable";
    private static final String COURSE_PATH = "courseTable";
    private static final String COURSE_ALERT_PATH = "courseAlertTable";
    private static final String COURSE_NOTE_PATH = "courseNoteTable";
    private static final String ASSESSMENT_PATH = "assessmentTable";
    private static final String ASSESSMENT_ALERT_PATH = "assessmentAlertTable";
    private static final String ASSESSMENT_NOTE_PATH = "assessmentNoteTable";

    //uri
    public static final Uri TERM_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri COURSE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
    public static final Uri COURSE_ALERT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_ALERT_PATH);
    public static final Uri COURSE_NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_NOTE_PATH);
    public static final Uri ASSESSMENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);
    public static final Uri ASSESSMENT_ALERT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_ALERT_PATH);
    public static final Uri ASSESSMENT_NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_NOTE_PATH);

    //constants
    private static final int TERMS = 1;
    private static final int TERMS_ID = 2;
    private static final int COURSES = 3;
    private static final int COURSES_ID = 4;
    private static final int COURSE_ALERTS = 5;
    private static final int COURSE_ALERTS_ID = 6;
    private static final int COURSE_NOTES = 7;
    private static final int COURSE_NOTES_ID = 8;
    private static final int ASSESSMENTS = 9;
    private static final int ASSESSMENTS_ID = 10;
    private static final int ASSESSMENT_ALERTS = 11;
    private static final int ASSESSMENT_ALERTS_ID = 12;
    private static final int ASSESSMENT_NOTES = 13;
    private static final int ASSESSMENT_NOTES_ID = 14;

    //uri matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_ALERT_PATH, COURSE_ALERTS);
        uriMatcher.addURI(AUTHORITY, COURSE_ALERT_PATH + "/#", COURSE_ALERTS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTE_PATH, COURSE_NOTES);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTE_PATH + "/#", COURSE_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH + "/#", ASSESSMENTS_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_ALERT_PATH, ASSESSMENT_ALERTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_ALERT_PATH + "/#", ASSESSMENT_ALERTS_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_NOTE_PATH, ASSESSMENT_NOTES);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_NOTE_PATH + "/#", ASSESSMENT_NOTES_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        db = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String selection, @Nullable String[] strings1, @Nullable String s1) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.query(DBOpenHelper.TERM_TABLE,DBOpenHelper.TERM_COLUMNS, selection,null,null,null,DBOpenHelper.TERM_ID + " DESC");
            case COURSES:
                return db.query(DBOpenHelper.COURSE_TABLE,DBOpenHelper.COURSE_COLUMNS, selection,null,null,null,DBOpenHelper.COURSE_ID + " DESC");
            case COURSE_ALERTS:
                return db.query(DBOpenHelper.COURSE_ALERT_TABLE,DBOpenHelper.COURSE_ALERTS_COLUMNS, selection,null,null,null,DBOpenHelper.COURSE_ALERT_ID + " DESC");
            case COURSE_NOTES:
                return db.query(DBOpenHelper.COURSE_NOTE_TABLE,DBOpenHelper.COURSE_NOTES_COLUMNS, selection,null,null,null,DBOpenHelper.COURSE_NOTE_ID + " DESC");
            case ASSESSMENTS:
                return db.query(DBOpenHelper.ASSESSMENT_TABLE,DBOpenHelper.ASSESSMENT_COLUMNS, selection,null,null,null,DBOpenHelper.ASSESSMENT_ID + " DESC");
            case ASSESSMENT_ALERTS:
                return db.query(DBOpenHelper.ASSESSMENT_ALERT_TABLE,DBOpenHelper.ASSESSMENT_ALERTS_COLUMNS, selection,null,null,null,DBOpenHelper.ASSESSMENT_ALERT_ID + " DESC");
            case ASSESSMENT_NOTES:
                return db.query(DBOpenHelper.ASSESSMENT_NOTE_TABLE,DBOpenHelper.ASSESSMENT_NOTES_COLUMNS, selection,null,null,null,DBOpenHelper.ASSESSMENT_NOTE_ID + " DESC");
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                id = db.insert(DBOpenHelper.TERM_TABLE, null, contentValues);
                return uri.parse(TERM_PATH + "/" + id);
            case COURSES:
                id = db.insert(DBOpenHelper.COURSE_TABLE, null, contentValues);
                return uri.parse(COURSE_PATH + "/" + id);
            case COURSE_ALERTS:
                id = db.insert(DBOpenHelper.COURSE_ALERT_TABLE, null, contentValues);
                return uri.parse(COURSE_ALERT_PATH + "/" + id);
            case COURSE_NOTES:
                id = db.insert(DBOpenHelper.COURSE_NOTE_TABLE, null, contentValues);
                return uri.parse(COURSE_NOTE_PATH + "/" + id);
            case ASSESSMENTS:
                id = db.insert(DBOpenHelper.ASSESSMENT_TABLE, null, contentValues);
                return uri.parse(ASSESSMENT_PATH + "/" + id);
            case ASSESSMENT_ALERTS:
                id = db.insert(DBOpenHelper.ASSESSMENT_ALERT_TABLE, null, contentValues);
                return uri.parse(ASSESSMENT_ALERT_PATH + "/" + id);
            case ASSESSMENT_NOTES:
                id = db.insert(DBOpenHelper.ASSESSMENT_NOTE_TABLE, null, contentValues);
                return uri.parse(ASSESSMENT_NOTE_PATH + "/" + id);
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.delete(DBOpenHelper.TERM_TABLE, s, strings);
            case COURSES:
                return db.delete(DBOpenHelper.COURSE_TABLE, s, strings);
            case COURSE_ALERTS:
                return db.delete(DBOpenHelper.COURSE_ALERT_TABLE, s, strings);
            case COURSE_NOTES:
                return db.delete(DBOpenHelper.COURSE_NOTE_TABLE, s, strings);
            case ASSESSMENTS:
                return db.delete(DBOpenHelper.ASSESSMENT_TABLE, s, strings);
            case ASSESSMENT_ALERTS:
                return db.delete(DBOpenHelper.ASSESSMENT_ALERT_TABLE, s, strings);
            case ASSESSMENT_NOTES:
                return db.delete(DBOpenHelper.ASSESSMENT_NOTE_TABLE, s, strings);
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.update(DBOpenHelper.TERM_TABLE, values, s, strings);
            case COURSES:
                return db.update(DBOpenHelper.COURSE_TABLE, values, s, strings);
            case COURSE_ALERTS:
                return db.update(DBOpenHelper.COURSE_ALERT_TABLE, values, s, strings);
            case COURSE_NOTES:
                return db.update(DBOpenHelper.COURSE_NOTE_TABLE, values, s, strings);
            case ASSESSMENTS:
                return db.update(DBOpenHelper.ASSESSMENT_TABLE, values, s, strings);
            case ASSESSMENT_ALERTS:
                return db.update(DBOpenHelper.ASSESSMENT_ALERT_TABLE, values, s, strings);
            case ASSESSMENT_NOTES:
                return db.update(DBOpenHelper.ASSESSMENT_NOTE_TABLE, values, s, strings);
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

}
