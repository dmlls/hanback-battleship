package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diego.hanbackbattleship.miscellaneous.OceanPrinter;

import org.w3c.dom.Text;

import java.util.Random;

public class GameOver extends AppCompatActivity {

    public static final String EXTRA_WINNER = "winner";

    private Random random = new Random();

    private TextView youText;
    private TextView winnerText;
    private TextView gameOverText;
    private ConstraintLayout cornersTop;
    private ConstraintLayout cornersBottom;

    private boolean restartAppEnabled;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        boolean playerIsWinner = intent.getBooleanExtra(EXTRA_WINNER, false);

        handler = new Handler();

        youText = findViewById(R.id.you_text);
        winnerText = findViewById(R.id.winner_text);
        gameOverText = findViewById(R.id.game_over_text);
        cornersTop = findViewById(R.id.corners_top);
        cornersBottom = findViewById(R.id.corners_bottom);

        Typeface doctorGlitch = Typeface.createFromAsset(getAssets(), "fonts/doctorGlitch.otf");
        Typeface gameCubeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/game_cube.ttf");

        youText.setTypeface(doctorGlitch);
        winnerText.setTypeface(doctorGlitch);
        gameOverText.setTypeface(gameCubeTypeFace);

        if (playerIsWinner) {
            winnerText.setText(R.string.won);
        } else {
            winnerText.setText(R.string.lost);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onResume();

        new OceanPrinter(this).startBackgroundAnimation();

        startAnimation();
    }

    private void startAnimation() {
        youText.animate().alpha(1f).setDuration(OceanPrinter.VERY_SHORT_DURATION);
        winnerText.animate().alpha(1f).setDuration(OceanPrinter.VERY_SHORT_DURATION);
        gameOverText.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameOverText.setVisibility(View.VISIBLE);
                gameOverText.animate().alpha(0.6f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersTop.animate().alpha(0.3f).setDuration(OceanPrinter.MEDIUM_DURATION);
                cornersBottom.animate().alpha(0.3f).setDuration(OceanPrinter.MEDIUM_DURATION);
            }
        }, OceanPrinter.SHORT_DURATION);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setPlayAgain();
            }
        }, OceanPrinter.LONG_DURATION);
    }

    private void setPlayAgain() {
        final ImageView playAgainButton = findViewById(R.id.play_again);
        gameOverText.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        gameOverText.setAllCaps(true);
        cornersTop.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersBottom.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        youText.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        winnerText.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameOverText.setText(R.string.play_again);
                playAgainButton.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                int[] playAgainCoords = new int[2];
                playAgainButton.getLocationOnScreen(playAgainCoords);
                gameOverText.setY(playAgainCoords[1] + (float) playAgainButton.getLayoutParams().height * 1.2f);
                restartAppEnabled = true;
                gameOverText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameOverText.animate().alpha(1f).setDuration(OceanPrinter.MEDIUM_DURATION);
                    }
                }, OceanPrinter.MEDIUM_DURATION);
            }
        }, OceanPrinter.MEDIUM_DURATION);
    }

    public void onPlayAgainClicked(View view) { // restart app
        if (restartAppEnabled) {
            Intent mStartActivity = new Intent(this, InsertShips.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }
    }
}