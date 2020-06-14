package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    public static final String EXTRA_WINNER = "winner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        boolean playerIsWinner = intent.getBooleanExtra(EXTRA_WINNER, false);

        TextView winner = (TextView) findViewById(R.id.game_over_text);
        Button playAgain = (Button) findViewById(R.id.play_again);

        if (playerIsWinner) {
            winner.setText(R.string.you_won);
        } else {
            winner.setText(R.string.game_over);
        }
    }

    public void onPlayAgainClicked(View view) {
        Intent intent = new Intent(this, InsertShips.class);
        startActivity(intent);
    }
}