package com.diego.hanbackbattleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum Orientation implements Parcelable {
    VERTICAL, HORIZONTAL;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<Orientation> CREATOR = new Creator<Orientation>() {
        @Override
        public Orientation createFromParcel(Parcel source) {
            return Orientation.values()[source.readInt()];
        }

        @Override
        public Orientation[] newArray(int size) {
            return new Orientation[size];
        }
    };
}
