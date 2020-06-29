package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.KeyToCoordinateTranslator;
import com.diego.hanbackbattleship.miscellaneous.KeypadQuarter;
import com.diego.hanbackbattleship.miscellaneous.OceanPrinter;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ShipType;

public class InsertShips extends AppCompatActivity implements View.OnClickListener {
    private Player player;
    private ShipType[] shipTypes;
    private int shipCounter;
    private Orientation orientation = Orientation.HORIZONTAL;
    private KeypadQuarter currentQuarter = KeypadQuarter.UPPER_LEFT;

    private OceanPrinter playerOceanPrinter;

    Typeface exxaGameTypeFace;
    Typeface gameCubeTypeFace;

    private boolean placeShipButtonEnabled = false;
    private boolean rotateButtonEnabled = false;
    private boolean nextButtonEnabled = false;

    private ImageView cornersLeft;
    private ImageView cornersRight;
    private TextView shipName;
    private ImageView placeShipButton;
    private ImageView rotateButton;
    private ImageView nextButton;

    private int[] lastCoordsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insert_ships);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        player = new Player();
        Player opponent = new Player();

        player.setOpponent(opponent);
        opponent.setOpponent(player);

        playerOceanPrinter = new OceanPrinter(this, player.getOcean());

        shipTypes = player.getShipTypes();

        shipName = findViewById(R.id.ship_type);
        shipName.setText(shipTypes[shipCounter].toString());

        cornersLeft = findViewById(R.id.corners_left);
        cornersRight = findViewById(R.id.corners_right);
        placeShipButton = findViewById(R.id.place_ship_button);
        placeShipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onPlaceShipClicked(v);
            }
        });
        rotateButton = findViewById(R.id.rotate_button);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRotateClicked(v);
            }
        });
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onNextClicked(v);
            }
        });

        TextView placeShipText = findViewById(R.id.place_ship_text);

        // set fonts
        exxaGameTypeFace = Typeface.createFromAsset(getAssets(), "fonts/exxa_game.ttf");
        gameCubeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/game_cube.ttf");

        placeShipText.setTypeface(exxaGameTypeFace);
        shipName.setTypeface(gameCubeTypeFace);

        playerOceanPrinter.printOceanWhenAddingShips();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View quarterUpperLeft = findViewById(R.id.quarter_1);
        View quarterUpperRight = findViewById(R.id.quarter_2);
        View quarterLowerLeft = findViewById(R.id.quarter_3);
        View quarterLowerRight = findViewById(R.id.quarter_4);

        quarterUpperLeft.setOnClickListener(this);
        quarterUpperRight.setOnClickListener(this);
        quarterLowerLeft.setOnClickListener(this);
        quarterLowerRight.setOnClickListener(this);

        playerOceanPrinter.placeQuarters(currentQuarter);
        shipName.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersLeft.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersRight.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
    }


    public void onPlaceShipClicked(View view) {
        if (placeShipButtonEnabled) {
            shipCounter++;
            if (shipCounter < ShipType.values().length ) {
                nextShipAnimation();
                orientation = Orientation.HORIZONTAL;
                playerOceanPrinter.printOceanWhenAddingShips(shipTypes[shipCounter]);
            }
            if (shipCounter == ShipType.values().length) {
                donePlacingShipsAnimation();
            }
        }
    }

    public void onNextClicked(View view) {
        if (nextButtonEnabled) {
            Intent intent = new Intent(this, BattleActivity.class);
            DataHolder.getInstance().save(BattleActivity.ID_OCEAN, player.getOcean());
            DataHolder.getInstance().save(BattleActivity.ID_OPPONENT, player.getOpponent());
            startActivity(intent);
        }
    }

    private void donePlacingShipsAnimation() {
        placeShipButtonEnabled = false;
        rotateButtonEnabled = false;
        rotateButton.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        placeShipButton.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        placeShipButton.animate().xBy(-53f).setDuration(OceanPrinter.SHORT_DURATION);
        placeShipButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                placeShipButton.animate().xBy(53f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.SHORT_DURATION);
        nextButton.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
        nextButton.animate().xBy(-53f).setDuration(OceanPrinter.SHORT_DURATION);
        nextButtonEnabled = true;
    }

    private void nextShipAnimation() {
        shipName.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersLeft.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        cornersRight.animate().alpha(0f).setDuration(OceanPrinter.SHORT_DURATION);
        shipName.postDelayed(new Runnable() {
            @Override
            public void run() {
                shipName.setText(shipTypes[shipCounter].toString());
                shipName.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersLeft.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
                cornersRight.animate().alpha(0.3f).setDuration(OceanPrinter.SHORT_DURATION);
            }
        }, OceanPrinter.SHORT_DURATION);
        placeShipButtonEnabled = false;
        rotateButtonEnabled = false;
        placeShipButton.animate().alpha(0.5f).setDuration(OceanPrinter.SHORT_DURATION);
        rotateButton.animate().alpha(0.5f).setDuration(OceanPrinter.SHORT_DURATION);
    }

    public void onRotateClicked(View view) {
        if (rotateButtonEnabled) {
            orientation = getOppositeOrientation(orientation);
            addShip(lastCoordsInput, orientation);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int[] coords = KeyToCoordinateTranslator.translate(keyCode, currentQuarter);
        lastCoordsInput = coords;
        addShip(coords, orientation);
        rotateButton.animate().alpha(1f).setDuration(OceanPrinter.SHORT_DURATION);
        rotateButtonEnabled = true;
        return true;
    }

    private Orientation getOppositeOrientation(Orientation orientation) {
        return orientation.equals(Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public void addShip(int[] coords, Orientation orientation) {
        boolean shipWasAdded = player.addShip(shipTypes[shipCounter], coords[0], coords[1], orientation);
        placeShipButtonEnabled = shipWasAdded;
        float alpha = shipWasAdded ? 1f : 0.5f;
        placeShipButton.animate().alpha(alpha).setDuration(OceanPrinter.SHORT_DURATION);
        if (shipWasAdded) {
            playerOceanPrinter.printOceanWhenAddingShips(shipTypes[shipCounter]);
        } else {
            playerOceanPrinter.moveShipToIllegalCoords(shipTypes[shipCounter], coords, orientation);
        }
    }

    @Override
    public void onClick(View v) {
        currentQuarter = playerOceanPrinter.changeQuarterFocus(v);
    }
}
