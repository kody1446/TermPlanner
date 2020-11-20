package com.example.termplanner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ProgressTracker extends AppCompatActivity {
    long termId;
    DBOpenHelper helper;
    Cursor courseCursor;
    Term term;
    private TextView cPTT;
    private TextView cC;
    private TextView cD;
    private TextView cPerc;
    private TextView aNot;
    private TextView aPass;
    private TextView aFail;
    private TextView aPerc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        termId = intent.getLongExtra("termId", -1);
        cPTT = findViewById(R.id.courses_inProgress);
        cC = findViewById(R.id.courses_completed);
        cD = findViewById(R.id.courses_dropped);
        cPerc = findViewById(R.id.courses_percent);
        aNot = findViewById(R.id.assessment_notTaken);
        aPass = findViewById(R.id.assessment_passed);
        aFail = findViewById(R.id.assessment_failed);
        aPerc = findViewById(R.id.assessment_percent);



    }

    @Override
    protected void onResume() {
        helper = new DBOpenHelper(this);
        term = DBDataManager.getTerm(this, termId);
        getSupportActionBar().setTitle(term.getName() + " Progress");
        courseCursor = helper.getCourses(termId);
        int cPlanToTake =0 ;
        int cInProgress= 0;
        int cCompleted= 0;
        int cDropped= 0;
        double total = 0;
        total = courseCursor.getCount();
        while (courseCursor.moveToNext()){
            int active = courseCursor.getInt(courseCursor.getColumnIndex(DBOpenHelper.COURSE_ACTIVE));
            if(active == 0){
                cInProgress++;
            }
            if(active == 1){
                cCompleted++;
            }
            if(active == 2){
                cDropped++;
            }
            if(active == 3){
                cPlanToTake++;
            }
        }
        double com = cCompleted;
        double perc = (com / total)* 100;
        perc = perc*100;
        perc = Math.floor(perc);
        perc = perc/100;
        cPTT.setText("Plan to take/ In progress: " + cPlanToTake + "/" + cInProgress);
        cC.setText("Completed: " + cCompleted);
        cD.setText("Dropped: " + cDropped);
        cPerc.setText("You are done with " + perc + "% of your courses");
        Cursor newCourseCursor = helper.getCourses(termId);
        int tot = newCourseCursor.getCount();
        ArrayList<Long> courses = new ArrayList<Long>();
        for (int i = 0;i <tot; i++){
            while (newCourseCursor.moveToNext()){
                courses.add(newCourseCursor.getLong(newCourseCursor.getColumnIndex(DBOpenHelper.COURSE_ID)));
            }
        }
        ArrayList<Integer> assessments = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++){
            Cursor cursor = helper.getAllAssessments(courses.get(i));
            while(cursor.moveToNext()){
                assessments.add(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_STATE)));

            }
        }
        int aNotTaken = 0;
        int aPassed = 0;
        int aFailed = 0;

        for (int i = 0; i<assessments.size(); i++){
            int state = assessments.get(i);
            if (state == 0){
                aNotTaken++;
            }
            if(state == 1){
                aPassed++;
            }
            if(state == 2){
                aFailed++;
            }

        }
        double atotal = assessments.size();
        double aPercTotal = (aPassed / atotal) *100;
        aPercTotal = aPercTotal*100;
        aPercTotal = Math.floor(aPercTotal);
        aPercTotal = aPercTotal/100;
        aNot.setText("Not taken: " + aNotTaken);
        aPass.setText("Passed: " + aPassed);
        aFail.setText("Failed: " + aFailed);
        String optionalMessage ="";
        if(aPercTotal == 0) {
            optionalMessage = " Update your assessment activity in the assessments page if you have passed any assessments.";
        }
        aPerc.setText("You have passed " + aPercTotal + "% of your assessments. " + optionalMessage);







        super.onResume();
    }
}