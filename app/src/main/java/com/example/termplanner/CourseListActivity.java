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

public class CourseListActivity extends AppCompatActivity implements AddCourseDialog.AddCourseDialogListener{
    private SimpleCursorAdapter cursorAdapter;
    private long termId;
    Term term;
    DBOpenHelper helper;
    ListView listView;
    Cursor cursor;
    Button addCourseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Intent intent = getIntent();
        termId = intent.getLongExtra("termId", -1);
        listView = findViewById(R.id.allCoursesLV);
        //get term
        term = new Term();
        term = DBDataManager.getTerm(this, termId);
        Toolbar toolbar = findViewById(R.id.toolbar);


        // setup action bar with custom layout
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(term.getName() + " Courses");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addCourseBtn = findViewById(R.id.addCourseBtn);
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long courseId) {
                Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);

            }
        });

    }
    // inflate action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_course_test_data:
                try {
                    createSampleData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                onResume();
                return true;
            case R.id.term_list:
                Intent intent = new Intent(CourseListActivity.this, TermList.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    public void openDialog(){
        AddCourseDialog addCourseDialog = new AddCourseDialog();
        addCourseDialog.show(getSupportFragmentManager(), "Add Course Dialog");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createSampleData() throws ParseException {
        Term term;
        term = DBDataManager.getTerm(CourseListActivity.this, termId);
        String start = term.getStartDate();
        String end = term.getEndDate();
        DBDataManager.insertCourse(CourseListActivity.this,
                termId,
                "Software I",
                start,
                end,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                1,
                "Bob Ross",
                "775-267-2461",
                "bobRoss@gmail.com"
        );
        DBDataManager.insertCourse(CourseListActivity.this,
                termId,
                "Software II",
                start,
                end,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                0,
                "Bob Floss",
                "775-267-2461",
                "bobFloss@gmail.com"
        );
        DBDataManager.insertCourse(CourseListActivity.this,
                termId,
                "Mobile Development",
                start,
                end,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                3,
                "Bob Toss",
                "775-267-2461",
                "bobToss@gmail.com"
        );

    }
    @Override
    protected void onResume() {

        super.onResume();
        helper = new DBOpenHelper(this);
        //get database
        helper.getReadableDatabase();
        cursor = helper.getCourses(termId);

        //populate listview
        displayListView();
    }

    //pull info from db and put into listview
    private void displayListView(){

        //desired columns
        String[] columns = new String[]{
                DBOpenHelper.COURSE_NAME,
                DBOpenHelper.COURSE_MENTOR,
                DBOpenHelper.COURSE_MENTOR_EMAIL

        };

        //id to ui elements
        int[] to = new int[]{
                R.id.courseNameTxt,
                R.id.courseMentorTxt,
                R.id.courseMentorEmail
        };
        cursorAdapter = new SimpleCursorAdapter(
                this, R.layout.course_list_item, cursor, columns, to, 0
        );

        listView.setAdapter(cursorAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void applyTexts(String name, String start, String end, String Description, String mentor, String email, String phone) throws ParseException {
        Course course = new Course();
        course.setCourseName(name);
        course.setCourseStart(start);
        course.setCourseEnd(end);
        course.setCourseDescription(Description);
        course.setCourseMentor(mentor);
        course.setCourseMentorEmail(email);
        course.setCourseMentorPhone(phone);
        course.setCourseActive(0);
        course.setCourseTermId(termId);
        DBDataManager.insertCourse(CourseListActivity.this,
                course.getCourseTermId(),
                course.getCourseName(),
                course.getCourseStart(),
                course.getCourseEnd(),
                course.getCourseDescription(),
                course.getCourseActive(),
                course.getCourseMentor(),
                course.getCourseMentorPhone(),
                course.getCourseMentorEmail()
                );
        onResume();

    }
}