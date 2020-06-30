package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ShipState;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AutonomousPlayer extends Player {

    private Random random = new Random();
    private List<OceanCell> notVisitedCells = new ArrayList<>();
    private OceanCell lastCell;

    public AutonomousPlayer() {
        super();
        int[] coords;
        boolean shipCanBeAdded;
        for (ShipType shipType : getShipTypes()) {
            do {
                coords = getRandomCoordinates();
                shipCanBeAdded = addShip(shipType, coords[0], coords[1], getRandomOrientation());
            } while (!shipCanBeAdded);
        }
        notVisitedCells = super.getOcean().getAllCellsAsList();
    }

    public AutonomousPlayer(Ocean ocean, Player opponent, int score) {
        super(ocean, opponent, score);
    }

    public ShipState launchMissile() {
        OceanCell cell = getRandomNotVisitedCell();
        lastCell = cell;
        return super.launchMissile(cell);
    }

    public ShipState callSuperLaunchMissile(OceanCell cell) {
        return super.launchMissile(cell);
    }

    public int[] getLastShotCoordinates() {
        return lastCell.getCoordinates();
    }

    public OceanCell getLastCell() {
        return lastCell;
    }

    private OceanCell getRandomNotVisitedCell() {
        return notVisitedCells.remove(random.nextInt(notVisitedCells.size()));
    }

    private int[] getRandomCoordinates() {
        return new int[]{random.nextInt(Ocean.getHeight()), random.nextInt(Ocean.getWidth())};
    }

    private Orientation getRandomOrientation() {
        return Orientation.values()[random.nextInt(Orientation.values().length)];
    }
}
