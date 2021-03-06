package com.example.serba.snookertracker_1856482.dialogs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.serba.snookertracker_1856482.R;
import com.example.serba.snookertracker_1856482.models.ThemeUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsDialog extends DialogFragment {
    private static final String SETTINGS_LISTENER = "SETTINGS_LISTENER";
    private SettingsDialogListener dialogListener;

    public SettingsDialog() {
        // Required empty public constructor
    }

    public void setDialogListener(SettingsDialogListener listener) {
        this.dialogListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.fragment_settings_dialog, container, false);

        int themeId = ThemeUtils.getSelectedThemeId();
        ((Spinner) dialogView.findViewById(R.id.theme_spinner)).setSelection(themeId == R.style.GrassTheme ? 0 : 1);
        dialogView.findViewById(R.id.change_theme_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner themeSpinner = dialogView.findViewById(R.id.theme_spinner);
                long selectedThemeId = themeSpinner.getSelectedItemId();
                dialogListener.onThemeSelected(selectedThemeId);
                SettingsDialog.this.dismiss();
            }
        });
        return dialogView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SETTINGS_LISTENER, dialogListener);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            dialogListener = (SettingsDialogListener) savedInstanceState.getSerializable(SETTINGS_LISTENER);
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
