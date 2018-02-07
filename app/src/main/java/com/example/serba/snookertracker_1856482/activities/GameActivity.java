package com.example.serba.snookertracker_1856482.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.serba.snookertracker_1856482.R;
import com.example.serba.snookertracker_1856482.dialogs.MatchResultsDialog;
import com.example.serba.snookertracker_1856482.dialogs.MatchResultsDialogListener;
import com.example.serba.snookertracker_1856482.models.APlayer;
import com.example.serba.snookertracker_1856482.models.FrameListener;
import com.example.serba.snookertracker_1856482.models.FrameManager;
import com.example.serba.snookertracker_1856482.models.GamePlayerHolder;
import com.example.serba.snookertracker_1856482.models.SnookerBalls;
import com.example.serba.snookertracker_1856482.models.SoloPlayer;

public class GameActivity extends AppCompatActivity {
    private boolean teamModeOn = false;
    private APlayer playerOne;
    private APlayer playerTwo;
    private FrameManager frameManager;
    private GamePlayerHolder team_1_player_1_holder;
    private GamePlayerHolder team_1_player_2_holder;
    private GamePlayerHolder team_2_player_1_holder;
    private GamePlayerHolder team_2_player_2_holder;
    private TextView strikingPlayerLabel;
    private FloatingActionButton missButton;
    private View breakGroup;
    private FloatingActionButton redBallButton;
    private TextView teamOneScoreLabel;
    private TextView teamTwoScoreLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamModeOn = extras.getBoolean(GameSetupActivity.TEAM_MODE);
            playerOne = (APlayer) extras.getSerializable(GameSetupActivity.PLAYER_ONE);
            playerTwo = (APlayer) extras.getSerializable(GameSetupActivity.PLAYER_TWO);
        }

        team_1_player_1_holder = new GamePlayerHolder(findViewById(R.id.team_1_player_1));
        team_2_player_1_holder = new GamePlayerHolder(findViewById(R.id.team_2_player_1));

        if (teamModeOn) {
            team_1_player_2_holder = new GamePlayerHolder(findViewById(R.id.team_1_player_2));
            team_2_player_2_holder = new GamePlayerHolder(findViewById(R.id.team_2_player_2));
            findViewById(R.id.team_mode_group).setVisibility(View.VISIBLE);
        }

//        Init players
        team_1_player_1_holder.setPlayer((SoloPlayer) playerOne.getNextPlayer());
        team_2_player_1_holder.setPlayer((SoloPlayer) playerTwo.getNextPlayer());
        if (teamModeOn) {
            team_1_player_2_holder.setPlayer((SoloPlayer) playerOne.getNextPlayer());
            team_2_player_2_holder.setPlayer((SoloPlayer) playerTwo.getNextPlayer());
        }

        frameManager = new FrameManager();
        frameManager.setFirstContestant(playerOne);
        frameManager.setSecondContestant(playerTwo);

        frameManager.setFrameListener(new FrameListener() {
            @Override
            public void updateUI() {
                team_1_player_1_holder.updateViews();
                team_2_player_1_holder.updateViews();
                if (teamModeOn) {
                    team_1_player_2_holder.updateViews();
                    team_2_player_2_holder.updateViews();
                    teamOneScoreLabel.setText(String.valueOf(playerOne.getScore()));
                    teamTwoScoreLabel.setText(String.valueOf(playerTwo.getScore()));
                }
                if (frameManager.isBreakMode()) {
                    redBallButton.setVisibility(View.GONE);
                    breakGroup.setVisibility(View.VISIBLE);
                } else {
                    redBallButton.setVisibility(View.VISIBLE);
                    breakGroup.setVisibility(View.GONE);
                }

                if (frameManager.isFinalPot() && !frameManager.isGameOver()) {
                    redBallButton.setBackgroundTintList(getResources().getColorStateList(frameManager.getFinalPotColor()));
                } else if (frameManager.isGameOver()) {
                    endGame();
                }
            }
        });
        frameManager.initialiseFrame();
        strikingPlayerLabel.setText(frameManager.getCurrentPlayer().getName());
    }

    public void initViews() {
        strikingPlayerLabel = findViewById(R.id.striking_player_label);
        breakGroup = findViewById(R.id.break_group);
        redBallButton = findViewById(R.id.ball_red_button);
        teamOneScoreLabel = findViewById(R.id.team_1_score_label);
        teamTwoScoreLabel = findViewById(R.id.team_2_score_label);

        setViewTag(R.id.ball_red_button, SnookerBalls.RED);
        setViewTag(R.id.ball_yellow_button, SnookerBalls.YELLOW);
        setViewTag(R.id.ball_green_button, SnookerBalls.GREEN);
        setViewTag(R.id.ball_brown_button, SnookerBalls.BROWN);
        setViewTag(R.id.ball_blue_button, SnookerBalls.BLUE);
        setViewTag(R.id.ball_pink_button, SnookerBalls.PINK);
        setViewTag(R.id.ball_black_button, SnookerBalls.BLACK);

        findViewById(R.id.ball_miss_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameManager.nextPlayer();
                strikingPlayerLabel.setText(frameManager.getCurrentPlayer().getName());
            }
        });

        findViewById(R.id.foul_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameManager.foul();
                strikingPlayerLabel.setText(frameManager.getCurrentPlayer().getName());
            }
        });

        findViewById(R.id.end_frame_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGame();
            }
        });
    }

    public void setViewTag(int viewId, Object tag) {
        findViewById(viewId).setTag(tag);
    }

    public void handlePottedBall(View v) {
        if (!frameManager.isFinalPot()) {
            SnookerBalls pottedBall = (SnookerBalls) v.getTag();
            frameManager.potBall(pottedBall);
        } else {
            frameManager.potBall(frameManager.getFinalPotSeriesBall());
        }
    }

    public void endGame() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchResultsDialog matchResultsDialog = new MatchResultsDialog();

        Bundle extras = new Bundle();
        extras.putSerializable(GameSetupActivity.PLAYER_ONE, playerOne);
        extras.putSerializable(GameSetupActivity.PLAYER_TWO, playerTwo);
        extras.putBoolean(GameSetupActivity.TEAM_MODE, teamModeOn);
        matchResultsDialog.setArguments(extras);

        matchResultsDialog.setCancelable(false);
        matchResultsDialog.setDialogListener(new MatchResultsDialogListener() {
            @Override
            public void onPlayAgainSelected() {
                frameManager.initialiseFrame();
            }

            @Override
            public void onEndMatchSelected() {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        matchResultsDialog.show(transaction, "game_over_dialog");
    }
}
