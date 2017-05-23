package com.example.pivotal.checklist.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.models.ChecklistItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class ChecklistRepository {

    ChecklistSqlHelper checklistSqlHelper;
    SQLiteDatabase database;

    public ChecklistRepository(Context context) {
        checklistSqlHelper = new ChecklistSqlHelper(context);
        database = checklistSqlHelper.getWritableDatabase();
    }

    public Observable<Boolean> saveChecklist(final Checklist checklist) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                database.beginTransaction();
                database.insert(ChecklistTable.getTableName(), null, ChecklistTable.getChecklistContentValues(checklist));
                for (ChecklistItem checklistItem : checklist.getChecklistItemList()) {
                    database.insert(ChecklistNoteTable.getTableName(), null, ChecklistNoteTable.getChecklistNoteContentValues(checklist, checklistItem));
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            }
        }).compose(this.<Boolean>provideSetupObservableTransformer());
    }

    public Observable<Boolean> updateChecklist(final Checklist checklist) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                database.beginTransaction();
                database.update(ChecklistTable.getTableName(), ChecklistTable.getChecklistContentValues(checklist), ChecklistTable.Columns.ID + " == ?", new String[]{String.valueOf(checklist.getId())});

                database.delete(ChecklistNoteTable.getTableName(), ChecklistNoteTable.Columns.CHECKLIST_ID + " == ?", new String[]{String.valueOf(checklist.getId())});

                for (ChecklistItem checklistItem : checklist.getChecklistItemList()) {
                    database.insert(ChecklistNoteTable.getTableName(), null, ChecklistNoteTable.getChecklistNoteContentValues(checklist, checklistItem));
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            }
        }).compose(this.<Boolean>provideSetupObservableTransformer());
    }

    public Observable<List<Checklist>> getChecklists() {
        return Observable.fromCallable(new Callable<List<Checklist>>() {
            @Override
            public List<Checklist> call() throws Exception {
                database.beginTransaction();
                List<Checklist> checklists = new ArrayList<>();
                Cursor checklistCursor = database.query(ChecklistTable.getTableName(), null, null, null, null, null, null);
                checklistCursor.move(-1);

                while (checklistCursor.moveToNext()) {
                    Checklist checklist = ChecklistTable.getChecklist(checklistCursor);
                    List<ChecklistItem> checklistItems = new ArrayList<>();

                    Cursor checklistNoteCursor = database.query(ChecklistNoteTable.getTableName(), null, ChecklistNoteTable.Columns.CHECKLIST_ID + " == ?", new String[]{String.valueOf(checklist.getId())}, null, null, null);
                    checklistNoteCursor.move(-1);

                    while(checklistNoteCursor.moveToNext()) {
                        checklistItems.add(ChecklistNoteTable.getChecklistItem(checklistNoteCursor));
                    }

                    checklist.setChecklistItemList(checklistItems);
                    checklists.add(checklist);
                    checklistNoteCursor.close();
                }

                checklistCursor.close();
                database.setTransactionSuccessful();
                database.endTransaction();
                return checklists;
            }
        }).compose(this.<List<Checklist>>provideSetupObservableTransformer());
    }

    private <T> ObservableTransformer<T, T> provideSetupObservableTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public Observable<Boolean> deleteChecklist(final int checkListId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                database.beginTransaction();
                database.delete(ChecklistTable.getTableName(), ChecklistTable.Columns.ID + " == ?", new String[]{String.valueOf(checkListId)});
                database.delete(ChecklistNoteTable.getTableName(), ChecklistNoteTable.Columns.CHECKLIST_ID + " == ?", new String[]{String.valueOf(checkListId)});
                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            }
        }).compose(this.<Boolean>provideSetupObservableTransformer());
    }
}
