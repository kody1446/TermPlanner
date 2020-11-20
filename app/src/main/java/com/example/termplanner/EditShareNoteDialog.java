package com.example.termplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditShareNoteDialog extends AppCompatDialogFragment {
    public EditText editTextNote;
    public EditShareNoteDialog.EditShareNoteDialogListener listener;
    public Button shareNoteBtn;
    public Button deleteNoteBtn;
    private long courseNoteId;
    private String courseNote;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.course_note_dialog, null);

        builder.setView(view)
                .setTitle("Edit Note")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Apply changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String note = editTextNote.getText().toString();

                        listener.applyTexts(note, courseNoteId);

                    }
                });
        editTextNote = view.findViewById(R.id.courseNoteTxt);
        editTextNote.setText(courseNote);
        shareNoteBtn = view.findViewById(R.id.shareNoteBtn);
        deleteNoteBtn = view.findViewById(R.id.delete_note);
        shareNoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, courseNote);
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);

                    }
                }
        );
        deleteNoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onDelete(courseNoteId);
                        getDialog().dismiss();

                    }
                }
        );
        return builder.create();
    }

    public void setDialogValues(long id, String n){
        courseNoteId = id;
        courseNote = n;

    };
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditShareNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddTermDialogListener");
        }
    }
    public interface EditShareNoteDialogListener{
        void applyTexts(String note, long id);
        void onDelete(long id);
    }
}
