package com.example.termplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.time.LocalDate;


public class MainActivity extends AppCompatActivity {
    DBOpenHelper db = new DBOpenHelper(MainActivity.this);
    private long activeTermId;
    private Button allTermsBtn;
    private Button currentTermBtn;
    private Button progressTrackerBtn;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Home Page");
            // get db
            db.getWritableDatabase();




        //button event for loading termlist
        allTermsBtn = findViewById(R.id.allTermsBtn);
        currentTermBtn = findViewById(R.id.currentTermBtn);
        progressTrackerBtn = findViewById(R.id.trackerBtn);
        progressTrackerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activeTermId = DBDataManager.getActiveTerm(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, ProgressTracker.class);
                        intent.putExtra("termId", activeTermId);
                        startActivity(intent);
                    }
                }
        );
        allTermsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermList.class);
                startActivity(intent);
            }
        });
        currentTermBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activeTermId = DBDataManager.getActiveTerm(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, TermDetailActivity.class);
                        intent.putExtra("termId", activeTermId);
                        startActivity(intent);
                    }
                }
        );



    }

   @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {

        super.onResume();
        Cursor cursor = db.getAllTerms();
            while (cursor.moveToNext()) {
                LocalDate start = LocalDate.parse(cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START)));
                LocalDate end = LocalDate.parse(cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END)));
                LocalDate date = LocalDate.now();
                try {
                    if (start.isBefore(date) && end.isAfter(date)) {
                        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
                        long termId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TERM_ID));
                        String starter = start.toString();
                        String ender = end.toString();
                        DBDataManager.updateTerm(this, name, starter, ender, 1, termId);
                        Toast.makeText(this, "Active term has been set", Toast.LENGTH_LONG).show();
                    }
                } catch (CursorIndexOutOfBoundsException e) {
                    Toast.makeText(this, "no active term", Toast.LENGTH_LONG).show();

                }
            }

    }



}