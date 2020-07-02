package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutonomousPlayerIntelligent extends AutonomousPlayer {

    private Random random = new Random();
    private boolean difficultMode;
    private List<Ship> opponentShips;
    private boolean shipFound;
    private Ship targetShip;
    private List<OceanCell> targetShipCells;
    private OceanCell lastCellHit;
    private List<OceanCell> unvisitedCells = new ArrayList<>();
  //  private int distance; // used to get a ship surrounding random cells


    public AutonomousPlayerIntelligent(Ocean opponentOcean, boolean difficultMode) {
        super();
        opponentShips = new ArrayList<>(opponentOcean.getShips());
        unvisitedCells = opponentOcean.getAllCellsAsList();
        this.difficultMode = difficultMode;
      //  distance = 2;
    }

    @Override
    public ShipState launchMissile() {
        if (shipFound) {
            OceanCell randomCell;
    /*        int i = 0;
            do {
                randomCell = getSurroundingCell(targetShip, distance);
                i++;
                if (i > 99) {
                    break;
                }
            } while (!unvisitedCells.contains(randomCell));
            targetShipCells.add(randomCell); // add random cells to make it less obvious */
            targetShipCells.add(unvisitedCells.remove(random.nextInt(unvisitedCells.size()))); // add random cells to make it less obvious
            lastCellHit = targetShipCells.remove(random.nextInt(targetShipCells.size()));
            unvisitedCells.remove(lastCellHit);
            ShipState result = callSuperLaunchMissile(lastCellHit);
            while (result == null) {
                result =  super.launchMissile(); // launch randomly
            }
            if (lastCellHit.getShipStateInCell().equals(ShipState.SUNKEN)) {
                shipFound = false;
            }
            return result;
        } else {
            if (random.nextInt(2) == 1 || difficultMode) {
                shipFound = true;
                targetShip = opponentShips.remove(random.nextInt(opponentShips.size()));
                targetShipCells =  new ArrayList<>(targetShip.getCells());
                return launchMissile();
            } else {
                ShipState result =  super.launchMissile(); // launch randomly
                lastCellHit = super.getLastCell();
                unvisitedCells.remove(lastCellHit);
                return result;
            }
        }
    }
/*
    public OceanCell getSurroundingCell(Ship ship, int maxDistance) {
        OceanCell surroundingCell;
        int[] shipCoords = ship.getCells().get(random.nextInt(ship.getCells().size())).getCoordinates();
        boolean validCoords;
        int[] coords = new int[2];
        do {
            if (ship.getOrientation().equals(Orientation.HORIZONTAL)) {
                coords[1] = shipCoords[1];
                coords[0] = shipCoords[0] + random.nextInt(maxDistance) + 1;
                if (coords[0] >= Ocean.getHeight()) {
                    coords[0] = shipCoords[0] - random.nextInt(maxDistance) + 1;
                    validCoords = coords[0] >= 0;
                } else {
                    validCoords = true;
                }
            } else {
                coords[0] = shipCoords[0];
                coords[1] = shipCoords[1] + random.nextInt(maxDistance) + 1;
                if (coords[1] >= Ocean.getWidth()) {
                    coords[1] = shipCoords[1] - random.nextInt(maxDistance) + 1;
                    validCoords = coords[1] >= 0;
                } else {
                    validCoords = true;
                }
            }
        } while (!validCoords);
        System.out.println(coords[0] + " " + coords[1]);
        return opponentOcean.getCell(coords[0], coords[1]);
    }*/

    @Override
    public int[] getLastShotCoordinates() {
        return lastCellHit.getCoordinates();
    }
}
