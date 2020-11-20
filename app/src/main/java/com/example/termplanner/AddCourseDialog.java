package com.example.termplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCourseDialog extends AppCompatDialogFragment {
    public EditText editTextName;
    public EditText editTextStart;
    public EditText editTextEnd;
    public EditText editTextMentor;
    public EditText editTextMentorPhone;
    public EditText editTextMentorEmail;
    private AddCourseDialog.AddCourseDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.add_course_dialog, null);

        builder.setView(view)
                .setTitle("Add Course")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        String mentor = editTextMentor.getText().toString();
                        if(validateDates(editTextEnd.getText().toString())==true && validateDates(editTextStart.getText().toString())==true) {
                            String start = editTextStart.getText().toString();
                            String end = editTextEnd.getText().toString();
                            String Description = "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                            String mentorEmail = editTextMentorEmail.getText().toString();
                            String mentorPhone = editTextMentorPhone.getText().toString();

                            if (validateEmail(mentorEmail) == true) {
                                try {
                                    listener.applyTexts(name, start, end, Description, mentor, mentorEmail, mentorPhone);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(getContext(), "Please provide a valid email address", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "Please provide valid dates YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        editTextMentor = view.findViewById(R.id.courseMentorTxt);
        editTextName = view.findViewById(R.id.courseNameTxt);
        editTextStart = view.findViewById(R.id.startTxt);
        editTextEnd = view.findViewById(R.id.endTxt);
        editTextMentorEmail = view.findViewById(R.id.courseMentorEmailTxt);
        editTextMentorPhone = view.findViewById(R.id.courseMentorPhoneTxt);
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
        editTextMentorPhone.addTextChangedListener(new TextWatcher() {
            String lastChar = " ";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = editTextMentorPhone.getText().toString().length();
                if (digits > 1)
                    lastChar = editTextMentorPhone.getText().toString().substring(digits-1);}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int digits = editTextMentorPhone.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        editTextMentorPhone.append("-");}} }
        });
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddCourseDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddTermDialogListener");
        }
    }

    public interface AddCourseDialogListener{
        void applyTexts(String name, String start, String end,String Description, String mentor, String email, String phone) throws ParseException;
    }
    public static boolean validateEmail(String input){
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

