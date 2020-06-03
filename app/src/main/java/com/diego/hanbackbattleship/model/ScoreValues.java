package com.diego.hanbackbattleship.model;

public enum ScoreValues {
    HIT(500), SUNK(1000), HIT_PREVIOUSLY(200), SUNK_PREVIOUSLY(450),
    HIT_BY_OPP(-250), SUNK_BY_OPP(-500), HIT_PREV_BY_OPP(-100), SUNK_PREV_BY_OPP(-225);

    private int value;

    private ScoreValues(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
