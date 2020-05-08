package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum ShipState implements Parcelable {
    UNHARMED, HIT, SUNKEN;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<ShipState> CREATOR = new Creator<ShipState>() {
        @Override
        public ShipState createFromParcel(Parcel source) {
            return ShipState.values()[source.readInt()];
        }

        @Override
        public ShipState[] newArray(int size) {
            return new ShipState[size];
        }
    };
}

