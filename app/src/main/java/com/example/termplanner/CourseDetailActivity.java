package com.example.termplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CourseDetailActivity extends AppCompatActivity implements EditShareNoteDialog.EditShareNoteDialogListener, AddCourseNoteDialog.AddCourseNoteDialogListener{
    private TextView courseName;
    private TextView courseDescription;
    private TextView mentorName;
    private TextView mentorPhone;
    private TextView mentorEmail;
    private TextView startEnd;
    private Spinner active;
    private Button assessmentBtn;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    Cursor cursor;
    DBOpenHelper helper;
    private Toolbar toolbar;
    long courseId;
    long termId;
    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Intent intent = getIntent();
        courseId = intent.getLongExtra("courseId", -1);
        termId = intent.getLongExtra("termId", -1);

        courseName = findViewById(R.id.courseNameTxt);
        courseDescription = findViewById(R.id.courseDescriptionTxt);
        mentorName = findViewById(R.id.mentorTxt);
        mentorPhone = findViewById(R.id.mentorPhoneTxt);
        mentorEmail = findViewById(R.id.mentorEmailTxt);
        startEnd = findViewById(R.id.startEndTxt);
        active = findViewById(R.id.courseActiveSpinner);
        listView = findViewById(R.id.courseNoteLV);
        toolbar = findViewById(R.id.toolbar);
        assessmentBtn = findViewById(R.id.assesmentBtn);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = new TextView(this);
        textView.setText("Course notes: ");
        listView.addHeaderView(textView);
        assessmentBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CourseDetailActivity.this, AssessmentListActivity.class);
                        intent.putExtra("courseId", courseId);
                        intent.putExtra("termId", termId);
                        startActivity(intent);
                    }
                }
        );

    }

    @Override
    protected void onResume() {

        super.onResume();
        try{
            course = DBDataManager.getCourse(this, courseId);
            String dates =  "Starting on: " + course.getCourseStart()+
                    "\n Expected Completion: "  + course.getCourseEnd();
            getSupportActionBar().setTitle(course.getCourseName());
            courseName.setText(course.getCourseName());
            courseDescription.setText(course.getCourseDescription());
            mentorName.setText("Mentor:         " + course.getCourseMentor());
            mentorPhone.setText("Phone:         " +course.getCourseMentorPhone());
            mentorEmail.setText("Email:         " + course.getCourseMentorEmail());
            startEnd.setText(dates);
            active.setSelection(course.getCourseActive());
            active.setEnabled(false);
        }catch(CursorIndexOutOfBoundsException e){
            Toast.makeText(this, "No course to display in db", Toast.LENGTH_SHORT).show();
        }
        helper = new DBOpenHelper(this);
        helper.getReadableDatabase();
        cursor = helper.getCourseNotes(courseId);
        displayListView();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String note = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE));
                        openDialog(l,note);
                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_note_data:
                createTestData();
                onResume();
                return true;
            case R.id.edit_course:
                Intent intent = new Intent(this, EditCourseActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
                return true;
            case R.id.delete_course:
                int deleteCourse = DBDataManager.deleteCourse(this, courseId);
                if(deleteCourse > 0) {
                    Toast.makeText(this,"Course Deleted", Toast.LENGTH_LONG).show();
                    Intent back = new Intent(CourseDetailActivity.this, CourseListActivity.class);
                    back.putExtra("termId", termId);
                    startActivity(back);
                }else{
                    Toast.makeText(this,"An error has occurred", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.add_note:
                openAddDialog();
                return true;
            case R.id.course_list:
                Intent cl = new Intent(CourseDetailActivity.this, CourseListActivity.class);
                cl.putExtra("termId", termId);
                startActivity(cl);
                return true;
            case R.id.start_alarm:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String stDate = course.getCourseStart();
                Date date = null;
                try {
                    date = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(date);
                Intent startAlarm = new Intent(CourseDetailActivity.this, MyReceiver.class);
                startAlarm.putExtra("title", course.getCourseName());
                startAlarm.putExtra("text", "Starting on  " + course.getCourseStart());
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, 1, startAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                long start = startCal.getTimeInMillis();
                Toast.makeText(this, "Course Alert Created.", Toast.LENGTH_SHORT).show();
                am.set(AlarmManager.RTC_WAKEUP, start, sender);
                return true;
            case R.id.end_alarm:
                SimpleDateFormat sdfe = new SimpleDateFormat("yyyy-MM-dd");
                String enDate = course.getCourseEnd();
                Date ender= null;
                try {
                    ender = sdfe.parse(enDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar endCal = Calendar.getInstance();
                endCal.setTime(ender);
                Intent endAlarm = new Intent(CourseDetailActivity.this, MyReceiver.class);
                endAlarm.putExtra("title", course.getCourseName());
                endAlarm.putExtra("text", "Ending on  " + course.getCourseEnd());
                PendingIntent send = PendingIntent.getBroadcast(CourseDetailActivity.this, 2, endAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager ame = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                long end = endCal.getTimeInMillis();
                Toast.makeText(this,"Course Alert Created." , Toast.LENGTH_SHORT).show();
                ame.set(AlarmManager.RTC_WAKEUP, end, send);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void createTestData(){
        DBDataManager.insertCourseNote(this, courseId, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        DBDataManager.insertCourseNote(this, courseId, "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
    }

    //pull info from db and put into listview
    private void displayListView(){
        //desired columns
        String[] columns = new String[]{
                DBOpenHelper.COURSE_NOTE
        };
        //id to ui elements
        int[] to = new int[]{
                R.id.courseNoteTxt

        };
        cursorAdapter = new SimpleCursorAdapter(
                this, R.layout.course_note_list_item, cursor, columns, to, 0
        );
        listView.setAdapter(cursorAdapter);
    }
    @Override
    public void onDelete(long id){
        int delete =  DBDataManager.deleteCourseNote(this, id);
        if(delete > 0) {
            Toast.makeText(this, "Note has been deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }
        onResume();
    }

    @Override
    public void applyTexts(String note, long id) {
        CourseNote cNote = new CourseNote();
        cNote.setNote(note);
        DBDataManager.updateCourseNote(this, cNote.getNote(), id);
        onResume();

    }
    @Override
    public void addNote(String note){
        CourseNote courseNote = new CourseNote();
        courseNote.setNote(note);
        DBDataManager.insertCourseNote(this, courseId, courseNote.getNote());
        onResume();
    }
    public void openAddDialog(){
        AddCourseNoteDialog dialog = new AddCourseNoteDialog();
        dialog.show(getSupportFragmentManager(), "add note dialog");
    }
    public void openDialog(long id, String note){
        EditShareNoteDialog dialog = new EditShareNoteDialog();
        dialog.setDialogValues(id, note);
        dialog.show(getSupportFragmentManager(), "Note Dialog");
    }
}