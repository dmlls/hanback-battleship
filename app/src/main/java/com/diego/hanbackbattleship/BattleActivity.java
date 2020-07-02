package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.AutonomousPlayer;
import com.diego.hanbackbattleship.control.AutonomousPlayerIntelligent;
import com.diego.hanbackbattleship.control.Difficulty;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.KeyToCoordinateTranslator;
import com.diego.hanbackbattleship.miscellaneous.KeypadQuarter;
import com.diego.hanbackbattleship.miscellaneous.OceanPrinter;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.ShipState;

import java.util.concurrent.TimeUnit;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {

    static { System.loadLibrary("fullcolorled"); }
    public native int FLEDControl(int ledNum, int r, int g, int b);

    public static final String ID_OCEAN = "ocean";
    public static final String ID_OPPONENT = "opponent";



    private Player player;
    private AutonomousPlayer opponent;

    private OceanPrinter playerOceanPrinter;
    private OceanPrinter opponentOceanPrinter;

    private KeypadQuarter currentQuarter = KeypadQuarter.UPPER_LEFT;

    private boolean keyAlreadyPressed;

    private boolean playerTurn = true; // true if it's player's turn, false if it's opponent's

    private boolean nextButtonEnabled = false;

    private Handler handler;

    private Typeface exxaGameTypeFace;
    private Typeface gameCubeTypeFace;

    private TextView turn;
    private TextView subtitle;
    private TextView resultText;
    private ImageView nextButton;
    private ImageView cornersLeft;
    private ImageView cornersRight;

    private boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FLEDControl(5, 0, 0, 0);

        player = new Player((Ocean) DataHolder.getInstance().retrieve(ID_OCEAN),
                                (Player) DataHolder.getInstance().retrieve(ID_OPPONENT), 0);

        switch ((Difficulty) DataHolder.getInstance().retrieve(SelectDifficultyActivity.DIFFICULTY_ID)) {
            default:
            case EASY:
                opponent = new AutonomousPlayer();
                break;
            case NORMAL:
                opponent = new AutonomousPlayerIntelligent(player.getOcean(), false);
            case DIFFICULT:
                opponent = new AutonomousPlayerIntelligent(player.getOcean(), false);
        }


        player.setOpponent(opponent);
        opponent.setOpponent(player);

        playerOceanPrinter = new OceanPrinter(this, player.getOcean());
        opponentOceanPrinter = new OceanPrinter(this, opponent.getOcean());

        handler = new Handler();

        turn = findViewById(R.id.turn);
        subtitle = findViewById(R.id.subtitle);
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onNextClicked(v);
            }
        });
        resultText = findViewById(R.id.result);
        cornersLeft = findViewById(R.id.corners_left);
        cornersRight = findViewById(R.id.corners_right);

        // set fonts
        exxaGameTypeFace = Typeface.createFromAsset(getAssets(), "fonts/exxa_game.ttf");
        gameCubeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/game_cube.ttf");
        Typeface doctorGlitch = Typeface.createFromAsset(getAssets(), "fonts/doctorGlitch.otf");

        turn.setTypeface(gameCubeTypeFace);
        turn.setText(R.string.your_turn);

        subtitle.setTypeface(exxaGameTypeFace);
        subtitle.setText(R.string.launch);

        resultText.setTypeface(doctorGlitch);

<<<<<<< HEAD
        opponentOceanPrinter.printOceanVisited(); // the player sees their opponent's ocean and vice-versa
=======
        opponentOceanPrinter.printOceanVisitedWithShips(); // the player sees their opponent's ocean and vice-versa // TODO: quitar withSHips
