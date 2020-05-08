package com.diego.hanbackbattleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.diego.hanbackbattleship.control.Referee;
import com.diego.hanbackbattleship.model.Ship;

import java.util.ArrayList;

public class HitShips extends AppCompatActivity {

    private Referee referee;

    public static final String EXTRA_SHIP = "referee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_ships);

        referee = new Referee(getIntent().<Ship>getParcelableArrayListExtra(EXTRA_SHIP));

        TextView ocean = findViewById(R.id.ocean);
        ocean.setText(referee.printOcean());
    }
}
