package com.diego.hanbackbattleship.control;

import android.util.Log;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AutonomousPlayer extends Player {

    private Random random = new Random();

    public AutonomousPlayer() {
        super();
        int[] coords;
        boolean shipCanBeAdded;
        for (ShipType shipType : getShipTypes()) {
            Orientation orientation = getRandomOrientation();
            do {
                coords = getRandomCoordinates();
                shipCanBeAdded = addShip(shipType, coords[0], coords[1], getRandomOrientation());
            } while (!shipCanBeAdded);
        }
    }

    public AutonomousPlayer(Ocean ocean, Player opponent, int score) {
        super(ocean, opponent, score);
    }

    public void launchMissile() {
        int[] coords = getRandomCoordinates();
        super.launchMissile(coords[0], coords[1]);
    }

    private int[] getRandomCoordinates() {
        return new int[]{random.nextInt(Ocean.getHeight()), random.nextInt(Ocean.getWidth())};
    }

    private Orientation getRandomOrientation() {
        return Orientation.values()[random.nextInt(Orientation.values().length)];
    }
}