>>>>>>> master
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

        LinearLayout oceanLayout = findViewById(R.id.ocean_base);
        int[] oceanCoordsOnScreen = new int[2];
        oceanLayout.getLocationOnScreen(oceanCoordsOnScreen);
        resultText.setY((float) oceanCoordsOnScreen[1] + (float) oceanLayout.getHeight() / 2 -
                            (float) playerOceanPrinter.getSizeOfCell() / 2);

        opponentOceanPrinter.placeQuarters(currentQuarter);
        playerOceanPrinter.startBackgroundAnimation();
    }

    private void changeTurnAnimation() {
        final ImageView cornersSubtitle = findViewById(R.id.corners_subtitle);
        final ImageView oceanFrame = findViewById(R.id.ocean_frame);
        final ImageView oceanBackground = findViewById(R.id.ocean_background);
        final FrameLayout oceanContainer = findViewById(R.id.ocean_container);

        nextButtonEnabled = false;
        nextButton.setVisibility(View.INVISIBLE);

        resultText.animate().scaleXBy(-0.5f).scaleYBy(-0.5f).setDuration(OceanPrinter.SHORT_DURATION);
        nextButton.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        turn.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        subtitle.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersLeft.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersRight.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersSubtitle.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        oceanFrame.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        oceanBackground.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        oceanContainer.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        turn.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getPlayerTurn().equals(opponent)) {
                    playerOceanPrinter.printOceanVisitedWithShips();
                } else {
                    opponentOceanPrinter.printOceanVisited();
                    opponentOceanPrinter.fadeInQuarterFocus();
                }
                changeSubtitle();
                turn.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersLeft.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersRight.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersSubtitle.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                oceanBackground.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                oceanContainer.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                oceanFrame.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.CHANGE_TURN_ANIMATION_DURATION);
    }

    private void changeSubtitle() {
        float subtitleAlpha;
        if (getPlayerTurn().equals(opponent)) {
            turn.setText(R.string.opponents);
            subtitle.setText(R.string.turn);
            subtitle.setTypeface(gameCubeTypeFace);
            subtitle.setAllCaps(true);
            subtitleAlpha = 1f;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 7, 0, 0);
            subtitle.setLayoutParams(params);
        } else {
            turn.setText(R.string.your_turn);
            subtitle.setText(R.string.launch);
            subtitleAlpha = 0.7f;
            subtitle.setTypeface(exxaGameTypeFace);
            subtitle.setAllCaps(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, (int) getResources().getDimension(R.dimen.margin_small), 0, 0);
            subtitle.setLayoutParams(params);
        }
        subtitle.animate().alpha(subtitleAlpha).setDuration(OceanPrinter.CHANGE_TURN_ANIMATION_DURATION);
    }

    public void onNextClicked(View view) {
        if (nextButtonEnabled) {
            if (isGameFinished()) {
                Intent intent = new Intent(this, GameOverActivity.class);
                intent.putExtra(GameOverActivity.EXTRA_WINNER, getWinner().equals(player));
                startActivity(intent);
            } else {
                changeTurn();
                changeTurnAnimation();
                if (getPlayerTurn().equals(opponent)) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final ShipState result = opponent.launchMissile();
                            final int[] coords = opponent.getLastShotCoordinates();
<<<<<<< HEAD
                            System.out.println("Coords: [" + coords[0] + ", " + coords[1] + "] Result: " + result);
=======
>>>>>>> master
                            playerOceanPrinter.playShootingAnimation(coords, result, true);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateUIAfterShooting(result);
                                }
                            }, OceanPrinter.SHOOTING_ANIMATION_DURATION + OceanPrinter.INTERMEDIATE_DURATION);
                            opponentOceanPrinter.fadeOutQuarterFocus();
                        }
                    }, OceanPrinter.CHANGE_TURN_ANIMATION_DURATION + OceanPrinter.INTERMEDIATE_DURATION);
                }
                keyAlreadyPressed = false;
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!keyAlreadyPressed && getPlayerTurn().equals(player)) {
            int[] coords = KeyToCoordinateTranslator.translate(keyCode, currentQuarter);
            final ShipState result = player.launchMissile(coords[0], coords[1]);
            if (result == null) { // position already hit
                nextButtonEnabled = false;
                nextButton.setVisibility(View.INVISIBLE);
                opponentOceanPrinter.positionAlreadyHitAnimation(coords);
                return false;
            } else {
                fullColorLedControlAnimation(result);
                opponentOceanPrinter.playShootingAnimation(coords, result, false);
                opponentOceanPrinter.fadeOutQuarterFocus();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateUIAfterShooting(result);
                    }
                }, OceanPrinter.SHOOTING_ANIMATION_DURATION + OceanPrinter.INTERMEDIATE_DURATION);
<<<<<<< HEAD
=======
                System.out.println(player.getScore());
>>>>>>> master
                keyAlreadyPressed = true;
                return true;
            }
        }
        return false;
    }

    private void fullColorLedControlAnimation(ShipState result) {
        FledThread fledThread = new FledThread(result);
        fledThread.setDaemon(true);
        fledThread.start();
    }


    private void updateUIAfterShooting(ShipState result) {
        nextButtonEnabled = true;
        nextButton.setVisibility(View.VISIBLE);
        nextButton.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
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

    @Override
    protected void onStop() {
        super.onStop();
        FLEDControl(5, 0, 0, 0); // clear LEDs
    }

    private class FledThread extends Thread {
        private ShipState result;
        private int[] blueRGB = {0, 0, 255};
        private int[] orangeRGB = {255, 255, 0};
        private int[] redRGB = {255, 0, 0};

        public FledThread(ShipState result) {
            this.result = result;
        }

        public void run() {
            try {
                switch (result) {
                    default:
                    case NO_SHIP: // blue
                        FLEDControl(5, blueRGB[0], blueRGB[1], blueRGB[2]); // turn on all leds
                        TimeUnit.MILLISECONDS.sleep(1000);
                        FLEDControl(5, 0, 0, 0); // turn off all leds
                        break;
                    case HIT: // orange
                        for (int i = 0; i < 5; i++) {
                            FLEDControl(5, orangeRGB[0], orangeRGB[1], orangeRGB[2]);
                            TimeUnit.MILLISECONDS.sleep(100);
                            FLEDControl(5, 0, 0, 0);
                            TimeUnit.MILLISECONDS.sleep(100);
                        }
                        break;
                    case SUNKEN: // red
                        for (int i = 0; i < 20; i++) {
                            FLEDControl(5, redRGB[0], redRGB[1], redRGB[2]);
                            TimeUnit.MILLISECONDS.sleep(50);
                            FLEDControl(5, 0, 0, 0);
                            TimeUnit.MILLISECONDS.sleep(50);
                        }
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
