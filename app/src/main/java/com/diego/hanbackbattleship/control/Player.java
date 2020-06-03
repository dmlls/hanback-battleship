package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.ScoreValues;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

public class Player {
    private Ocean ocean;
    private Player opponent;
    private int score;
    private boolean previouslyHit; // true if in the player's last turn, they hit opponent's boat
    private boolean previouslySunk; // true if in the player's last turn, they sunk opponent's boat

    public Player() {
        ocean = new Ocean();
    }

    public Player(OceanCell[][] cells) {
        ocean = new Ocean(cells);
    }

    public void addShip(ShipType shipType, int coorX, int coorY, Orientation orientation) {
        Ship ship = new Ship(shipType);
        if (orientation.equals(Orientation.HORIZONTAL)) {
            for (int y = coorY; y < coorY + shipType.getSize(); y++) {
                ocean.getAllCells()[coorX][y].setShip(ship);
                ship.getCells().add(ocean.getAllCells()[coorX][y]);
            }
        } else {
            for (int x = coorX; x < coorX + shipType.getSize(); x++) {
                ocean.getAllCells()[x][coorY].setShip(ship);
                ship.getCells().add(ocean.getAllCells()[x][coorY]);
            }
        }
        ocean.addShip(ship);
    }

    public void launchMissile(int coorX, int coorY) {
        OceanCell cell = opponent.getOcean().getCell(coorX, coorY);
        if (!cell.wasVisited()) {
            cell.setVisited(true);
            Ship opponentShip = cell.getShip();
            if (opponentShip != null) {
                opponentShip.hit(ocean.getAllCells()[coorX][coorY]);
                updateScore(opponentShip);
            }
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
            if (previouslyHit) {
                this.score += ScoreValues.HIT_PREVIOUSLY.getValue();
            } else if (previouslySunk) {
                this.score += ScoreValues.SUNK_PREVIOUSLY.getValue();
            }
            if (opponent.wasPreviouslyHitByOpponent()) {
                opponent.updateScore(ScoreValues.HIT_PREV_BY_OPP.getValue());
            } else if (opponent.wasPreviouslySunkByOpponent()) {
                opponent.updateScore(ScoreValues.SUNK_PREV_BY_OPP.getValue());
            }
        }
    }

    public void updateScore(int change) {
        score += change;
    }

    public ShipType[] getShipTypes() {
        return ShipType.values();
    }

    public Ocean getOcean() {
        return ocean;
    }

    public boolean wasPreviouslyHitByOpponent() {
        return previouslyHit;
    }

    public boolean wasPreviouslySunkByOpponent() {
        return previouslySunk;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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
