package com.example.termplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.text.ParseException;



public class DBDataManager {

    public static long getActiveTerm(Context context){
        Cursor cursor = context.getContentResolver().query(DBProvider.TERM_URI, null,DBOpenHelper.TERM_ACTIVE +
                " = 1", null, null);
        long id = -1;
        if(cursor.getCount() == 0){
            return id;
        }else{
            try{
                cursor.moveToFirst();
                id = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TERM_ID));
                cursor.close();
                return id;
            }catch(Exception e){
                e.printStackTrace();
                return id;

            }
        }
    }

    public static void insertTerm(Context context, String name, String start, String end, int active){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, name);
        values.put(DBOpenHelper.TERM_START, start);
        values.put(DBOpenHelper.TERM_END, end);
        values.put(DBOpenHelper.TERM_ACTIVE, active);
        context.getContentResolver().insert(DBProvider.TERM_URI, values);
    }

    public static Integer deleteTerm(Context context, long id){
        return context.getContentResolver().delete(DBProvider.TERM_URI,DBOpenHelper.TERM_ID + " = " + id, null);
    }

    public static Integer deleteCourse(Context context, long id){
        return context.getContentResolver().delete(DBProvider.COURSE_URI, DBOpenHelper.COURSE_ID + " = " + id , null);
    }
    public static Integer deleteAssessment(Context context, long id){
        return context.getContentResolver().delete(DBProvider.ASSESSMENT_URI, DBOpenHelper.ASSESSMENT_ID + " = " + id, null);
    }
    public static Integer deleteCourseNote(Context context, long id){
        return context.getContentResolver().delete(DBProvider.COURSE_NOTE_URI, DBOpenHelper.COURSE_NOTE_ID + " = " + id, null);
    }
    public static Integer deleteAssessmentNote(Context context, long id){
        return context.getContentResolver().delete(DBProvider.ASSESSMENT_NOTE_URI, DBOpenHelper.ASSESSMENT_NOTE_ID + " = " + id, null);
    }

    public static Term getTerm(Context context, long termId){
        Cursor cursor = context.getContentResolver().query(DBProvider.TERM_URI, DBOpenHelper.TERM_COLUMNS, DBOpenHelper.TERM_ID +
                " = " + termId, null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
        String start = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START));
        String end = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END));
        int active = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TERM_ACTIVE));
        Term term = new Term();
        term.setName(name);
        term.setStartDate(start);
        term.setEndDate(end);
        term.setActive(active);
        return term;
    }
    public static Assessment getAssessment(Context context, long assessmentId){
        Cursor cursor = context.getContentResolver().query(DBProvider.ASSESSMENT_URI, DBOpenHelper.ASSESSMENT_COLUMNS, DBOpenHelper.ASSESSMENT_ID +
                " = " + assessmentId, null ,null );
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DESCRIPTION));
        String due = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DUE_DATE));
        int type = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE));
        int state = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_STATE));
        Assessment assessment = new Assessment();
        assessment.setTitle(title);
        assessment.setDescription(description);
        assessment.setDue(due);
        assessment.setType(type);
        assessment.setState(state);
        return assessment;
    }
    public static Course getCourse(Context context, long courseId){
        Cursor cursor = context.getContentResolver().query(DBProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS,DBOpenHelper.COURSE_ID +
                " = " + courseId, null, null);
        cursor.moveToFirst();
        long courseTermId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_TERM_ID));
        String courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        String courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_START));
        String courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_END));
        String courseDescription = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_DESCRIPTION));
        int courseActive = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_ACTIVE));
        String courseMentor = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR));
        String courseMentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_PHONE));
        String courseMentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_EMAIL));
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseStart(courseStart);
        course.setCourseEnd(courseEnd);
        course.setCourseTermId(courseTermId);
        course.setCourseActive(courseActive);
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        course.setCourseMentor(courseMentor);
        course.setCourseMentorPhone(courseMentorPhone);
        course.setCourseMentorEmail(courseMentorEmail);
        return course;

    }
    public static void updateTerm(Context context, String name, String start, String end, int active, long id){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, name);
        values.put(DBOpenHelper.TERM_START, start);
        values.put(DBOpenHelper.TERM_END, end);
        values.put(DBOpenHelper.TERM_ACTIVE, active);
        context.getContentResolver().update(DBProvider.TERM_URI, values, DBOpenHelper.TERM_ID +
                " = " + id, null);
    }
    public static void updateCourse(Context context, String name, String start, String end, String desc, String mentor, String mPhone, String mEmail, int active, long id, long termId) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_NAME, name);
        values.put(DBOpenHelper.COURSE_START, start);
        values.put(DBOpenHelper.COURSE_END, end);
        values.put(DBOpenHelper.COURSE_DESCRIPTION, desc);
        values.put(DBOpenHelper.COURSE_MENTOR, mentor);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, mPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, mEmail);
        values.put(DBOpenHelper.COURSE_ACTIVE, active);
        context.getContentResolver().update(DBProvider.COURSE_URI, values, DBOpenHelper.COURSE_ID +
                " = " + id, null);
    }
    public static void updateAssessment(Context context, String title, String desc, String due, int type, int state, long id) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_TITLE, title);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, desc);
        values.put(DBOpenHelper.ASSESSMENT_DUE_DATE, due);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, type);
        values.put(DBOpenHelper.ASSESSMENT_STATE, state);
        context.getContentResolver().update(DBProvider.ASSESSMENT_URI, values, DBOpenHelper.ASSESSMENT_ID +
                " = " + id, null);
    }
    public static void updateCourseNote(Context context, String note, long id){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE, note);
        context.getContentResolver().update(DBProvider.COURSE_NOTE_URI, values, DBOpenHelper.COURSE_NOTE_ID +
                " = " + id, null);
    }
    public static void updateAssessmentNote(Context context, String note, long id){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE, note);
        context.getContentResolver().update(DBProvider.ASSESSMENT_NOTE_URI, values, DBOpenHelper.ASSESSMENT_NOTE_ID +
                " = " + id, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void insertCourse(Context context, long courseTermId, String courseName, String start, String end, String courseDescription, int courseActive,
                                    String courseMentor, String courseMentorPhone, String courseMentorEmail) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, courseTermId);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, start);
        values.put(DBOpenHelper.COURSE_END, end);
        values.put(DBOpenHelper.COURSE_DESCRIPTION, courseDescription);
        values.put(DBOpenHelper.COURSE_ACTIVE, courseActive);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, courseMentorPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, courseMentorEmail);
        context.getContentResolver().insert(DBProvider.COURSE_URI, values);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void insertAssessment(Context context, long courseId, String title, String desc, String due, int type, int state) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_TITLE, title);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, desc);
        values.put(DBOpenHelper.ASSESSMENT_DUE_DATE, due);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, type);
        values.put(DBOpenHelper.ASSESSMENT_STATE, state);
        context.getContentResolver().insert(DBProvider.ASSESSMENT_URI, values);
    }



    public static void insertCourseNote(Context context, long courseId, String courseNote){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_FK, courseId);
        values.put(DBOpenHelper.COURSE_NOTE, courseNote);
        context.getContentResolver().insert(DBProvider.COURSE_NOTE_URI, values);
    }
    public static void insertAssessmentNote(Context context, long assessmentId, String assessmentNote){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE_FK, assessmentId);
        values.put(DBOpenHelper.ASSESSMENT_NOTE, assessmentNote);
        context.getContentResolver().insert(DBProvider.ASSESSMENT_NOTE_URI, values);
    }













}
