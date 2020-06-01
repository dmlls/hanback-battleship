package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final ShipType type;
    private List<OceanCell> cells = new ArrayList<>();
    private boolean sunken;
    private int hits; // number of times the ship was hit

    public Ship(ShipType type) {
        this.type = type;
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

    public List<OceanCell> getCells() {
        return cells;
    }

    public boolean isSunken() {
        return sunken;
    }

    public int getHits() {
        return hits;
    }

    public void setCells(List<OceanCell> cells) {
        this.cells = cells;
    }

    public void setSunken(boolean sunken) {
        this.sunken = sunken;
    }

    @Override
    public String toString() {
        return new String(type.toString() + cells.toString() + " Sunken: " + sunken);
    }
}

