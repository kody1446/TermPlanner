package com.example.termplanner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TermList extends AppCompatActivity implements AddTermDialog.AddTermDialogListener {
    private SimpleCursorAdapter cursorAdapter;
    private DBOpenHelper helper;
    private Button addTerm;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        //setup all id's for items in view
        Toolbar toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.allTermsLv);
        addTerm = findViewById(R.id.addTermBtn);

        // setup action bar with custom layout
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Terms");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //take to detailed term view on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long termId) {
                Intent intent = new Intent(TermList.this, TermDetailActivity.class);
                intent.putExtra("termId", termId);
                startActivity(intent);

            }
        });

        // open add term dialog on click
        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

       //end of class
    }


    // inflate action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //specify events for menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_page:
                Intent intent = new Intent(TermList.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.add_test_data:
                createSampleData();
                onResume();
                return true;
            case R.id.current_term:
                long activeTermId;
                activeTermId = DBDataManager.getActiveTerm(this);
                Intent active = new Intent(TermList.this, TermDetailActivity.class);
                active.putExtra("termId", activeTermId);
                startActivity(active);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void openDialog(){
        AddTermDialog addTermDialog = new AddTermDialog();
        addTermDialog.show(getSupportFragmentManager(), "Add Term Dialog");

    }

    //on load and on resume we will update the data for our listview rather than using callbacks
    @Override
    protected void onResume() {

        super.onResume();
        displayListView();
    }


    // create a button or menu item to add test data
    private void createSampleData(){
        DBDataManager.insertTerm(this, "Fall 2020", "2020-07-01","2020-12-31",0);
        DBDataManager.insertTerm(this, "Spring 2021", "2021-01-01","2021-06-30",0);
        DBDataManager.insertTerm(this, "Fall 2021", "2021-07-01","2021-12-31",0);
        DBDataManager.insertTerm(this, "Spring 2022", "2022-01-01","2022-06-30",0);
    }

    //pull info from db and put into listview
   private void displayListView(){
         helper = new DBOpenHelper(TermList.this);
        //get database
        helper.getReadableDatabase();
        Cursor cursor = helper.getAllTerms();

            //populate listview
            //desired columns
            String[] columns = new String[]{
                    DBOpenHelper.TERM_NAME,
                    DBOpenHelper.TERM_START,
                    DBOpenHelper.TERM_END,
                    DBOpenHelper.TERM_ID
            };

            //id to ui elements
            int[] to = new int[]{
                    R.id.tvTitle,
                    R.id.tvEnd,
                    R.id.tvStart,
                    R.id.termId
            };


            cursorAdapter = new SimpleCursorAdapter(
                    TermList.this, R.layout.term_list_item, cursor, columns, to, 0
            );

            listView.setAdapter(cursorAdapter);

    }


    //add term from dialog by using the dialog interface
    @Override
    public void applyTexts(String name, String start, String end) {
        Term term = new Term();
        term.setName(name);
        term.setStartDate(start);
        term.setEndDate(end);
        term.setActive(0);
        DBDataManager.insertTerm(this, term.getName(), term.getStartDate(), term.getEndDate(), term.getActive());
        onResume();
    }
}