package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.AutonomousPlayer;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.KeyToCoordinateTranslator;
import com.diego.hanbackbattleship.miscellaneous.KeypadQuarter;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.ShipState;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BattleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String ID_OCEAN = "ocean";
    public static final String ID_OPPONENT = "opponent";

    private Player player;
    private AutonomousPlayer opponent;

    private KeypadQuarter quarter = KeypadQuarter.UPPER_LEFT;

    private boolean keyAlreadyPressed;

    private boolean playerTurn = true; // true if it's player's turn, false if it's opponent's

    private TextView ocean;
    private TextView turn;
    private TextView launchMissileText;
    private TextView resultText;
    private Button nextButton;
    private Spinner quarterSpinner;
    private TextView alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        player = new Player((Ocean) DataHolder.getInstance().retrieve(ID_OCEAN),
                                (Player) DataHolder.getInstance().retrieve(ID_OPPONENT), 0);
        opponent = new AutonomousPlayer();

        player.setOpponent(opponent);
        opponent.setOpponent(player);

        turn = findViewById(R.id.turn);
        ocean = findViewById(R.id.ocean);
        nextButton = findViewById(R.id.next_button);
        launchMissileText = findViewById(R.id.launch_missile_text);
        resultText = findViewById(R.id.result);
        alert = findViewById(R.id.already_hit_alert);

        quarterSpinner = findViewById(R.id.quarter);
        quarterSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterQuarter = ArrayAdapter.createFromResource(this,
                R.array.quarter, android.R.layout.simple_spinner_item);
        adapterQuarter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(adapterQuarter);

        turn.setText(R.string.your_turn);
        printOcean();
    }

    public void onNextClicked(View view) {
        if (isGameFinished()) {
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra(GameOver.EXTRA_WINNER, getWinner().equals(player));
            startActivity(intent);
        } else {
            changeTurn();
            if (getPlayerTurn().equals(opponent)) {
                turn.setText(R.string.opponents_turn);
                launchMissileText.setVisibility(GONE);
                quarterSpinner.setVisibility(GONE);
                ShipState result = opponent.launchMissile();
                setResultText(result);
            } else {
                turn.setText(R.string.your_turn);
                resultText.setVisibility(GONE);
                nextButton.setVisibility(GONE);
                launchMissileText.setVisibility(VISIBLE);
                quarterSpinner.setVisibility(VISIBLE);
            }
            keyAlreadyPressed = false;
            printOcean();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!keyAlreadyPressed) {
            int[] coords = KeyToCoordinateTranslator.translate(keyCode, quarter);
            ShipState result = player.launchMissile(coords[0], coords[1]);
            if (result == null) {
                nextButton.setEnabled(false);
                alert.setVisibility(VISIBLE);
                return false;
            } else {
                setResultText(result);
                launchMissileText.setVisibility(GONE);
                alert.setVisibility(GONE);
                quarterSpinner.setVisibility(GONE);
                resultText.setVisibility(VISIBLE);
                nextButton.setEnabled(true);
                nextButton.setFocusable(false);
                nextButton.setVisibility(VISIBLE);
                keyAlreadyPressed = true;
                printOcean();
                return true;
            }
        }
        return false;
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

    private void printOcean() {
        ocean.setText(getPlayerWithoutTurn().printOceanOnlyVisited());
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            quarter = KeypadQuarter.UPPER_LEFT;
        } else if (position == 1) {
            quarter = KeypadQuarter.UPPER_RIGHT;
        } else if (position == 2) {
            quarter = KeypadQuarter.LOWER_LEFT;
        } else {
            quarter = KeypadQuarter.LOWER_RIGHT;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
