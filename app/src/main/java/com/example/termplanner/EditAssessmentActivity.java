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
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditAssessmentActivity extends AppCompatActivity {
    private EditText title;
    private EditText description;
    private EditText due;
    private Spinner type;
    private Spinner state;
    private Button save;
    private Button cancel;
    Toolbar toolbar;
    long assessmentId;
    long termId;
    Assessment assess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        assessmentId = getIntent().getLongExtra("assessmentId", -1);
        termId = getIntent().getLongExtra("termId", -1);
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);
        due = findViewById(R.id.editTextDue);
        type = findViewById(R.id.assessment_type);
        state = findViewById(R.id.assessment_state);
        toolbar = findViewById(R.id.toolbar);
        save = findViewById(R.id.saveBtn);
        cancel = findViewById(R.id.cancelBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Assessment");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        assess = DBDataManager.getAssessment(this, assessmentId);
        title.setText(assess.getTitle());
        description.setText(assess.getDescription());
        due.setText(assess.getDue());
        due.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = due.getText().toString().length();
                if (digits > 1)
                    lastChar = due.getText().toString().substring(digits-1);}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int digits = due.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4 || digits == 7) {
                        due.append("-");}}
            }
        });
        type.setSelection(assess.getType());
        state.setSelection(assess.getState());
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        assess.setTitle(title.getText().toString());
                        assess.setDescription(description.getText().toString());
                        if (validateDates(due.getText().toString()) == true) {
                            assess.setDue(due.getText().toString());
                            assess.setType(type.getSelectedItemPosition());
                            assess.setState(state.getSelectedItemPosition());
                            try {
                                DBDataManager.updateAssessment(EditAssessmentActivity.this, assess.getTitle(), assess.getDescription(), assess.getDue(), assess.getType(), assess.getState(), assessmentId);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(EditAssessmentActivity.this, "Assessment Updated", Toast.LENGTH_SHORT).show();
                            goBack();
                        }
                        else{
                            Toast.makeText(EditAssessmentActivity.this, "Please provide valid date YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goBack();
                    }
                }
        );

    }
    public void goBack(){
        Intent intent = new Intent(EditAssessmentActivity.this, AssessmentDetailActivity.class);
        intent.putExtra("assessmentId", assessmentId);
        intent.putExtra("termId", termId);
        startActivity(intent);
    }
    public static boolean validateDates(String input) {
        String reDate = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        Pattern datePat = Pattern.compile(reDate, Pattern.CASE_INSENSITIVE);
        Matcher matcher = datePat.matcher(input);
        return matcher.find();
    }
}