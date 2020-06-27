package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final ShipType type;
    private final Orientation orientation;
    private List<OceanCell> cells = new ArrayList<>();
    private boolean sunken;
    private int hits; // number of times the ship was hit

    public Ship(ShipType type, Orientation orientation) {
        this.type = type;
        this.orientation = orientation;
    }

    public void hit(OceanCell cell) {
        cell.setShipStateInCell(ShipState.HIT);
        hits++;
        if (hits == type.getSize()) {
            sunken = true;
            for (OceanCell c : cells) {
                c.setShipStateInCell(ShipState.SUNKEN);
            }
        }
    }

    public ShipType getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public List<OceanCell> getCells() {
        return cells;
    }

    public boolean isSunken() {
        return sunken;
    }

    public int getHits() {
        return hits;
    }

    public void addCell(OceanCell cell) {
        cells.add(cell);
    }

    public void setCells(List<OceanCell> cells) {
        this.cells = cells;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setSunken(boolean sunken) {
        this.sunken = sunken;
    }

    public Ship copy() {
        Ship shipCopy = new Ship(this.type, this.orientation);
        shipCopy.setHits(this.hits);
        shipCopy.setSunken(this.sunken);
        for (OceanCell cell : cells) {
            shipCopy.addCell(new OceanCell(cell.getCoordinates()));
        }
        for (OceanCell shipCells : shipCopy.getCells()) {
            shipCells.setShip(shipCopy, shipCells.getShipSlice());
        }
        return shipCopy;
    }

    @Override
    public String toString() {
        return new String(type.toString() + cells.toString() + " Sunken: " + sunken);
    }
}

