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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddCourseNoteDialog extends AppCompatDialogFragment {
    public EditText editTextNote;
    public AddCourseNoteDialog.AddCourseNoteDialogListener listener;
    public Button shareNoteBtn;
    public Button deleteNoteBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.course_note_dialog, null);

        builder.setView(view)
                .setTitle("Add Note")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String note = editTextNote.getText().toString();

                        listener.addNote(note);

                    }
                });
        editTextNote = view.findViewById(R.id.courseNoteTxt);
        shareNoteBtn = view.findViewById(R.id.shareNoteBtn);
        deleteNoteBtn = view.findViewById(R.id.delete_note);
        deleteNoteBtn.setVisibility(view.INVISIBLE);
        shareNoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        String note = editTextNote.getText().toString();
                        sendIntent.putExtra(Intent.EXTRA_TEXT, note);
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);

                    }
                }
        );
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddCourseNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddTermDialogListener");
        }
    }
    public interface AddCourseNoteDialogListener{
        void addNote(String note);
    }
}
