package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.AutonomousPlayer;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.model.DataHolder;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;

public class BattleActivity extends AppCompatActivity {

    public static final String ID_OCEAN = "ocean";
    public static final String ID_OPPONENT = "opponent";

    private Player player;
    private AutonomousPlayer opponent;

    private boolean playerTurn = true; // true if it's player's turn, false if it's opponent's

    private TextView ocean;
    private TextView turn;

    Button okButton;
    Button nextButton;
    EditText coordinateX;
    EditText coordinateY;

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

        turn.setText(R.string.your_turn);
        printOcean();
    }

    public void onOkClicked(View view) {
        Player playerWithTurn;
        okButton = findViewById(R.id.ok_button);
        nextButton = findViewById(R.id.next_button);

        coordinateX = (EditText) findViewById(R.id.coordinate_x);
        coordinateY = (EditText) findViewById(R.id.coordinate_y);

        int coorX = Integer.parseInt(coordinateX.getText().toString());
        int coorY = Integer.parseInt(coordinateY.getText().toString());

        player.launchMissile(coorX, coorY);

        printOcean();

        coordinateX.setVisibility(View.GONE);
        coordinateY.setVisibility(View.GONE);

        coordinateX.getText().clear();
        coordinateY.getText().clear();

        okButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
    }

    public void onNextClicked(View view) {
        if (!isGameFinished()) {
            changeTurn();
            if (getPlayerTurn().equals(opponent)) {
                turn.setText(R.string.opponents_turn);
                opponent.launchMissile();
            } else {
                turn.setText(R.string.your_turn);
                coordinateX.setVisibility(View.VISIBLE);
                coordinateY.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
            }
            printOcean();
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
}
