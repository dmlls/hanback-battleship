package com.diego.hanbackbattleship.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Ocean {
    private static final int HEIGHT = 8; // same as physical buttons on the board
    private static final int WIDTH = 8;
    private List<Ship> ships = new ArrayList<>(); // ships on the ocean

    public Ocean() {}

    public Ocean(List<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    private boolean isShipInCoordinates(Ship ship, int coordinateX, int coordinateY) {
        int[] shipBaseCoords = ship.getBaseCoordinates();
        if (ship.getOrientation() == Orientation.HORIZONTAL) {
            return shipBaseCoords[1] <= coordinateY &&
                    coordinateY < shipBaseCoords[1] + ship.getType().getSize() &&
                    shipBaseCoords[0] == coordinateX;
        } else {
            return shipBaseCoords[0] <= coordinateX &&
                    coordinateX < shipBaseCoords[0] + ship.getType().getSize() &&
                    shipBaseCoords[1] == coordinateY;
        }
    }

    public Ship getShip(int coordinateX, int coordinateY) {
        for (Ship ship : ships) {
            if (isShipInCoordinates(ship, coordinateX, coordinateY)) {
                return ship;
            }
        }
        return null;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public String printOcean() {
        return toString();
    }

    @Override
    @NonNull
    public String toString() {
        StringBuilder ocean = new StringBuilder();
        Ship ship;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                ship = getShip(i, j);
                if (ship != null) {
                    if ((ship.getOrientation().equals(Orientation.HORIZONTAL) &&
                            ship.getHits()[j - ship.getBaseCoordinates()[1]]) ||
                            (ship.getOrientation().equals(Orientation.VERTICAL) &&
                                    ship.getHits()[i - ship.getBaseCoordinates()[0]])) {
                        if (ship.getState().equals(ShipState.SUNKEN)) {
                            ocean.append("■");
                        } else {
                            ocean.append("◪");
                        }
                    } else {
                        ocean.append("■");
                    }
                } else {
                    ocean.append("▩");
                }
            }
            ocean.append("\n");
        }
        return ocean.toString();
    }
}

