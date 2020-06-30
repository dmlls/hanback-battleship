package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutonomousPlayerIntelligent extends AutonomousPlayer {

    private Random random = new Random();
    private List<Ship> opponentShips;
    private boolean shipFound;
    private List<OceanCell> targetShipCells;
    private OceanCell lastCell;
    private List<OceanCell> notVisitedCells = new ArrayList<>();


    public AutonomousPlayerIntelligent(Ocean opponentOcean) {
        super();
        opponentShips = opponentOcean.getShips();
        notVisitedCells = opponentOcean.getAllCellsAsList();
    }

    @Override
    public ShipState launchMissile() {
        if (shipFound) {
            targetShipCells.add(notVisitedCells.remove(random.nextInt(notVisitedCells.size()))); // add random cells to make it less obvious
            OceanCell targetCell = targetShipCells.remove(random.nextInt(targetShipCells.size()));
            if (targetCell.getShipStateInCell().equals(ShipState.SUNKEN)) {
                shipFound = false;
            }
            lastCell = targetCell;
            notVisitedCells.remove(lastCell);
            return callSuperLaunchMissile(targetCell);
        } else {
            if (random.nextInt(4) == 1) {
                shipFound = true;
                Ship targetShip = opponentShips.remove(random.nextInt(opponentShips.size()));
                targetShipCells = targetShip.getCells();
                return launchMissile();
            } else {
                ShipState result =  super.launchMissile(); // launch randomly
                lastCell = super.getLastCell();
                notVisitedCells.remove(lastCell);
                return result;
            }
        }
    }

    @Override
    public int[] getLastShotCoordinates() {
        return lastCell.getCoordinates();
    }
}
