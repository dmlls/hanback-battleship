package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.AutonomousPlayer;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.KeyToCoordinateTranslator;
import com.diego.hanbackbattleship.miscellaneous.KeypadQuarter;
import com.diego.hanbackbattleship.miscellaneous.OceanPrinter;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.ShipState;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ID_OCEAN = "ocean";
    public static final String ID_OPPONENT = "opponent";

    private native int SegmentControl(int score);

    //  private ScoreThread scoreThread = new ScoreThread();

    private Player player;
    private AutonomousPlayer opponent;

    private OceanPrinter playerOceanPrinter;
    private OceanPrinter opponentOceanPrinter;

    private KeypadQuarter currentQuarter = KeypadQuarter.UPPER_LEFT;

    private boolean keyAlreadyPressed;

    private boolean playerTurn = true; // true if it's player's turn, false if it's opponent's

    private Handler handler;

    private TextView turn;
    private TextView launchMissileText;
    private TextView resultText;
    private Button nextButton;
    private TextView alert;

  //  private boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        System.loadLibrary("7segment");

        player = new Player((Ocean) DataHolder.getInstance().retrieve(ID_OCEAN),
                                (Player) DataHolder.getInstance().retrieve(ID_OPPONENT), 0);
        opponent = new AutonomousPlayer();

        player.setOpponent(opponent);
        opponent.setOpponent(player);

        playerOceanPrinter = new OceanPrinter(this, player.getOcean());
        opponentOceanPrinter = new OceanPrinter(this, opponent.getOcean());
/*
        scoreThread.setDaemon(true);
        scoreThread.start();*/

        handler = new Handler();

        turn = findViewById(R.id.turn);
        nextButton = findViewById(R.id.next_button);
        launchMissileText = findViewById(R.id.launch_missile_text);
        resultText = findViewById(R.id.result);
        alert = findViewById(R.id.already_hit_alert);

        turn.setText(R.string.your_turn);
        opponentOceanPrinter.printOceanVisited(); // the player sees their opponent's ocean and vice-versa
        opponentOceanPrinter.fadeInQuarterFocus();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onResume();

        View quarterUpperLeft = findViewById(R.id.quarter_1);
        View quarterUpperRight = findViewById(R.id.quarter_2);
        View quarterLowerLeft = findViewById(R.id.quarter_3);
        View quarterLowerRight = findViewById(R.id.quarter_4);

        quarterUpperLeft.setOnClickListener(this);
        quarterUpperRight.setOnClickListener(this);
        quarterLowerLeft.setOnClickListener(this);
        quarterLowerRight.setOnClickListener(this);

        opponentOceanPrinter.placeQuarters(currentQuarter);
    }

    public void onNextClicked(View view) {
        if (isGameFinished()) {
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra(GameOver.EXTRA_WINNER, getWinner().equals(player));
            startActivity(intent);
        } else {
            changeTurn();
            if (getPlayerTurn().equals(opponent)) {
                opponentOceanPrinter.playChangeTurnAnimation(playerOceanPrinter, true);
                turn.setText(R.string.opponents_turn);
                updateUIBeforeShooting();
                final ShipState result = opponent.launchMissile();
                final int[] coords = opponent.getLastShotCoordinates();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playerOceanPrinter.playShootingAnimation(coords, result, true);
                    }
                }, OceanPrinter.CHANGE_TURN_ANIMATION_DURATION);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateUIAfterShooting(result);
                    }
                }, OceanPrinter.SHOOTING_ANIMATION_DURATION);
                opponentOceanPrinter.fadeOutQuarterFocus();
            } else {
                playerOceanPrinter.playChangeTurnAnimation(opponentOceanPrinter, false);
                turn.setText(R.string.your_turn);
                updateUIBeforeShooting();
                opponentOceanPrinter.printOceanVisited();
                opponentOceanPrinter.fadeInQuarterFocus();
            }
            keyAlreadyPressed = false;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!keyAlreadyPressed && getPlayerTurn().equals(player)) {
            int[] coords = KeyToCoordinateTranslator.translate(keyCode, currentQuarter);
            final ShipState result = player.launchMissile(coords[0], coords[1]);
            if (result == null) { // position already hit
                nextButton.setEnabled(false);
                alert.setVisibility(VISIBLE);
                return false;
            } else {
                opponentOceanPrinter.playShootingAnimation(coords, result, false);
                opponentOceanPrinter.fadeOutQuarterFocus();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateUIAfterShooting(result);
                    }
                }, OceanPrinter.SHOOTING_ANIMATION_DURATION);
                System.out.println(player.getScore());
                keyAlreadyPressed = true;
                return true;
            }
        }
        return false;
    }

    private void updateUIBeforeShooting() {
        launchMissileText.setVisibility(VISIBLE);
        resultText.setVisibility(GONE);
        nextButton.setVisibility(GONE);
    }

    private void updateUIAfterShooting(ShipState result) {
        setResultText(result);
        launchMissileText.setVisibility(GONE);
        alert.setVisibility(GONE);
        resultText.setVisibility(VISIBLE);
        nextButton.setEnabled(true);
        nextButton.setFocusable(false);
        nextButton.setAlpha(0f);
        nextButton.setVisibility(VISIBLE);
        nextButton.animate().alpha(1f);
    }

    private void setResultText(ShipState result) {
        if (result != null) {
            switch (result) {
                case NO_SHIP:
                    resultText.setText(R.string.water);
                    break;
                case HIT:
                    resultText.setText(R.string.hit);
                    break;
                case SUNKEN:
                    resultText.setText(R.string.sunken);
                    break;
            }
        }
    }

    private boolean isGameFinished() {
        return player.hasLost() || opponent.hasLost();
    }


    private Player getWinner() {
        if (isGameFinished()) {
            return player.hasLost() ? opponent : player;
        }
        return null;
    }

    private Player getPlayerTurn() {
        return playerTurn ? player : opponent;
    }

    private Player getPlayerWithoutTurn() {
        return playerTurn ? opponent : player;
    }

    private void changeTurn() {
        playerTurn = !playerTurn;
    }

    @Override
    public void onClick(View v) {
        if (!keyAlreadyPressed) {
            currentQuarter = opponentOceanPrinter.changeQuarterFocus(v);
        }
    }
/*
    private class ScoreThread extends Thread {
        public void run() {
            while(!stopThread) {
                SegmentControl(getPlayerTurn().getScore());
            }
        }
    }*/
}
