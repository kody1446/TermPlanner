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


public class EditCourseActivity extends AppCompatActivity {
    public EditText name;
    public EditText start;
    public EditText end;
    public EditText description;
    public EditText mentor;
    public EditText mentorPhone;
    public EditText mentorEmail;
    public Spinner active;
    public Button saveBtn;
    public Button cancelBtn;
    public long courseId;
    public long termId;
    Toolbar toolbar;
    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        name = findViewById(R.id.editTextName);
        start = findViewById(R.id.editTextStart);
        end = findViewById(R.id.editTextEnd);
        description = findViewById(R.id.editTextDescription);
        mentor = findViewById(R.id.editTextMentor);
        mentorPhone = findViewById(R.id.editTextMentorPhone);
        mentorEmail = findViewById(R.id.editTextMentorEmail);
        active = findViewById(R.id.courseActiveSpinner);
        saveBtn = findViewById(R.id.saveBtn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cancelBtn = findViewById(R.id.cancelBtn);

        courseId = getIntent().getLongExtra("courseId", -1);
        termId = getIntent().getLongExtra("termId", -1);
        course = DBDataManager.getCourse(this, courseId);
        getSupportActionBar().setTitle("Edit ' " + course.getCourseName() + " '");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        name.setText(course.getCourseName());
        start.setText(course.getCourseStart());
        start.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = start.getText().toString().length();
                if (digits > 1)
                    lastChar = start.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int digits = start.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4 || digits == 7) {
                        start.append("-");
                    }
                }
            }
        });
        end.setText(course.getCourseEnd());
        end.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = end.getText().toString().length();
                if (digits > 1)
                    lastChar = end.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int digits = end.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4 || digits == 7) {
                        end.append("-");
                    }
                }
            }
        });
        description.setText(course.getCourseDescription());
        mentor.setText(course.getCourseMentor());
        mentorPhone.setText(course.getCourseMentorPhone());
        mentorPhone.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = mentorPhone.getText().toString().length();
                if (digits > 1)
                    lastChar = mentorPhone.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int digits = mentorPhone.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        mentorPhone.append("-");
                    }
                }
            }
        });
        mentorEmail.setText(course.getCourseMentorEmail());
        active.setSelection(course.getCourseActive());
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        course.setCourseId(courseId);
                        course.setCourseTermId(termId);
                        course.setCourseName(name.getText().toString());
                        if (validateDates(start.getText().toString()) == true && validateDates(end.getText().toString()) == true) {
                            course.setCourseStart(start.getText().toString());
                            course.setCourseEnd(end.getText().toString());
                            course.setCourseDescription(description.getText().toString());
                            course.setCourseMentor(mentor.getText().toString());
                            course.setCourseMentorPhone(mentorPhone.getText().toString());
                            if (validateEmail(mentorEmail.getText().toString()) == true) {
                                course.setCourseMentorEmail(mentorEmail.getText().toString());
                                course.setCourseActive(active.getSelectedItemPosition());
                                try {
                                    DBDataManager.updateCourse(EditCourseActivity.this, course.getCourseName(), course.getCourseStart(),
                                            course.getCourseEnd(), course.getCourseDescription(), course.getCourseMentor(),
                                            course.getCourseMentorPhone(), course.getCourseMentorEmail(), course.getCourseActive(), course.getCourseId(),
                                            course.getCourseTermId());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                startCourseActivity();
                                Toast.makeText(EditCourseActivity.this, "Course changes applied", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditCourseActivity.this, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditCourseActivity.this, "Please provide valid dates YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startCourseActivity();
                    }
                }
        );


    }

    public void startCourseActivity() {
        Intent intent = new Intent(EditCourseActivity.this, CourseDetailActivity.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("termId", termId);
        startActivity(intent);
    }


    public static boolean validateEmail(String input) {
        String reEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(reEmail, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();

    }

    public static boolean validateDates(String input) {
        String reDate = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        Pattern datePat = Pattern.compile(reDate, Pattern.CASE_INSENSITIVE);
        Matcher matcher = datePat.matcher(input);
        return matcher.find();
    }
}