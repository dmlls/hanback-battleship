package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipState;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.ArrayList;
import java.util.List;

public class Referee {
    private Ocean ocean;
    private boolean[][] visited = new boolean[Ocean.getHeight()][Ocean.getWidth()];

    public Referee() {
        ocean = new Ocean();
    }

    public Referee(OceanCell[][] cells) {
        ocean = new Ocean(cells);
    }

    public void addShip(ShipType shipType, int coorX, int coorY, Orientation orientation) {
        Ship ship = new Ship(shipType);
        if (orientation.equals(Orientation.HORIZONTAL)) {
            for (int y = coorY; y < coorY + shipType.getSize(); y++) {
                ocean.getCells()[coorX][y].setShip(ship);
                ship.getCells().add(ocean.getCells()[coorX][y]);
            }
        } else {
            for (int x = coorX; x < coorX + shipType.getSize(); x++) {
                ocean.getCells()[x][coorY].setShip(ship);
                ship.getCells().add(ocean.getCells()[x][coorY]);
            }
        }
        ocean.addShip(ship);
    }

    public boolean launchMissile(int coorX, int coorY) { // return true if ship was hit false otherwise
        Ship ship = ocean.getCells()[coorX][coorY].getShip();

        visited[coorX][coorY] = true;

        if (ship == null) {
            return false;
        }

        ship.hit(ocean.getCells()[coorX][coorY]);
        return true;
    }

    public ShipType[] getShipTypes() {
        return ShipType.values();
    }

    public Ocean getOcean() {
        return ocean;
    }

    public String printOcean() {
        return ocean.printOcean();
    }

    public String printOceanOnlyVisited() {
        String ocean = printOcean();
        StringBuilder oceanOnlyVisited = new StringBuilder();
        for (int x = 0; x < Ocean.getHeight(); x++) {
            for (int y = 0; y < Ocean.getWidth(); y++) {
                if (visited[x][y]) {
                    oceanOnlyVisited.append(ocean.charAt((Ocean.getWidth() + 1) * x + y)); // width + 1 because of "\n" characters
                } else {
                    oceanOnlyVisited.append("□");
                }
            }
            oceanOnlyVisited.append("\n");
        }
        return oceanOnlyVisited.toString();
    }
}
