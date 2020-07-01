package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.Difficulty;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.OceanPrinter;

public class SelectDifficultyActivity extends AppCompatActivity {

    public static final String DIFFICULTY_ID = "difficulty";

    private Handler handler;

    private LinearLayout difficultyLayout;
    private TextView difficultyText;
    private TextView easyText;
    private TextView normalText;
    private TextView difficultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler = new Handler();

        difficultyLayout = findViewById(R.id.difficulty_layout);
        difficultyText = findViewById(R.id.difficulty_text);
        easyText = findViewById(R.id.easy_text);
        normalText = findViewById(R.id.normal_text);
        difficultText = findViewById(R.id.difficult_text);

        // set fonts
        Typeface exxaGameTypeFace = Typeface.createFromAsset(getAssets(), "fonts/exxa_game.ttf");
        Typeface gameCubeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/game_cube.ttf");

        difficultyText.setTypeface(gameCubeTypeFace);
        easyText.setTypeface(exxaGameTypeFace);
        normalText.setTypeface(exxaGameTypeFace);
        difficultText.setTypeface(exxaGameTypeFace);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onResume();

        new OceanPrinter(this).startBackgroundAnimation();

        startAnimation();
    }

    private void startAnimation() {
        difficultyLayout.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                easyText.animate().alpha(0.7f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.VERY_SHORT_DURATION);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                normalText.animate().alpha(0.7f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.VERY_SHORT_DURATION * 2);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                difficultText.animate().alpha(0.7f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.VERY_SHORT_DURATION * 3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_text:
                DataHolder.getInstance().save(DIFFICULTY_ID, Difficulty.EASY);
                break;
            case R.id.normal_text:
                DataHolder.getInstance().save(DIFFICULTY_ID, Difficulty.NORMAL);
                break;
            case R.id.difficult_text:
                DataHolder.getInstance().save(DIFFICULTY_ID, Difficulty.DIFFICULT);
                break;
        }
        Intent intent = new Intent(this, InsertShipsActivity.class);
        startActivity(intent);
    }
}