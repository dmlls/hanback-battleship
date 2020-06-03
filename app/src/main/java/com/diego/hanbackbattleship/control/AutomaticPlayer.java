package com.diego.hanbackbattleship.control;

import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;

import java.util.Random;

public class AutomaticPlayer extends Player {

    private Random randomCell;

    public AutomaticPlayer() {
        super();
    }

    public AutomaticPlayer(OceanCell[][] cells) {
        super(cells);
    }

    public void launchMissile() {
        int coorX = randomCell.nextInt(Ocean.getHeight());
        int coorY = randomCell.nextInt(Ocean.getWidth());
        super.launchMissile(coorX, coorY);
    }
}
