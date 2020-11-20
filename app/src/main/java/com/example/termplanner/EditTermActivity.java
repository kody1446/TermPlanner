package com.example.termplanner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTermActivity extends AppCompatActivity {
    Term term;
    private long termId;
    private EditText editTextName;
    private EditText editTextStart;
    private EditText editTextEnd;
    private Button cancelBtn;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar toolbar =  findViewById(R.id.toolbar);

        // setup action bar with custom layout
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Term");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get intent and corresponding termId to populate editText fields
        Intent intent = getIntent();
        termId = intent.getLongExtra("termId", -1);

        //get term
        term = new Term();
        term = DBDataManager.getTerm(this, termId);

        //set id's for items
        editTextName = findViewById(R.id.editTextName);
        editTextStart = findViewById(R.id.editTextStart);
        editTextEnd = findViewById(R.id.editTextEnd);
        cancelBtn = findViewById(R.id.cancelBtn);
        saveBtn = findViewById(R.id.saveBtn);

        //set values for editText fields
        editTextName.setText(term.getName(),null);
        editTextStart.setText(term.getStartDate(), null);
        editTextEnd.setText(term.getEndDate(), null);
        editTextEnd.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = editTextEnd.getText().toString().length();
                if (digits > 1)
                    lastChar = editTextEnd.getText().toString().substring(digits-1);}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int digits = editTextEnd.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4 || digits == 7) {
                        editTextEnd.append("-");}}
            }
        });
        editTextStart.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = editTextStart.getText().toString().length();
                if (digits > 1)
                    lastChar = editTextStart.getText().toString().substring(digits-1);}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int digits = editTextStart.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4 || digits == 7) {
                        editTextStart.append("-");}}
            }
        });
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EditTermActivity.this, TermDetailActivity.class);
                        intent.putExtra("termId", termId);
                        startActivity(intent);

                    }
                }
        );
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        term.setName(editTextName.getText().toString());
                        if(validateDates(editTextStart.getText().toString())==true && validateDates(editTextEnd.getText().toString())==true) {
                            term.setStartDate(editTextStart.getText().toString());
                            term.setEndDate(editTextEnd.getText().toString());
                            DBDataManager.updateTerm(EditTermActivity.this, term.getName(), term.getStartDate(), term.getEndDate(), term.getActive(), termId);
                            Intent intent = new Intent(EditTermActivity.this, TermDetailActivity.class);
                            intent.putExtra("termId", termId);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EditTermActivity.this, "Please enter valid dates YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    public static boolean validateDates(String input) {
        String reDate = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        Pattern datePat = Pattern.compile(reDate, Pattern.CASE_INSENSITIVE);
        Matcher matcher = datePat.matcher(input);
        return matcher.find();
    }
}