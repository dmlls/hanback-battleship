package com.diego.hanbackbattleship.model;

public class OceanCell {
    private int[] coordinates;
    private Ship ship;
    private ShipState shipStateInCell;
    private boolean visited;

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

    public boolean wasVisited() {
        return visited;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        setShipStateInCell(ShipState.UNHARMED);
    }

    public void removeShip() {
        ship = null;
        shipStateInCell = ShipState.NO_SHIP;
        visited = false;
    }

    public void setShipStateInCell(ShipState state) {
        this.shipStateInCell = state;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "[" + String.valueOf(coordinates[0] + ", " + String.valueOf(coordinates[1]) + "]");
    }
}
