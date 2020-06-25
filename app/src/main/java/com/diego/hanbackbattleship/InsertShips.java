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

import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.miscellaneous.DataHolder;
import com.diego.hanbackbattleship.miscellaneous.KeyToCoordinateTranslator;
import com.diego.hanbackbattleship.miscellaneous.KeypadQuarter;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ShipType;

public class InsertShips extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Player player;
    private ShipType[] shipTypes;
    private int shipCounter;
    private Orientation orientation = Orientation.HORIZONTAL;
    private KeypadQuarter quarter = KeypadQuarter.UPPER_LEFT;

    private TextView shipName;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player = new Player();
        Player opponent = new Player();

        player.setOpponent(opponent);
        opponent.setOpponent(player);

        shipTypes = player.getShipTypes();

        setContentView(R.layout.activity_insert_ships);

        shipName = findViewById(R.id.ship_type);
        shipName.setText(shipTypes[shipCounter].toString());

        okButton = findViewById(R.id.ok_button);

        Spinner quarterSpinner = findViewById(R.id.quarter);
        quarterSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterQuarter = ArrayAdapter.createFromResource(this,
                R.array.quarter, android.R.layout.simple_spinner_item);
        adapterQuarter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(adapterQuarter);

        Spinner orientationSpinner = findViewById(R.id.orientation);
        orientationSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterOrientation = ArrayAdapter.createFromResource(this,
                R.array.orientation, android.R.layout.simple_spinner_item);
        adapterOrientation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientationSpinner.setAdapter(adapterOrientation);

        displayShips();
    }

    public void onOkClicked(View view) {
        shipCounter++;
        if (shipCounter < ShipType.values().length ) {
            shipName.setText(shipTypes[shipCounter].toString());
            okButton.setEnabled(false);
            displayShips();
        } else {
            Intent intent = new Intent(this, BattleActivity.class);
            DataHolder.getInstance().save(BattleActivity.ID_OCEAN, player.getOcean());
            DataHolder.getInstance().save(BattleActivity.ID_OPPONENT, player.getOpponent());
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int[] coords = KeyToCoordinateTranslator.translate(keyCode, quarter);
        TextView alert = findViewById(R.id.already_boat_alert);
        boolean shipWasAdded = player.addShip(shipTypes[shipCounter], coords[0], coords[1], orientation);
        okButton.setEnabled(shipWasAdded);
        if (shipWasAdded) {
            alert.setVisibility(View.GONE);
            displayShips();
            if (shipCounter == ShipType.values().length - 1) {
                okButton.setText(R.string.next);
            }
        } else {
            alert.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void displayShips() {
        TextView ocean = findViewById(R.id.ocean);
        ocean.setText(player.printOcean());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.orientation:
                if (position == 0) {
                    orientation = Orientation.HORIZONTAL;
                } else {
                    orientation = Orientation.VERTICAL;
                }
                break;
            case R.id.quarter:
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
