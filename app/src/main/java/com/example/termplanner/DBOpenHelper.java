package com.example.termplanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {
    //db info
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "wguplanner.db";

    //term table info
    public static final String TERM_TABLE = "termTable";
    public static final String TERM_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START ="termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_ACTIVE = "termActive";
    public static final String TERM_CREATED = "termCreated";
    public static final String[] TERM_COLUMNS = {TERM_ID, TERM_NAME, TERM_START, TERM_END, TERM_ACTIVE};

    //course table info
    public static final String COURSE_TABLE = "courseTable";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TERM_ID = "courseTermId";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END  = "courseEnd";
    public static final String COURSE_DESCRIPTION = "courseDescription";
    public static final String COURSE_ACTIVE ="courseActive";
    public static final String COURSE_MENTOR ="courseMentor";
    public static final String COURSE_MENTOR_PHONE = "courseMentorPhone";
    public static final String COURSE_MENTOR_EMAIL = "courseMentorEmail";
    public static final String[] COURSE_COLUMNS  = {COURSE_ID, COURSE_TERM_ID, COURSE_NAME, COURSE_START, COURSE_END, COURSE_DESCRIPTION,
    COURSE_ACTIVE, COURSE_MENTOR, COURSE_MENTOR_PHONE,COURSE_MENTOR_EMAIL};

    //assessment table info
    public static final String ASSESSMENT_TABLE = "assessmentTable";
    public static final String ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_COURSE_ID = "assessmentCourseId";
    public static final String ASSESSMENT_TITLE = "assessmentTitle";
    public static final String ASSESSMENT_DESCRIPTION = "assessmentDescription";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_STATE = "assessmentState";
    public static final String ASSESSMENT_DUE_DATE = "assessmentDueDate";
    public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_COURSE_ID, ASSESSMENT_TITLE, ASSESSMENT_DESCRIPTION, ASSESSMENT_TYPE,ASSESSMENT_STATE, ASSESSMENT_DUE_DATE};

    //course note table
    public static final String COURSE_NOTE_TABLE = "courseNoteTable";
    public static final String COURSE_NOTE_ID = "_id";
    public static final String COURSE_NOTE_FK = "courseId";
    public static final String COURSE_NOTE ="courseNote";
    public static final String[] COURSE_NOTES_COLUMNS = {COURSE_NOTE_ID, COURSE_NOTE_FK, COURSE_NOTE};

    //assessment note table
    public static final String ASSESSMENT_NOTE_TABLE ="assessmentNoteTable";
    public static final String ASSESSMENT_NOTE_ID ="_id";
    public static final String ASSESSMENT_NOTE_FK = "assessmentId";
    public static final String ASSESSMENT_NOTE ="assessmentNote";
    public static final String[] ASSESSMENT_NOTES_COLUMNS = {ASSESSMENT_NOTE_ID, ASSESSMENT_NOTE_FK,ASSESSMENT_NOTE};

    //course alert table
    public static final String COURSE_ALERT_TABLE = "courseAlertTable";
    public static final String COURSE_ALERT_ID ="_id";
    public static final String COURSE_ALERT = "courseAlert";
    public static final String COURSE_ALERT_FK = "courseAlertFk";
    public static final String[] COURSE_ALERTS_COLUMNS = {COURSE_ALERT_ID, COURSE_ALERT,COURSE_ALERT_FK};

    //assessment alert table
    public static final String ASSESSMENT_ALERT_TABLE = "assessmentAlertTable";
    public static final String ASSESSMENT_ALERT_ID = "_id";
    public static final String ASSESSMENT_ALERT = "assessmentAlert";
    public static final String ASSESSMENT_ALERT_FK = "assessmentAlertFk";
    public static final String[] ASSESSMENT_ALERTS_COLUMNS = {ASSESSMENT_ALERT_ID,ASSESSMENT_ALERT, ASSESSMENT_ALERT_FK};

    //method for creating db
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create term table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TERM_TABLE + "("+TERM_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TERM_NAME+" TEXT," +
                TERM_START+" TEXT,"+
                TERM_END+" TEXT,"+
                TERM_CREATED+ "TEXT DEFAULT CURRENT_TIMESTAMP, " +
                TERM_ACTIVE+" INTEGER)");

        //create course table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                COURSE_TABLE + "("+ COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COURSE_TERM_ID + " INTEGER,"+
                COURSE_NAME + " TEXT, "+
                COURSE_START + " TEXT, "+
                COURSE_END + " TEXT, "+
                COURSE_DESCRIPTION + " TEXT, "+
                COURSE_ACTIVE +" INTEGER," +
                COURSE_MENTOR +" TEXT," +
                COURSE_MENTOR_EMAIL + " TEXT," +
                COURSE_MENTOR_PHONE + " TEXT," +
                " FOREIGN KEY("+ COURSE_TERM_ID +")" + " REFERENCES " + TERM_TABLE+"(" + TERM_ID +") ON DELETE CASCADE)");

        //create assessment table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                ASSESSMENT_TABLE + "(" + ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ASSESSMENT_COURSE_ID + " INTEGER,"+
                ASSESSMENT_TITLE + " TEXT, " +
                ASSESSMENT_DESCRIPTION + " TEXT," +
                ASSESSMENT_TYPE +" INTEGER, " +
                ASSESSMENT_STATE + " INTEGER, " +
                ASSESSMENT_DUE_DATE + " TEXT, " +
                " FOREIGN KEY("+ASSESSMENT_COURSE_ID +")" +" REFERENCES " + COURSE_TABLE+ "("+COURSE_ID +") ON DELETE CASCADE)");

        //create course note table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                COURSE_NOTE_TABLE + "(" + COURSE_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COURSE_NOTE + " TEXT," +
                COURSE_NOTE_FK + " INTEGER," +
                " FOREIGN KEY("+COURSE_NOTE_FK +")"+ " REFERENCES " + COURSE_TABLE+ "("+ COURSE_ID +"))");

        //create assessment note table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                ASSESSMENT_NOTE_TABLE + "(" + ASSESSMENT_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ASSESSMENT_NOTE + " TEXT," +
                ASSESSMENT_NOTE_FK + " INTEGER," +
                " FOREIGN KEY ( "+ ASSESSMENT_NOTE_FK +")"+ " REFERENCES " + ASSESSMENT_TABLE+ "("+ ASSESSMENT_ID +"))");

        //create course alert table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                COURSE_ALERT_TABLE + "(" + COURSE_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COURSE_ALERT + " TEXT," +
                COURSE_ALERT_FK + " INTEGER," +
                " FOREIGN KEY ( "+ COURSE_ALERT_FK +")"+ " REFERENCES " + COURSE_TABLE+ "("+ COURSE_ID +"))");

        //create assessment alert table
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " +
                ASSESSMENT_ALERT_TABLE + "(" + ASSESSMENT_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ASSESSMENT_ALERT + " TEXT," +
                ASSESSMENT_ALERT_FK + " INTEGER," +
                " FOREIGN KEY ( "+ ASSESSMENT_ALERT_FK +")"+ " REFERENCES " + ASSESSMENT_TABLE+ "("+ ASSESSMENT_ID +"))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TERM_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ COURSE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ COURSE_ALERT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ COURSE_NOTE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ASSESSMENT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ASSESSMENT_ALERT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ASSESSMENT_NOTE_TABLE);


    }
    public Cursor getAllTerms(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TERM_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }
    public Cursor getCourses(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_TERM_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getAllCourses(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "  + COURSE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getAllAssessments(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "  + ASSESSMENT_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getAllAssessments(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query ="SELECT * FROM " + ASSESSMENT_TABLE + " WHERE " + ASSESSMENT_COURSE_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getCourseNotes(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_NOTE_TABLE + " WHERE " + COURSE_NOTE_FK + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getAssessmentNotes(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ASSESSMENT_NOTE_TABLE + " WHERE " + ASSESSMENT_NOTE_FK + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }



}
