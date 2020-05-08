package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ship implements Parcelable {
    private ShipType type;
    private final int[] BASE_COORDINATES;
    private final Orientation ORIENTATION;
    private ShipState state;
    private boolean[] hits; // positions where the ship was hit

    public Ship(ShipType type, int baseCoordinateX, int baseCoordinateY, Orientation orientation) {
        this.type = type;
        BASE_COORDINATES = new int[2];
        BASE_COORDINATES[0] = baseCoordinateX; // X coordinates are calculated to the right
        BASE_COORDINATES[1] = baseCoordinateY; // Y coordinates are calculated downwards
        ORIENTATION = orientation;
        state = ShipState.UNHARMED;
        hits = new boolean[type.getSize()]; // initialized with false by default
    }

    private Ship(Parcel in) {
        this.type = (ShipType) in.readParcelable(ShipType.class.getClassLoader());
        BASE_COORDINATES = new int[2];
        BASE_COORDINATES[0] = in.readInt();
        BASE_COORDINATES[1] = in.readInt();
        ORIENTATION = in.readParcelable(Orientation.class.getClassLoader());
        state = in.readParcelable(ShipState.class.getClassLoader());
        hits = in.createBooleanArray();
    }

    public ShipState hit(int hitPosition) { // 0 <= hitPosition < size
        hits[hitPosition] = true;
        state = ShipState.SUNKEN; // we check later if it was just hit or sunken
        for (boolean hit : hits) {
            if (!hit) {
                state = ShipState.HIT;
                break;
            }
        }
        return state;
    }

    public ShipType getType() {
        return type;
    }

    public int[] getBaseCoordinates() {
        return BASE_COORDINATES;
    }

    public Orientation getOrientation() {
        return ORIENTATION;
    }

    public ShipState getState() {
        return state;
    }

    public boolean[] getHits() {
        return hits;
    }

    @Override
    public String toString() {
        return new String(type.toString() + " X: " + BASE_COORDINATES[0] + " Y: " +
                BASE_COORDINATES[1] + " " + ORIENTATION.toString() + " " + state.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(type, flags);
        dest.writeInt(BASE_COORDINATES[0]);
        dest.writeInt(BASE_COORDINATES[1]);
        dest.writeParcelable(ORIENTATION, flags);
        dest.writeParcelable(state, flags);
        dest.writeBooleanArray(hits);
    }

    public static final Creator<Ship> CREATOR = new Creator<Ship>() {
        @Override
        public Ship createFromParcel(Parcel source) {
            return new Ship(source);
        }

        @Override
        public Ship[] newArray(int size) {
            return new Ship[size];
        }
    };
}

