package com.example.pivotal.checklist.activities;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pivotal.checklist.R;
import com.example.pivotal.checklist.models.Checklist;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ChecklistActivityTest {

    @Rule
    public ActivityTestRule<ChecklistActivity> mActivityRule = new ActivityTestRule<>(ChecklistActivity.class);

    @Test
    public void onSetTitle_updatesTitleInToolbar() {
        String newTitle = "New Title";
        startActivityWithNewChecklist(0);

        onView(withText(getString(R.string.title_activity_checklist, 0))).check(matches(isDisplayed()));
        onView(withId(R.id.add_checklist_name)).perform(typeText(newTitle), closeSoftKeyboard());
        onView(withId(R.id.set_checklist_title)).perform(click());
        onView(allOf(withText(newTitle), not(withId(R.id.add_checklist_name)))).check(matches(isDisplayed()));
    }

    @Test
    public void onSetTitle_withExistingChecklistAndUpdateTitle_updatesTitleInToolbar() {
        String checklistName = "Old checklist";
        String newTitle = "New checklist";
        Checklist checklist = new Checklist();
        checklist.setName(checklistName);
        startActivityWithChecklist(checklist);

        onView(withText(checklistName)).check(matches(isDisplayed()));
        onView(withId(R.id.add_checklist_name)).perform(typeText(newTitle), closeSoftKeyboard());
        onView(withId(R.id.set_checklist_title)).perform(click());
        onView(allOf(withText(newTitle), not(withId(R.id.add_checklist_name)))).check(matches(isDisplayed()));
    }

    private void startActivityWithNewChecklist(int titleNumber) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_NUMBER_TITLE_EXTRA, titleNumber);
        mActivityRule.launchActivity(intent);
    }

    private void startActivityWithChecklist(Checklist checklist) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.ChecklistExtras.CHECKLIST_EXTRA, checklist);
        mActivityRule.launchActivity(intent);
    }

    private String getString(@StringRes int stringId) {
        return mActivityRule.getActivity().getString(stringId);
    }

    private String getString(@StringRes int stringId, Object... formatArgs) {
        return mActivityRule.getActivity().getString(stringId, formatArgs);
    }
}
