package com.example.pivotal.checklist.activities;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;

import com.example.pivotal.checklist.BuildConfig;
import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.models.ChecklistItem;

import junit.framework.Assert;

import org.assertj.android.api.content.IntentAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowDialog;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChecklistActivityTest {

    private ChecklistActivity activity;
    private ActivityController<ChecklistActivity> activityController;

    @Before
    public void setup() {
        activityController = Robolectric.buildActivity(ChecklistActivity.class);
        activity = activityController.create().start().resume().get();
    }

    @Test
    public void onChecklistNameChangedWithText_updatesCheckListButtonEnableState() {
        activity.setChecklistTitleButton.setEnabled(false);

        activity.onChecklistNameChanged(Editable.Factory.getInstance().newEditable("123"));

        assertThat(activity.setChecklistTitleButton).isEnabled();
    }

    @Test
    public void onChecklistNameChangedWithoutText_updatesCheckListButtonEnableState() {
        activity.setChecklistTitleButton.setEnabled(true);

        activity.onChecklistNameChanged(Editable.Factory.getInstance().newEditable(""));

        assertThat(activity.setChecklistTitleButton).isDisabled();
    }

    @Test
    public void onSetTitleButtonClicked_withValidTitle_updatesToolbar() {
        String title = "New title";

        activity.setTitle("Something");
        activity.checklistTitleEditText.setText(title);
        activity.onSetTitleButtonClicked();

        Assert.assertEquals(activity.getTitle(), title);
    }

    @Test
    public void onSetTitleButtonClicked_withInvalidTitle_updatesToolbar() {
        String oldTitle = "old title";
        String title = "";

        activity.setTitle(oldTitle);
        activity.checklistTitleEditText.setText(title);
        activity.onSetTitleButtonClicked();

        Assert.assertEquals(activity.getTitle(), oldTitle);
    }

    @Test
    public void onFabClicked_showsDialog() {
        activity.onFabClicked();

        Dialog latestDialog = ShadowDialog.getLatestDialog();
        assertThat(latestDialog).isNotNull();
        assertThat(latestDialog).isShowing();
    }

    @Test
    public void onOptionsItemSelected_whenSavingAChecklist_finishesActivityWithData() {
        MenuItem saveMenu = mock(MenuItem.class);
        when(saveMenu.getItemId()).thenReturn(R.id.menu_save);

        activity.onOptionsItemSelected(saveMenu);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent resultIntent = shadowActivity.getResultIntent();
        assertThat(resultIntent).hasExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA);

        Checklist checklist = (Checklist) resultIntent.getSerializableExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA);
        Assert.assertEquals(checklist.getId(), 0);
        Assert.assertEquals(checklist.getName(), RuntimeEnvironment.application.getString(R.string.title_activity_checklist, 0));

        assertThat(activity).isFinishing();
    }

    @Test
    public void onOptionsItemSelected_whenNotSaving_doesNotFinishActivity() {
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(0);

        activity.onOptionsItemSelected(menuItem);

        assertThat(activity).isNotFinishing();
    }

    @Test
    public void onCreate_withNoCheckList_showsDefaultTitle() {
        String title = RuntimeEnvironment.application.getString(R.string.title_activity_checklist, 0);
        Assert.assertEquals(activity.getTitle(), title);
    }

    @Test
    public void onCreate_withNoChecklistAndSecondTitleNumber_showsSecondTitle() {
        int titleNumber = 1;
        String title = RuntimeEnvironment.application.getString(R.string.title_activity_checklist, titleNumber);
        Intent intent = new Intent();
        intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_NUMBER_TITLE_EXTRA, titleNumber);

        activityController = Robolectric.buildActivity(ChecklistActivity.class, intent);
        activity = activityController.create().start().resume().get();

        Assert.assertEquals(activity.getTitle(), title);
    }

    @Test
    public void onCreate_withChecklist_showsChecklistTitle() {
        String checklistName = "name";
        Checklist checklist = new Checklist();
        checklist.setName(checklistName);

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA, checklist);

        activityController = Robolectric.buildActivity(ChecklistActivity.class, intent);
        activity = activityController.create().start().resume().get();

        Assert.assertEquals(activity.getTitle(), checklistName);
    }

    @Test
    public void onCreate_withChecklistWithOneNote_checklistAdapterHasOneNote() {
        Checklist checklist = new Checklist();
        ChecklistItem checklistItem = new ChecklistItem();

        checklist.setChecklistItemList(Collections.singletonList(checklistItem));

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA, checklist);
        activityController = Robolectric.buildActivity(ChecklistActivity.class, intent);
        activity = activityController.create().start().resume().get();

        Assert.assertTrue(activity.checklistRecyclerView.getAdapter().getItemCount() == 1);
    }
}
