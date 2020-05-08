package com.diego.hanbackbattleship.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Ocean {
    private static final int HEIGHT = 8; // same as physical buttons on the board
    private static final int WIDTH = 8;
    private List<Ship> ships = new ArrayList<>(); // ships on the ocean

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public boolean isThereShip(int coordinateX, int coordinateY) {
        for (Ship ship : ships) {
            if (isTheShipInCoordinates(ship, coordinateX, coordinateY)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTheShipInCoordinates(Ship ship, int coordinateX, int coordinateY) {
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

    public List<Ship> getShips() {
        return ships;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

    @Override
    @NonNull
    public String toString() {
        StringBuilder ocean = new StringBuilder();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (isThereShip(i, j)) {
                    ocean.append("■  ");
                } else {
                    ocean.append("▩  ");
                }
            }
            ocean.append("\n");
        }
        return ocean.toString();
    }
}

