package com.example.pivotal.checklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.adapters.ChecklistItemAdapter;
import com.example.pivotal.checklist.factory.DialogFactory;
import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.models.ChecklistItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChecklistActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_checklist)
    RecyclerView checklistRecyclerView;

    @BindView(R.id.add_checklist_name)
    EditText checklistTitleEditText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.set_checklist_title)
    Button setChecklistTitleButton;

    private ChecklistItemAdapter checklistItemAdapter;
    private int checkListId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        checklistItemAdapter = new ChecklistItemAdapter();
        checklistRecyclerView.setAdapter(checklistItemAdapter);
        checklistRecyclerView.setLayoutManager(new LinearLayoutManager(ChecklistActivity.this));

        int titleNumber = getIntent().getIntExtra(MainActivity.ChecklistExtras.CHECKLIST_NUMBER_TITLE_EXTRA, 0);
        Checklist checklist = (Checklist) getIntent().getSerializableExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA);

        if (checklist != null) {
            setTitle(checklist.getName());
            checklistItemAdapter.setChecklistItems(checklist.getChecklistItemList());
            checkListId = checklist.getId();
        } else {
            checkListId = titleNumber;
            setTitle(getString(R.string.title_activity_checklist, titleNumber));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.checklist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            Checklist list = new Checklist();
            list.setId(checkListId);
            list.setName(getTitle().toString());
            list.setChecklistItemList(checklistItemAdapter.getList());

            Intent intent = new Intent();
            intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA, list);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        DialogFactory.showDialog(ChecklistActivity.this, new DialogFactory.OnNoteSavedListener() {
            @Override
            public void onNoteSaved(String note) {
                ChecklistItem checklistItem = new ChecklistItem();
                checklistItem.setNote(note);
                checklistItem.setChecklistId(checkListId);
                checklistItemAdapter.addChecklistItem(checklistItem);
            }
        });
    }

    @OnClick(R.id.set_checklist_title)
    void onSetTitleButtonClicked() {
        String title = checklistTitleEditText.getText().toString();
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @OnTextChanged(value = R.id.add_checklist_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onChecklistNameChanged(Editable editable) {
        setChecklistTitleButton.setEnabled(editable.length() > 0);
    }
}
