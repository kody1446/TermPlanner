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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AssessmentDetailActivity extends AppCompatActivity implements EditShareNoteDialog.EditShareNoteDialogListener, AddCourseNoteDialog.AddCourseNoteDialogListener{
    private TextView due;
    private TextView title;
    private TextView description;
    private ListView listView;
    SimpleCursorAdapter cursorAdapter;
    DBOpenHelper helper;
    Cursor cursor;
    Assessment assess;
    private Spinner type;
    private Spinner state;
    Toolbar toolbar;
    long courseId;
    long assessmentId;
    long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        courseId = getIntent().getLongExtra("courseId", -1);
        assessmentId = getIntent().getLongExtra("assessmentId", -1);
        termId = getIntent().getLongExtra("termId", -1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assessment");// update when logic is in
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        due = findViewById(R.id.due_date);
        title = findViewById(R.id.assessment_title);
        listView = findViewById(R.id.assessmentNoteLV);
        type = findViewById(R.id.assessment_type);
        state = findViewById(R.id.assessment_state);
        description = findViewById(R.id.assessment_description);
        TextView textView = new TextView(this);
        textView.setText("Assessment notes: ");
        listView.addHeaderView(textView);

    }
    @Override
    protected void onResume() {

        super.onResume();
        try{
            assess = DBDataManager.getAssessment(this, assessmentId);
            String dueDate = "Goal:   " + assess.getDue();
            getSupportActionBar().setTitle(assess.getTitle());
            title.setText(assess.getTitle());
            due.setText(dueDate);
            description.setText(assess.getDescription());
            type.setSelection(assess.getType());
            type.setEnabled(false);
            state.setSelection(assess.getState());
            state.setEnabled(false);

        }catch(CursorIndexOutOfBoundsException e){
            Toast.makeText(this, "No assessment to display in db", Toast.LENGTH_SHORT).show();
        }
        helper = new DBOpenHelper(this);
        helper.getReadableDatabase();
        cursor = helper.getAssessmentNotes(assessmentId);
        displayListView();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String note = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTE));
                        openDialog(l,note);
                    }
                }
        );
    }
    // inflate action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assessment_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //specify events for menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_test_note_data:
                createTestData();
                onResume();
                return true;
            case R.id.add_assessment_note:
                openAddDialog();
                return true;
            case R.id.delete_assessment:
                int deleteAssessment = DBDataManager.deleteAssessment(this, assessmentId);
                if(deleteAssessment > 0) {
                    Toast.makeText(this,"Assessment Deleted", Toast.LENGTH_LONG).show();
                    Intent back = new Intent(AssessmentDetailActivity.this, AssessmentListActivity.class);
                    back.putExtra("courseId", courseId);
                    back.putExtra("termId", termId);
                    startActivity(back);
                }else{
                    Toast.makeText(this,"An error has occurred", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.edit_assessment:
                Intent edit = new Intent(AssessmentDetailActivity.this, EditAssessmentActivity.class);
                edit.putExtra("assessmentId", assessmentId);
                edit.putExtra("termId", termId);
                startActivity(edit);
                return true;
            case R.id.back_assessments:
                Intent intent = new Intent(AssessmentDetailActivity.this, AssessmentListActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
                return true;
            case R.id.goal_alarm:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String stDate = assess.getDue();
                Date date = null;
                try {
                    date = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(date);
                Intent startAlarm = new Intent(AssessmentDetailActivity.this, MyReceiver.class);
                startAlarm.putExtra("title", assess.getTitle());
                startAlarm.putExtra("text", "Goal date today:  " + assess.getDue());
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailActivity.this, 3, startAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                long start = startCal.getTimeInMillis();
                Toast.makeText(this, "Assessment Alert Created.", Toast.LENGTH_SHORT).show();
                am.set(AlarmManager.RTC_WAKEUP, start, sender);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }}
    //pull info from db and put into listview
    private void displayListView(){
        //desired columns
        String[] columns = new String[]{
                DBOpenHelper.ASSESSMENT_NOTE
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
    public void createTestData(){
        DBDataManager.insertAssessmentNote(this, assessmentId, " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        DBDataManager.insertAssessmentNote(this, assessmentId, "ut aliquip ex ea commodo consequat.");
    }
    @Override
    public void onDelete(long id){
        int delete =  DBDataManager.deleteAssessmentNote(this, id);
        if(delete > 0) {
            Toast.makeText(this, "Note has been deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }
        onResume();
    }

    @Override
    public void applyTexts(String note, long id) {
        DBDataManager.updateAssessmentNote(this, note, id);
        onResume();

    }
    @Override
    public void addNote(String note){
        DBDataManager.insertAssessmentNote(this, assessmentId, note);
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