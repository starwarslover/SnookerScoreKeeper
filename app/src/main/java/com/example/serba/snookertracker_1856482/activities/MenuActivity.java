package com.example.serba.snookertracker_1856482.activities;

import android.app.Application;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.serba.snookertracker_1856482.R;
import com.example.serba.snookertracker_1856482.dialogs.MatchTypeDialog;
import com.example.serba.snookertracker_1856482.dialogs.MatchTypeListener;
import com.example.serba.snookertracker_1856482.models.ThemeUtils;
import com.example.serba.snookertracker_1856482.dialogs.SettingsDialog;
import com.example.serba.snookertracker_1856482.dialogs.SettingsDialogListener;

public class MenuActivity extends AppCompatActivity {
    private int theme = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.setDialogListener(new SettingsDialogListener() {
                    @Override
                    public void onThemeSelected(long themeId) {
                        handleSelectedTheme(themeId);
                    }
                });
                settingsDialog.show(fragmentTransaction, "settings_dialog");
            }
        });

        findViewById(R.id.start_match_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                MatchTypeDialog matchTypeDialog = new MatchTypeDialog();
                matchTypeDialog.setMatchTypeListener(new MatchTypeListener() {
                    @Override
                    public void onMatchTypeSelected(boolean teamMode) {
                        Intent intent = new Intent(MenuActivity.this, GameSetupActivity.class);
                        intent.putExtra(GameSetupActivity.TEAM_MODE, teamMode);
                        startActivity(intent);
                    }
                });
                matchTypeDialog.show(transaction, "match_type_dialog");
            }
        });

        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.this.finish();
                System.exit(0);
            }
        });
    }

    private void handleSelectedTheme(long themeId) {
        int theme = -1;
        if (themeId == 0) {
            theme = R.style.GrassTheme;
        } else if (themeId == 1) {
            theme = R.style.SkyTheme;
        }

        ThemeUtils.setSelectedThemeId(MenuActivity.this, theme);

        MenuActivity.this.finish();
        startActivity(new Intent(MenuActivity.this, MenuActivity.class));
        Toast.makeText(this, getResources().getString(R.string.theme_changed), Toast.LENGTH_SHORT).show();
    }
}
