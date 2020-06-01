package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OceanCell {
    private int[] coordinates;
    private Ship ship;
    private ShipState shipStateInCell;

    public OceanCell(int[] coordinates) {
        this.coordinates = coordinates;
        this.shipStateInCell = ShipState.NO_SHIP;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public Ship getShip() {
        return ship;
    }

    public ShipState getShipStateInCell() {
        return shipStateInCell;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        setShipStateInCell(ShipState.UNHARMED);
    }

    public void setShipStateInCell(ShipState state) {
        this.shipStateInCell = state;
    }
}