package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.List;

public class Referee {
    private Ocean ocean;

    public Referee() {
        ocean = new Ocean();
    }

    public Referee(List<Ship> ships) {
        ocean = new Ocean(ships);
    }

    public void addShip(ShipType shipType, int coorX, int coorY, Orientation orientation) {
        ocean.addShip(new Ship(shipType, coorX, coorY, orientation));
    }

    public ShipType[] getShipTypes() {
        return ShipType.values();
    }

    public Ocean getOcean() {
        return ocean;
    }

    public String printOcean() {
        return ocean.toString();
    }
}
