package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

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
import com.diego.hanbackbattleship.model.ShipType;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Referee referee;
    private ShipType[] ships;
    private int shipCounter;
    private Orientation orientation = Orientation.HORIZONTAL;

    private TextView shipName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referee = new Referee();
        ships = referee.getShipTypes();

        setContentView(R.layout.activity_main);

        shipName = findViewById(R.id.ship_type);
        shipName.setText(ships[shipCounter].toString());

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

            referee.addShip(ships[shipCounter], coorX, coorY, orientation);
            shipCounter++;
            if (shipCounter < ShipType.values().length) {
                coordinateX.getText().clear();
                coordinateY.getText().clear();
                coordinateX.requestFocus();
                shipName.setText(ships[shipCounter].toString());
            } /* else if (shipCounter == ShipType.values().length) {
                okButton.setText();
            } */
            displayShips();
        }
    }

    private void displayShips() {
        TextView ships = findViewById(R.id.ship_list);
        ships.setText(referee.printOcean());
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
