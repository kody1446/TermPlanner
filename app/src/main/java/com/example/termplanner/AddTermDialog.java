package com.example.termplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTermDialog extends AppCompatDialogFragment {
    public EditText editTextName;
    public EditText editTextStart;
    public EditText editTextEnd;
    private AddTermDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.add_term_dialog, null);

        builder.setView(view)
                .setTitle("Add Term")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        if(validateDates(editTextStart.getText().toString())== true && validateDates(editTextEnd.getText().toString())== true) {
                            String start = editTextStart.getText().toString();
                            String end = editTextEnd.getText().toString();
                            listener.applyTexts(name, start, end);
                        }else{
                            Toast.makeText(getContext(), "Please enter valid dates YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        editTextEnd = view.findViewById(R.id.edittext_end);
        editTextName = view.findViewById(R.id.edittext_name);
        editTextStart = view.findViewById(R.id.edittext_start);
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
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddTermDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddTermDialogListener");
        }
    }

    public interface AddTermDialogListener{
        void applyTexts(String name, String start, String end);
    }
    public static boolean validateDates(String input) {
        String reDate = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        Pattern datePat = Pattern.compile(reDate, Pattern.CASE_INSENSITIVE);
        Matcher matcher = datePat.matcher(input);
        return matcher.find();
    }
}
