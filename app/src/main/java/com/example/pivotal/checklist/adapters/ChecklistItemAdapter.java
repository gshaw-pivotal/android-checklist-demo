package com.example.pivotal.checklist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.pivotal.checklist.models.ChecklistItem;
import com.example.pivotal.checklist.views.ChecklistItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ChecklistItemAdapter extends RecyclerView.Adapter<ChecklistItemViewHolder> {

    private List<ChecklistItem> checklistItemList = new ArrayList<>();

    @Override
    public ChecklistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ChecklistItemViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ChecklistItemViewHolder holder, int position) {
        holder.setChecklistItem(checklistItemList.get(position), position, new ChecklistItemViewHolder.OnCheckListItemChangedListener() {
            @Override
            public void onNoteChanged(int position, String note) {
                checklistItemList.get(position).setNote(note);
            }

            @Override
            public void onCheckChanged(int position, boolean checked) {
                checklistItemList.get(position).setChecked(checked);
            }

            @Override
            public void onNoteDelete(int position) {
                checklistItemList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklistItemList.size();
    }

    public void setChecklistItems(List<ChecklistItem> items) {
        checklistItemList.clear();

        if (items != null) {
            checklistItemList.addAll(items);
        }

        notifyDataSetChanged();
    }

    public void addChecklistItem(ChecklistItem item) {
        if (item != null) {
            checklistItemList.add(item);
            notifyDataSetChanged();
        }
    }

    public List<ChecklistItem> getList() {
        return checklistItemList;
    }
}
