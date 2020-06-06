package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ScoreValues;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private Ocean ocean;
    private Player opponent;
    private int score;
    private boolean previouslyHitOpponent; // true if in the player's last turn, they hit opponent's boat
    private boolean previouslySunkOpponent; // true if in the player's last turn, they sunk opponent's boat

    public Player() {
        ocean = new Ocean();
    }

    public Player(Ocean ocean, Player opponent, int score) {
        this.ocean = ocean;
        this.opponent = opponent;
        this.score = score;
    }

    public boolean addShip(ShipType shipType, int coorX, int coorY, Orientation orientation) { // true if ship could be added, false otherwise
        Ship ship = new Ship(shipType);
        List<OceanCell> cells = new ArrayList<>();
        int shipSize = shipType.getSize();
        if (orientation.equals(Orientation.HORIZONTAL)) {
            if (coorY + shipSize > Ocean.getWidth()) {
                coorY -= coorY + shipSize - Ocean.getWidth(); // readjust (move ship to the left)
            }
            for (int y = coorY; y < coorY + shipSize; y++) {
                cells.add(ocean.getCell(coorX, y));
            }
        } else {
            if (coorX + shipSize > Ocean.getHeight()) {
                coorX -= coorX + shipSize - Ocean.getHeight(); // readjust (move ship upwards)
            }
            for (int x = coorX; x < coorX + shipSize; x++) {
                cells.add(ocean.getCell(x, coorY));
            }
        }
        if (!Collections.disjoint(ocean.getOccupiedCells(), cells)) { // if they have cells in common
            return false;
        }
        for (OceanCell cell : cells) {
            cell.setShip(ship);
            ship.addCell(cell);
        }
        ocean.addShip(ship);
        return true;
    }

    public void removeShip(Ship ship) {
        ocean.removeShip(ship);
    }

    public void launchMissile(int coorX, int coorY) {
        OceanCell opponentCell = opponent.getOcean().getCell(coorX, coorY);
        if (!opponentCell.wasVisited()) {
            opponentCell.setVisited(true);
            Ship opponentShip = opponentCell.getShip();
            if (opponentShip != null) {
                opponentShip.hit(opponentCell);
                if (opponentShip.isSunken()) {
                    previouslySunkOpponent = true;
                } else {
                    previouslyHitOpponent = true;
                }
            } else {
                previouslySunkOpponent = false;
                previouslyHitOpponent = false;
            }
            updateScore(opponentShip);
        }
    }

    private void updateScore(Ship opponentShip) {
        if (opponentShip != null) {
            if (opponentShip.isSunken()) {
                this.score += ScoreValues.SUNK.getValue();
                opponent.updateScore(ScoreValues.SUNK_BY_OPP.getValue());
            } else {
                this.score += ScoreValues.HIT.getValue();
                opponent.updateScore(ScoreValues.HIT_BY_OPP.getValue());
            }
            if (previouslyHitOpponent) {
                this.score += ScoreValues.HIT_PREVIOUSLY.getValue();
                opponent.updateScore(ScoreValues.HIT_PREV_BY_OPP.getValue());
            } else if (previouslySunkOpponent) {
                this.score += ScoreValues.SUNK_PREVIOUSLY.getValue();
                opponent.updateScore(ScoreValues.SUNK_PREV_BY_OPP.getValue());
            }
        }
    }

    public void updateScore(int change) {
        score += change;
    }

    public boolean hasLost() {
        for (Ship ship : ocean.getShips()) {
            if (!ship.isSunken()) {
                return false;
            }
        }
        return true;
    }

    public ShipType[] getShipTypes() {
        return ShipType.values();
    }

    public Ocean getOcean() {
        return ocean;
    }

    public Player getOpponent() {
        return opponent;
    }

    public int getScore() {
        return score;
    }

    public boolean getPreviouslyHitOpponent() {
        return previouslyHitOpponent;
    }

    public boolean getPreviouslySunkOpponent() {
        return previouslySunkOpponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setPreviouslyHitOpponent(boolean previouslyHitOpponent) {
        this.previouslyHitOpponent = previouslyHitOpponent;
    }

    public void setPreviouslySunkOpponent(boolean previouslySunkOpponent) {
        this.previouslySunkOpponent = previouslySunkOpponent;
    }

    public String printOcean() {
        return ocean.printOcean();
    }

    public String printOceanOnlyVisited() {
        String oceanString = printOcean();
        StringBuilder oceanOnlyVisited = new StringBuilder();
        for (int x = 0; x < Ocean.getHeight(); x++) {
            for (int y = 0; y < Ocean.getWidth(); y++) {
                if (ocean.getCell(x, y).wasVisited()) {
                    oceanOnlyVisited.append(oceanString.charAt((Ocean.getWidth() + 1) * x + y)); // width + 1 because of "\n" characters
                } else {
                    oceanOnlyVisited.append("□");
                }
            }
            oceanOnlyVisited.append("\n");
        }
        return oceanOnlyVisited.toString();
    }
}
