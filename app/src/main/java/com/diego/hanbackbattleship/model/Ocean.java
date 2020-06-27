package com.diego.hanbackbattleship.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public void addShip(Ship ship, List<OceanCell> cells) {
        this.ships.add(ship);
        ship.setCells(cells);
        for (int i = 0; i < cells.size(); i++) {
            OceanCell cell = cells.get(i);
            this.cells[cell.getCoordinates()[0]][cell.getCoordinates()[1]].setShip(ship, i);
        }
        occupiedCells.addAll(cells);
    }

    public void removeShip(Ship ship) {
        List<OceanCell> shipCells = ship.getCells();
        this.ships.remove(ship);
        occupiedCells.removeAll(shipCells);
        ship.setCells(null);
        for (OceanCell cell : shipCells) {
            this.cells[cell.getCoordinates()[0]][cell.getCoordinates()[1]].removeShip();
        }
    }

    public Ship getShip(int coorX, int coorY) {
        return cells[coorX][coorX].getShip();
    }

    public Ship getShip(ShipType shipType) {
        for (Ship ship : ships) {
            if (ship.getType().equals(shipType)) {
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

    public OceanCell getCell(int coorX, int coorY) {
        return cells[coorX][coorY];
    }

    public OceanCell[][] getAllCells() {
        return cells;
    }

    public List<OceanCell> getAllCellsAsList() {
        List<OceanCell> cellsList = new ArrayList<>();
        for (int x = 0; x < HEIGHT; x++) {
            cellsList.addAll(Arrays.asList(cells[x]));
        }
        return cellsList;
    }

    public List<OceanCell> getOccupiedCells() {
        return occupiedCells;
    }

    public boolean isThereShipInCoords(int coorX, int coorY) {
        return cells[coorX][coorY].getShip() != null;
    }

    public void setCell(int x, int y, OceanCell cell) {
        this.cells[x][y] = cell;
    }

    public void addOccupiedCell(OceanCell oceanCell) {
        this.occupiedCells.add(oceanCell);
    }

    public Ocean copy() {
        Ocean oceanCopy = new Ocean();
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                oceanCopy.setCell(x, y, this.cells[x][y]);
                if (this.occupiedCells.contains(this.cells[x][y])) {
                    oceanCopy.addOccupiedCell(oceanCopy.getAllCells()[x][y]);
                }
            }
        }
        for (Ship ship : this.ships) {
            Ship shipCopy = ship.copy();
            oceanCopy.addShip(shipCopy, shipCopy.getCells());
        }
        return oceanCopy;
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
                        ocean.append("▣");
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

