package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.List;

public class Referee {
    private Ocean ocean;
    private boolean[][] visited = new boolean[Ocean.getHeight()][Ocean.getWidth()];

    public Referee() {
        ocean = new Ocean();
    }

    public Referee(List<Ship> ships) {
        ocean = new Ocean(ships);
    }

    public void addShip(ShipType shipType, int coorX, int coorY, Orientation orientation) {
        ocean.addShip(new Ship(shipType, coorX, coorY, orientation));
    }

    public boolean launchMissile(int coorX, int coorY) { // return true if ship was hit false otherwise
        Ship ship = ocean.getShip(coorX, coorY);

        visited[coorX][coorY] = true;

        if (ship == null) {
            return false;
        }
        if(ship.getOrientation().equals(Orientation.HORIZONTAL)) {
            ship.hit(coorY - ship.getBaseCoordinates()[1]);
        } else {
            ship.hit(coorX - ship.getBaseCoordinates()[0]);
        }
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
        for (int i = 0; i < Ocean.getHeight(); i++) {
            for (int j = 0; j < Ocean.getWidth(); j++) {
                if (visited[i][j]) {
                    oceanOnlyVisited.append(ocean.charAt((Ocean.getWidth() + 1) * i + j)); // width + 1 because of "\n" characters
                } else {
                    oceanOnlyVisited.append("□");
                }
            }
            oceanOnlyVisited.append("\n");
        }
        return oceanOnlyVisited.toString();
    }
}
