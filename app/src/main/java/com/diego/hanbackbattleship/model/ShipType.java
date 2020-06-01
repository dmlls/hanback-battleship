package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum ShipType {
    CARRIER(5), BATTLESHIP(4), CRUISER(3), SUBMARINE(3);

    private int size;

    private ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}