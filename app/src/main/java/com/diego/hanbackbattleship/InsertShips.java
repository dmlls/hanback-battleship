package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.model.DataHolder;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ShipType;

public class InsertShips extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Player player;
    private ShipType[] shipTypes;
    private int shipCounter;
    private Orientation orientation = Orientation.HORIZONTAL;

    private TextView shipName;

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

        Spinner orientationSpinner = findViewById(R.id.orientation);
        orientationSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orientation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientationSpinner.setAdapter(adapter);
    }

    public void onOkClicked(View view) {
        EditText coordinateX = (EditText) findViewById(R.id.coordinate_x);
        EditText coordinateY = (EditText) findViewById(R.id.coordinate_y);
        TextView alert = (TextView) findViewById(R.id.already_boat_alert);

        int coorX = Integer.parseInt(coordinateX.getText().toString());
        int coorY = Integer.parseInt(coordinateY.getText().toString());

        if (player.getOcean().isThereShipInCoords(coorX, coorY) && shipCounter != ShipType.values().length) {
            alert.setVisibility(View.VISIBLE);
        } else {
            alert.setVisibility(View.GONE);
            if (shipCounter < ShipType.values().length) {
                Button okButton = (Button) findViewById(R.id.ok_button);

                player.addShip(shipTypes[shipCounter], coorX, coorY, orientation);
                shipCounter++;
                if (shipCounter < ShipType.values().length) {
                    coordinateX.getText().clear();
                    coordinateY.getText().clear();
                    coordinateX.requestFocus();
                    shipName.setText(shipTypes[shipCounter].toString());
                } else if (shipCounter == ShipType.values().length) {
                    okButton.setText(R.string.next);
                }
                displayShips();
            } else {
                Intent intent = new Intent(this, BattleActivity.class);
                DataHolder.getInstance().save(BattleActivity.ID_OCEAN, player.getOcean());
                DataHolder.getInstance().save(BattleActivity.ID_OPPONENT, player.getOpponent());
                startActivity(intent);
            }
        }
    }

    private void displayShips() {
        TextView ocean = findViewById(R.id.ocean);
        ocean.setText(player.printOcean());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            orientation = Orientation.HORIZONTAL;
        } else {
            orientation = Orientation.VERTICAL;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
