package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.Referee;
import com.diego.hanbackbattleship.model.DataHolder;
import com.diego.hanbackbattleship.model.OceanCell;

import java.util.List;

public class LaunchMissile extends AppCompatActivity {

    public static final String ID_CELLS = "cells";
    private Referee referee;

    private TextView ocean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_ships);

        referee = new Referee((OceanCell[][]) DataHolder.getInstance().retrieve(ID_CELLS));

        ocean = findViewById(R.id.ocean);
        ocean.setText(referee.printOceanOnlyVisited());
    }

    public void onOkClicked(View view) {
        EditText coordinateX = (EditText) findViewById(R.id.coordinate_x);
        EditText coordinateY = (EditText) findViewById(R.id.coordinate_y);

        int coorX = Integer.parseInt(coordinateX.getText().toString());
        int coorY = Integer.parseInt(coordinateY.getText().toString());

        referee.launchMissile(coorX, coorY);
        ocean.setText(referee.printOceanOnlyVisited());
    }
}
