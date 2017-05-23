package com.example.pivotal.checklist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.views.CheckListViewHolder;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static io.reactivex.Observable.fromArray;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    private List<Checklist> checklistList = new ArrayList<>();
    private CheckListViewHolder.OnChecklistClickedListener onChecklistClickedListener;

    public CheckListAdapter(CheckListViewHolder.OnChecklistClickedListener listener) {
        onChecklistClickedListener = listener;
    }

    @Override
    public CheckListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CheckListViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(CheckListViewHolder holder, int position) {
        holder.setCheckList(checklistList.get(position), position, new CheckListViewHolder.OnChecklistClickedListener() {
            @Override
            public void onChecklistClicked(Checklist checklist) {
                if (onChecklistClickedListener != null) {
                    onChecklistClickedListener.onChecklistClicked(checklist);
                }
            }

            @Override
            public void onDeleteChecklist(int position, Checklist checklist) {
                if (onChecklistClickedListener != null) {
                    onChecklistClickedListener.onDeleteChecklist(position, checklist);
                }
                checklistList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklistList.size();
    }

    public void setChecklistList(List<Checklist> checklists) {
        checklistList.clear();

        if (checklists != null) {
            checklistList.addAll(checklists);
        }

        notifyDataSetChanged();
    }

    public void addCheckList(Checklist checklist) {
        if (checklist != null) {
            checklistList.add(checklist);
            notifyDataSetChanged();
        }
    }

    public void updateCheckList(final Checklist checklist) {
        if (checklist != null) {
            checklistList.clear();
            checklistList.addAll(FluentIterable.from(checklistList)
                    .transform(new com.google.common.base.Function<Checklist, Checklist>() {
                        @Nullable
                        @Override
                        public Checklist apply(Checklist input) {
                            if (input.getId() == checklist.getId()) {
                                return checklist;
                            }
                            return input;
                        }
                    })
                    .toList());

            notifyDataSetChanged();
        }
    }
}
