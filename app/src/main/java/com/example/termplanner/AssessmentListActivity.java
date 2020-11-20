package com.example.termplanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;


public class AssessmentListActivity extends AppCompatActivity implements AddAssessmentDialog.AddAssessmentDialogListener{
    private SimpleCursorAdapter cursorAdapter;
    private DBOpenHelper helper;
    private Button addAssessment;
    Cursor cursor;
    Toolbar toolbar;
    ListView listView;
    long courseId;
    long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        courseId = getIntent().getLongExtra("courseId", -1);
        termId = getIntent().getLongExtra("termId", -1);
        listView = findViewById(R.id.assessment_lv);
        toolbar = findViewById(R.id.toolbar);
        addAssessment = findViewById(R.id.add_assessment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assessment List");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(AssessmentListActivity.this, AssessmentDetailActivity.class);
                        intent.putExtra("courseId", courseId);
                        intent.putExtra("assessmentId", l);
                        intent.putExtra("termId", termId);
                        startActivity(intent);
                    }
                }
        );
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createSampleData() throws ParseException {
        DBDataManager.insertAssessment(this, courseId, "C5738","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "2020-07-01", 0, 0);
        DBDataManager.insertAssessment(this, courseId, "K3890","Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "2020-07-01", 1, 1);
    }
    // inflate action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assessment_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void openDialog(){
        AddAssessmentDialog addAssessmentDialog = new AddAssessmentDialog();
        addAssessmentDialog.show(getSupportFragmentManager(), "Add Assessment Dialog");

    }

    //specify events for menu items
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.add_test_assessment_data:
                try {
                    createSampleData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                onResume();
                return true;
            case R.id.course_back:
                Intent intent = new Intent(AssessmentListActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
    }
    }

    @Override
    protected void onResume() {

        super.onResume();
        helper = new DBOpenHelper(this);
        //get database
        helper.getReadableDatabase();
        cursor = helper.getAllAssessments(courseId);

        //populate listview
        displayListView();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void applyTexts(String t, String d, String du, int type, int state) throws ParseException {
        Assessment assess = new Assessment();
        assess.setTitle(t);
        assess.setDescription(d);
        assess.setDue(du);
        assess.setType(type);
        assess.setState(state);
        DBDataManager.insertAssessment(this, courseId, assess.getTitle(), assess.getDescription(), assess.getDue(),assess.getType(), assess.getState());
        onResume();
    }

    public void displayListView(){
        //desired columns
        String[] columns = new String[]{
                DBOpenHelper.ASSESSMENT_TITLE,
                DBOpenHelper.ASSESSMENT_DUE_DATE
        };

        //id to ui elements
        int[] to = new int[]{
               R.id.assessment_title,
                R.id.assessment_due
        };
        cursorAdapter = new SimpleCursorAdapter(
                this, R.layout.assessment_list_item, cursor, columns, to, 0
        );

        listView.setAdapter(cursorAdapter);
    }
}