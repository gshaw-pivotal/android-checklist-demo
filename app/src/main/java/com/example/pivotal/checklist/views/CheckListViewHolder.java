package com.example.pivotal.checklist.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.models.Checklist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.checklist_item_container)
    LinearLayout checklistContainer;

    @BindView(R.id.checklist_name)
    TextView checkListNameTextView;

    @BindView(R.id.delete_checklist)
    Button deleteCheckListButton;

    public static CheckListViewHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklists_list_item, parent, false);
        return new CheckListViewHolder(view);
    }

    private CheckListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setCheckList(final Checklist checklist, final int position, final OnChecklistClickedListener listener) {
        checklistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChecklistClicked(checklist);
                }
            }
        });
        checkListNameTextView.setText(checklist.getName());
        deleteCheckListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteChecklist(position, checklist);
                }
            }
        });
    }

    public interface OnChecklistClickedListener {
        void onChecklistClicked(Checklist checklist);
        void onDeleteChecklist(int position, Checklist checklist);
    }
}
