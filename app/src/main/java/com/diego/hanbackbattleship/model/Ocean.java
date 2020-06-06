package com.diego.hanbackbattleship.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ocean {
    private static final int HEIGHT = 8; // same as physical buttons on the board
    private static final int WIDTH = 8;
    private OceanCell[][] cells = new OceanCell[HEIGHT][WIDTH];
    private List<Ship> ships = new ArrayList<>(); // ships on the ocean
    private List<OceanCell> occupiedCells = new ArrayList<>();

    public Ocean() {
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                int[] coords = {x, y};
                cells[x][y] = new OceanCell(coords);
            }
        }
    }

    public Ocean(OceanCell[][] cells) {
        this.cells = cells;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
        occupiedCells.addAll(ship.getCells());
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
        occupiedCells.removeAll(ship.getCells());
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

    public OceanCell getCell(int coorX, int coorY) {
        return cells[coorX][coorY];
    }

    public OceanCell[][] getAllCells() {
        return cells;
    }

    public List<OceanCell> getOccupiedCells() {
        return occupiedCells;
    }

    public String printOcean() {
        return toString();
    }

    @Override
    @NonNull
    public String toString() {
        StringBuilder ocean = new StringBuilder();
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                OceanCell cell = cells[x][y];
                switch (cell.getShipStateInCell()) {
                    case NO_SHIP:
                        ocean.append("▩");
                        break;
                    case UNHARMED:
                        ocean.append("□");
                        break;
                    case HIT:
                        ocean.append("◪");
                        break;
                    case SUNKEN:
                        ocean.append("■");
                        break;
                }
            }
            ocean.append("\n");
        }
        return ocean.toString();
    }
}

