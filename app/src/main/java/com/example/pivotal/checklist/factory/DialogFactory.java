package com.example.pivotal.checklist.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.pivotal.checklist.R;

public class DialogFactory {

    public static AlertDialog showDialog(Context context, final OnNoteSavedListener onNoteSavedListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_note_dialog, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.add_note);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_note_dialog_title);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel_add_note, null);
        builder.setPositiveButton(R.string.save_add_note, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onNoteSavedListener != null) {
                    onNoteSavedListener.onNoteSaved(editText.getText().toString());
                }
            }
        });
        return builder.show();
    }

    public interface OnNoteSavedListener {
        void onNoteSaved(String note);
    }
}
