package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum ShipType implements Parcelable {
    CARRIER(5), BATTLESHIP(4), CRUISER(3), SUBMARINE(3);

    private int size;

    private ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<ShipType> CREATOR = new Creator<ShipType>() {
        @Override
        public ShipType createFromParcel(Parcel source) {
            return ShipType.values()[source.readInt()];
        }

        @Override
        public ShipType[] newArray(int size) {
            return new ShipType[size];
        }
    };
}