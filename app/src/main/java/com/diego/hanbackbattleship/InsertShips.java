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

import com.diego.hanbackbattleship.control.Referee;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.ArrayList;

public class InsertShips extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Referee referee;
    private ShipType[] shipTypes;
    private int shipCounter;
    private Orientation orientation = Orientation.HORIZONTAL;

    private TextView shipName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referee = new Referee();
        shipTypes = referee.getShipTypes();

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
        if (shipCounter < ShipType.values().length) {
            EditText coordinateX = (EditText) findViewById(R.id.coordinate_x);
            EditText coordinateY = (EditText) findViewById(R.id.coordinate_y);
            Button okButton = (Button) findViewById(R.id.ok_button);

            int coorX = Integer.parseInt(coordinateX.getText().toString());
            int coorY = Integer.parseInt(coordinateY.getText().toString());

            referee.addShip(shipTypes[shipCounter], coorX, coorY, orientation);
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
            Intent intent = new Intent(this, LaunchMissile.class);
            intent.putParcelableArrayListExtra(LaunchMissile.EXTRA_SHIPS, (ArrayList<Ship>) referee.getOcean().getShips());
            startActivity(intent);
        }
    }

    private void displayShips() {
        TextView ocean = findViewById(R.id.ocean);
        ocean.setText(referee.printOcean());
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