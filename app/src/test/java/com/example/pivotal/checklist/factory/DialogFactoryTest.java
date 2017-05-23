package com.example.pivotal.checklist.factory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.example.pivotal.checklist.BuildConfig;
import com.example.pivotal.checklist.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;

import butterknife.ButterKnife;

import static junit.framework.Assert.assertTrue;
import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DialogFactoryTest {

    @Test
    public void showDialog_launchesAndShowsDialog() {
        DialogFactory.OnNoteSavedListener onNoteSavedListener = mock(DialogFactory.OnNoteSavedListener.class);
        AlertDialog alertDialog = DialogFactory.showDialog(RuntimeEnvironment.application, onNoteSavedListener);

        Dialog latestDialog = ShadowDialog.getLatestDialog();
        assertThat(latestDialog).isNotNull();
        assertThat(latestDialog).isShowing();
        ShadowDialog shadowDialog = Shadows.shadowOf(latestDialog);
        assertTrue(RuntimeEnvironment.application.getString(R.string.add_note_dialog_title).equals(shadowDialog.getTitle()));
        assertThat(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)).hasTextString(R.string.save_add_note);
        assertThat(alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)).hasTextString(R.string.cancel_add_note);
        EditText noteInput = ButterKnife.findById(alertDialog, R.id.add_note);
        assertThat(noteInput).isVisible();
        assertThat(noteInput).hasHint(R.string.add_note_dialog_message);
    }

    @Test
    public void showDialog_negativeButtonClick_dismissesDialog() {
        DialogFactory.OnNoteSavedListener onNoteSavedListener = mock(DialogFactory.OnNoteSavedListener.class);
        AlertDialog alertDialog = DialogFactory.showDialog(RuntimeEnvironment.application, onNoteSavedListener);

        Dialog latestDialog = ShadowDialog.getLatestDialog();
        assertThat(latestDialog).isNotNull();
        assertThat(latestDialog).isShowing();

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

        assertThat(latestDialog).isNotShowing();
    }

    @Test
    public void showDialog_positiveButtonClick_dismissesDialog() {
        String noteText = "A new note";
        DialogFactory.OnNoteSavedListener onNoteSavedListener = mock(DialogFactory.OnNoteSavedListener.class);
        AlertDialog alertDialog = DialogFactory.showDialog(RuntimeEnvironment.application, onNoteSavedListener);

        Dialog latestDialog = ShadowDialog.getLatestDialog();
        assertThat(latestDialog).isNotNull();
        assertThat(latestDialog).isShowing();

        EditText noteInput = ButterKnife.findById(alertDialog, R.id.add_note);
        noteInput.setText(noteText);

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        assertThat(latestDialog).isNotShowing();

        verify(onNoteSavedListener).onNoteSaved(noteText);
    }
}
