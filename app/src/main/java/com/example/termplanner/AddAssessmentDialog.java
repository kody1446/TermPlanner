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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAssessmentDialog extends AppCompatDialogFragment {
    private EditText title;
    private EditText description;
    private EditText due;
    private Spinner type;
    private Spinner state;
    private AddAssessmentDialog.AddAssessmentDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.add_assessment_dialog, null);

        builder.setView(view)
                .setTitle("Add Assessment")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String t = title.getText().toString();
                        String d = description.getText().toString();
                        if(validateDates(due.getText().toString())==true) {
                            String du = due.getText().toString();
                            int ty = type.getSelectedItemPosition();
                            int st = state.getSelectedItemPosition();
                            try {
                                listener.applyTexts(t, d, du, ty, st);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getContext(), "Please provide valid date YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        title = view.findViewById(R.id.assessment_title);
        description = view.findViewById(R.id.assessment_description);
        due = view.findViewById(R.id.assessment_due);
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
        type = view.findViewById(R.id.assessment_type);
        state = view.findViewById(R.id.assessment_state);
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddAssessmentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddTermDialogListener");
        }
    }

    public interface AddAssessmentDialogListener{
        void applyTexts(String t, String d, String du, int type, int state) throws ParseException;
    }
    public static boolean validateDates(String input) {
        String reDate = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        Pattern datePat = Pattern.compile(reDate, Pattern.CASE_INSENSITIVE);
        Matcher matcher = datePat.matcher(input);
        return matcher.find();
    }

}

