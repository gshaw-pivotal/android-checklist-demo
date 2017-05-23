package com.example.pivotal.checklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.adapters.CheckListAdapter;
import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.persistence.ChecklistRepository;
import com.example.pivotal.checklist.views.CheckListViewHolder;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements CheckListViewHolder.OnChecklistClickedListener {

    @BindView(R.id.recycler_view_checklist_list)
    RecyclerView checkListRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CheckListAdapter checkListAdapter;
    private ChecklistRepository checklistRepository;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        checkListAdapter = new CheckListAdapter(this);
        checkListRecyclerView.setAdapter(checkListAdapter);
        checkListRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        checklistRepository = new ChecklistRepository(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.CREATE_CHECKLIST && resultCode == Activity.RESULT_OK) {
            Checklist checklist = (Checklist) data.getSerializableExtra(ChecklistExtras.CHECKLIST_EXTRA);
            checkListAdapter.addCheckList(checklist);
            checklistRepository.saveChecklist(checklist).subscribe();
        } else if (requestCode == RequestCodes.EDIT_CHECKLIST && resultCode == Activity.RESULT_OK) {
            Checklist checklist = (Checklist) data.getSerializableExtra(ChecklistExtras.CHECKLIST_EXTRA);
            checkListAdapter.updateCheckList(checklist);
            checklistRepository.updateChecklist(checklist).subscribe();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposable = checklistRepository.getChecklists().subscribe(new Consumer<List<Checklist>>() {
            @Override
            public void accept(@NonNull List<Checklist> checklists) throws Exception {
                checkListAdapter.setChecklistList(checklists);
            }
        });
    }

    @Override
    protected void onPause() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onPause();
    }

    @Override
    public void onChecklistClicked(Checklist checklist) {
        Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
        intent.putExtra(ChecklistExtras.CHECKLIST_EXTRA, checklist);
        startActivityForResult(intent, RequestCodes.EDIT_CHECKLIST);
    }

    @Override
    public void onDeleteChecklist(int position, Checklist checkList) {
        checklistRepository.deleteChecklist(checkList.getId()).subscribe();
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
        intent.putExtra(ChecklistExtras.CHECKLIST_NUMBER_TITLE_EXTRA, checkListAdapter.getItemCount() + 1);
        startActivityForResult(intent, RequestCodes.CREATE_CHECKLIST);
    }

    public static class RequestCodes {
        public static final int CREATE_CHECKLIST = 0;
        public static final int EDIT_CHECKLIST = 1;
    }

    public static class ChecklistExtras {
        public static final String CHECKLIST_EXTRA = "checklist_extra";
        public static final String CHECKLIST_NUMBER_TITLE_EXTRA = "checklist_number_title_extra";
    }
}
