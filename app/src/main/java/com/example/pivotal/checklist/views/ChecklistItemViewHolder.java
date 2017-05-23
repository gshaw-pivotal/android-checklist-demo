package com.example.pivotal.checklist.views;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.models.ChecklistItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ChecklistItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.checklist_item_text)
    EditText noteText;

    @BindView(R.id.checklist_item_checkbox)
    CheckBox isCheckedBox;

    @BindView(R.id.delete_note)
    Button deleteNoteButton;

    private TextWatcher textWatcher;

    public static ChecklistItemViewHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_note_list_item, parent, false);
        return new ChecklistItemViewHolder(view);
    }

    private ChecklistItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setChecklistItem(ChecklistItem item, final int position, final OnCheckListItemChangedListener onCheckListItemChangedListener) {
        noteText.removeTextChangedListener(textWatcher);
        noteText.setText(item.getNote());
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onCheckListItemChangedListener != null) {
                    onCheckListItemChangedListener.onNoteChanged(position, s.toString());
                }
            }
        };
        noteText.addTextChangedListener(textWatcher);
        isCheckedBox.setOnCheckedChangeListener(null);
        isCheckedBox.setChecked(item.isChecked());
        isCheckedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckListItemChangedListener != null) {
                    onCheckListItemChangedListener.onCheckChanged(position, isChecked);
                }
            }
        });
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckListItemChangedListener != null) {
                    onCheckListItemChangedListener.onNoteDelete(position);
                }
            }
        });
    }

    public interface OnCheckListItemChangedListener {
        void onNoteChanged(int position, String note);
        void onCheckChanged(int position, boolean checked);
        void onNoteDelete(int position);
    }
}
