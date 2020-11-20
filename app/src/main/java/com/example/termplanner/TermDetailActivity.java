package com.example.termplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TermDetailActivity extends AppCompatActivity  {
    long termId;
    private Button courseBtn;
    private TextView name;
    private TextView start;
    private TextView end;
    Term term;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        //get intent from term list activity
        Intent intent = getIntent();

        //get term id
        termId = intent.getLongExtra("termId", -1);

        //set field id's for view
         name = findViewById(R.id.termNameTxt);
         start = findViewById(R.id.startTxt);
         end = findViewById(R.id.endTxt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //set button id's for view
        courseBtn = findViewById(R.id.courseBtn);

        courseBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TermDetailActivity.this, CourseListActivity.class);
                        intent.putExtra("termId", termId);
                        startActivity(intent);
                    }
                }
        );

    }
    @Override
    protected void onResume() {
        term = new Term();
        super.onResume();
        //get term info from database
        try {
            term = DBDataManager.getTerm(this, termId);
            getSupportActionBar().setTitle(term.getName());
            //set field values to selected term
            name.setText(term.getName());
            start.setText(term.getStartDate());
            end.setText(term.getEndDate());
            getSupportActionBar().setTitle(term.getName());
        }catch(CursorIndexOutOfBoundsException e){
            Toast.makeText(this, "No active term", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            // return to all term list
            case R.id.all_terms:
                Intent intent = new Intent(TermDetailActivity.this, TermList.class);
                startActivity(intent);
                return true;

            // delete selected term
            case R.id.delete_term:
                DBOpenHelper db = new DBOpenHelper(this);
                cursor = db.getCourses(termId);

                if(cursor.getCount() == 0) {
                    int delete = DBDataManager.deleteTerm(TermDetailActivity.this, termId);
                    if (delete > 0) {
                        Toast.makeText(TermDetailActivity.this, "Term Deleted", Toast.LENGTH_LONG).show();
                        Intent back = new Intent(TermDetailActivity.this, TermList.class);
                        startActivity(back);


                    } else {
                        Toast.makeText(TermDetailActivity.this, "an Error has occured", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this, "Cannot delete a term with courses assigned to it.", Toast.LENGTH_SHORT).show();
                }
                return true;
            // edit selected term
            case R.id.edit_term:
                Intent editTerm = new Intent(TermDetailActivity.this, EditTermActivity.class);
                editTerm.putExtra("termId", termId);
                startActivity(editTerm);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}