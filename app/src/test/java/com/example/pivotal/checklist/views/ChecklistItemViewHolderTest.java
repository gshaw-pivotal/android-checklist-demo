package com.example.pivotal.checklist.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout;

import com.example.pivotal.checklist.BuildConfig;
import com.example.pivotal.checklist.models.ChecklistItem;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChecklistItemViewHolderTest {

    private ChecklistItemViewHolder mSubject;

    @Before
    public void setup() {
        LinearLayout layout = new LinearLayout(RuntimeEnvironment.application);
        mSubject = ChecklistItemViewHolder.newInstance(layout);
    }

    @Test
    public void setChecklistItem_setNote_showsNote() {
        String noteText = "the note";
        ChecklistItem checklistItem = mock(ChecklistItem.class);

        when(checklistItem.getNote()).thenReturn(noteText);

        mSubject.setChecklistItem(checklistItem, 0, null);

        assertThat(mSubject.noteText).hasTextString(noteText);
    }

    @Test
    public void setChecklistItem_setChecked_showsChecked() {
        ChecklistItem checklistItem = mock(ChecklistItem.class);

        when(checklistItem.isChecked()).thenReturn(true);

        mSubject.isCheckedBox.setChecked(false);
        mSubject.setChecklistItem(checklistItem, 0, null);

        assertThat(mSubject.isCheckedBox).isChecked();
    }

    @Test
    public void setChecklistItem_deleteButtonClicked_notifiesChecklistItemChangedListener() {
        ChecklistItem checklistItem = mock(ChecklistItem.class);
        int position = 0;
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListener =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);

        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListener);
        mSubject.deleteNoteButton.performClick();

        verify(onCheckListItemChangedListener).onNoteDelete(position);
    }

    @Test
    public void setChecklistItem_checkBoxClicked_notifiesChecklistItemChangedListener() {
        ChecklistItem checklistItem = mock(ChecklistItem.class);
        int position = 0;
        Boolean isChecked = true;
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListener =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);

        mSubject.isCheckedBox.setChecked(!isChecked);
        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListener);
        mSubject.isCheckedBox.performClick();

        verify(onCheckListItemChangedListener).onCheckChanged(position, isChecked);
    }

    @Test
    public void setChecklistItem_assignCheckListTwice_doesNotNotifyChecklistItemChangedListener() {
        int position = 0;
        Boolean isCheckedInitial = true;
        Boolean isCheckedFinal = false;
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListenerInitial =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListenerFinal =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);
        ChecklistItem checklistItem = mock(ChecklistItem.class);
        when(checklistItem.isChecked()).thenReturn(isCheckedInitial);

        mSubject.isCheckedBox.setChecked(!isCheckedInitial);
        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListenerInitial);
        when(checklistItem.isChecked()).thenReturn(isCheckedFinal);
        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListenerFinal);

        verify(onCheckListItemChangedListenerInitial, never()).onCheckChanged(position, isCheckedInitial);
        verify(onCheckListItemChangedListenerInitial, never()).onCheckChanged(position, isCheckedFinal);
    }

    @Test
    public void setChecklistItem_onTextChanged_notifiesChecklistItemChangedListener() {
        String oldNote = "Old Note";
        String note = "New Note";
        int position = 0;
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListener =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);
        ChecklistItem checklistItem = mock(ChecklistItem.class);
        when(checklistItem.getNote()).thenReturn(oldNote);

        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListener);

        List<TextWatcher> watchers = Shadows.shadowOf(mSubject.noteText).getWatchers();
        Assert.assertTrue(watchers.size() == 1);
        watchers.get(0).afterTextChanged(Editable.Factory.getInstance().newEditable(note));

        verify(onCheckListItemChangedListener, never()).onNoteChanged(position, oldNote);
        verify(onCheckListItemChangedListener).onNoteChanged(position, note);
    }

    @Test
    public void setChecklistItem_settingText_doesNotNotifyOnCheckListItemChangedListener() {
        String oldNote = "Old Note";
        String newNote = "New Note";
        int position = 0;
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListenerInitial =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);
        ChecklistItemViewHolder.OnCheckListItemChangedListener onCheckListItemChangedListenerFinal =
                mock(ChecklistItemViewHolder.OnCheckListItemChangedListener.class);
        ChecklistItem checklistItem = mock(ChecklistItem.class);
        when(checklistItem.getNote()).thenReturn(oldNote);

        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListenerInitial);
        when(checklistItem.getNote()).thenReturn(newNote);
        mSubject.setChecklistItem(checklistItem, position, onCheckListItemChangedListenerFinal);

        verify(onCheckListItemChangedListenerInitial, never()).onNoteChanged(position, oldNote);
        verify(onCheckListItemChangedListenerInitial, never()).onNoteChanged(position, newNote);
    }
}
