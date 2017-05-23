package com.example.pivotal.checklist.views;

import android.widget.LinearLayout;

import com.example.pivotal.checklist.BuildConfig;
import com.example.pivotal.checklist.models.Checklist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CheckListViewHolderTest {

    private CheckListViewHolder mSubject;

    @Before
    public void setup() {
        LinearLayout parent = new LinearLayout(RuntimeEnvironment.application);
        mSubject = CheckListViewHolder.newInstance(parent);
    }

    @Test
    public void setChecklist_withCheckListNameSet_showsChecklistName() {
        String checklistName = "Checklist Name";
        Checklist checkList = mock(Checklist.class);
        when(checkList.getName()).thenReturn(checklistName);

        mSubject.setCheckList(checkList, 0, null);

        assertThat(mSubject.checkListNameTextView).hasTextString(checklistName);
    }

    @Test
    public void setChecklist_onChecklistClicked_notifiesChecklistClickedListener() {
        Checklist checkList = mock(Checklist.class);
        CheckListViewHolder.OnChecklistClickedListener onChecklistClickedListener = mock(CheckListViewHolder.OnChecklistClickedListener.class);

        mSubject.setCheckList(checkList, 0, onChecklistClickedListener);
        mSubject.checklistContainer.performClick();

        verify(onChecklistClickedListener).onChecklistClicked(checkList);
    }

    @Test
    public void setChecklist_onDeleteButtonClicked_notifiesChecklistClickedListener() {
        int position = 0;
        Checklist checklist = mock(Checklist.class);
        CheckListViewHolder.OnChecklistClickedListener onChecklistClickedListener = mock(CheckListViewHolder.OnChecklistClickedListener.class);

        mSubject.setCheckList(checklist, position, onChecklistClickedListener);
        mSubject.deleteCheckListButton.performClick();

        verify(onChecklistClickedListener).onDeleteChecklist(position, checklist);
    }
}
