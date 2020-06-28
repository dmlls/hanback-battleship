package com.diego.hanbackbattleship.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diego.hanbackbattleship.R;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.io.IOException;
import java.util.HashMap;

import pl.droidsonroids.gif.GifDrawable;

public class OceanPrinter {

    private static final int HORIZONTAL_MARGIN = 15;
    private static final int TOP_OFFSET = 38;

    private static final int QUARTER_FOCUS_BLINKING_DURATION = 2000;
    private static final int SHIP_BLINKING_DURATION = 700;
    private static final int MOVEMENT_DURATION = 600;

    private HashMap<ShipType, int[]> shipInitialCoordinates;

    private View currentBlinkingShip;
    private boolean blinkWasStarted;

    private Context context;
    private Ocean ocean;
    private int oceanSize; // HEIGHT = WIDTH

    private int sizeOfCell;

    private View quarterFocus;

    private ImageView[][] ivBaseCells;
    private ImageView[][] ivTopCells;
    private ImageView[] ivBattleship;
    private ImageView[] ivCarrier;
    private ImageView[] ivCruiser;
    private ImageView[] ivSubmarine;

    private Drawable[] battleship = new Drawable[4];
    private Drawable[] battleshipSunken = new Drawable[4];
    private Drawable[] carrier = new Drawable[5];
    private Drawable[] carrierSunken = new Drawable[5];
    private Drawable[] cruiser = new Drawable[3];
    private Drawable[] cruiserSunken = new Drawable[3];
    private Drawable[] submarine = new Drawable[3];
    private Drawable[] submarineSunken = new Drawable[3];
    private GifDrawable shootHit;
    private GifDrawable shipSunk;
    private GifDrawable shootWater;
    private Drawable sea;
    private Drawable water;

    private LinearLayout oceanBaseLayout;
    private LinearLayout oceanTopLayout;
    private LinearLayout battleshipLayout;
    private LinearLayout carrierLayout;
    private LinearLayout cruiserLayout;
    private LinearLayout submarineLayout;

    public OceanPrinter(Context context, Ocean ocean) {
        this.context = context;
        this.ocean = ocean;
        this.oceanSize = Ocean.getHeight();
        quarterFocus = ((Activity) context).findViewById(R.id.quarter_focus);
        try {
            initializeResources();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initializeResources() throws IOException {
        battleship[0] = context.getResources().getDrawable(R.drawable.ic_battleship_1);
        battleship[1] = context.getResources().getDrawable(R.drawable.ic_battleship_2);
        battleship[2] = context.getResources().getDrawable(R.drawable.ic_battleship_3);
        battleship[3] = context.getResources().getDrawable(R.drawable.ic_battleship_4);

        carrier[0] = context.getResources().getDrawable(R.drawable.ic_carrier_1);
        carrier[1] = context.getResources().getDrawable(R.drawable.ic_carrier_2);
        carrier[2] = context.getResources().getDrawable(R.drawable.ic_carrier_3);
        carrier[3] = context.getResources().getDrawable(R.drawable.ic_carrier_4);
        carrier[4] = context.getResources().getDrawable(R.drawable.ic_carrier_5);

        cruiser[0] = context.getResources().getDrawable(R.drawable.ic_cruiser_1);
        cruiser[1] = context.getResources().getDrawable(R.drawable.ic_cruiser_2);
        cruiser[2] = context.getResources().getDrawable(R.drawable.ic_cruiser_3);

        submarine[0] = context.getResources().getDrawable(R.drawable.ic_submarine_1);
        submarine[1] = context.getResources().getDrawable(R.drawable.ic_submarine_2);
        submarine[2] = context.getResources().getDrawable(R.drawable.ic_submarine_3);

        battleshipSunken[0] = context.getResources().getDrawable(R.drawable.ic_battleship_sunken_1);
        battleshipSunken[1] = context.getResources().getDrawable(R.drawable.ic_battleship_sunken_2);
        battleshipSunken[2] = context.getResources().getDrawable(R.drawable.ic_battleship_sunken_3);
        battleshipSunken[3] = context.getResources().getDrawable(R.drawable.ic_battleship_sunken_4);

        carrierSunken[0] = context.getResources().getDrawable(R.drawable.ic_carrier_sunken_1);
        carrierSunken[1] = context.getResources().getDrawable(R.drawable.ic_carrier_sunken_2);
        carrierSunken[2] = context.getResources().getDrawable(R.drawable.ic_carrier_sunken_3);
        carrierSunken[3] = context.getResources().getDrawable(R.drawable.ic_carrier_sunken_4);
        carrierSunken[4] = context.getResources().getDrawable(R.drawable.ic_carrier_sunken_5);

        cruiserSunken[0] = context.getResources().getDrawable(R.drawable.ic_cruiser_sunken_1);
        cruiserSunken[1] = context.getResources().getDrawable(R.drawable.ic_cruiser_sunken_2);
        cruiserSunken[2] = context.getResources().getDrawable(R.drawable.ic_cruiser_sunken_3);

        submarineSunken[0] = context.getResources().getDrawable(R.drawable.ic_submarine_sunken_1);
        submarineSunken[1] = context.getResources().getDrawable(R.drawable.ic_submarine_sunken_2);
        submarineSunken[2] = context.getResources().getDrawable(R.drawable.ic_submarine_sunken_3);

        shootHit = new GifDrawable(context.getResources(), R.raw.shoot_hit);
        shipSunk = new GifDrawable(context.getResources(), R.raw.ship_sunk);
        shootWater = new GifDrawable(context.getResources(), R.raw.shoot_water);

        sea = context.getResources().getDrawable(R.drawable.ic_sea);
        water = context.getResources().getDrawable(R.drawable.ic_water);

        sizeOfCell = Math.round((float) context.getResources().getDisplayMetrics().widthPixels / oceanSize) - HORIZONTAL_MARGIN;

        oceanBaseLayout = ((Activity) context).findViewById(R.id.ocean_base);
        oceanTopLayout = ((Activity) context).findViewById(R.id.ocean_top);
        battleshipLayout = ((Activity) context).findViewById(R.id.battleship);
        carrierLayout = ((Activity) context).findViewById(R.id.carrier);
        cruiserLayout = ((Activity) context).findViewById(R.id.cruiser);
        submarineLayout = ((Activity) context).findViewById(R.id.submarine);

        ivBaseCells = new ImageView[oceanSize][oceanSize];
        ivTopCells = new ImageView[oceanSize][oceanSize];
        ivBattleship = new ImageView[battleship.length];
        ivCarrier = new ImageView[carrier.length];
        ivCruiser = new ImageView[cruiser.length];
        ivSubmarine = new ImageView[submarine.length];

        shipInitialCoordinates = new HashMap<>();
    }

    private void setColorFilter(LinearLayout shipLayout) {
        LinearLayout linShip = (LinearLayout) shipLayout.getChildAt(0);
        for (int j = 0; j < linShip.getChildCount(); j++) {
            ImageView ivShip = (ImageView) linShip.getChildAt(j);
            ivShip.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            ivShip.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void removeAllColorFilters(LinearLayout shipLayout) {
        LinearLayout linShip = (LinearLayout) shipLayout.getChildAt(0);
        for (int j = 0; j < linShip.getChildCount(); j++) {
            ImageView ivShip = (ImageView) linShip.getChildAt(j);
            ivShip.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            ivShip.getDrawable().setColorFilter(null);
        }
    }

    private void startBlinking(View view, int blinkingDuration, float minAlpha) {
        Animation anim = new AlphaAnimation(1.0f, minAlpha);
        anim.setDuration(blinkingDuration);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    private void stopBlinking(View view) {
        view.clearAnimation();
    }

    private void startShipBlinking(View view) {
        if (view != null && !blinkWasStarted) {
            startBlinking(view, SHIP_BLINKING_DURATION, 0.0f);
            blinkWasStarted = true;
        }
        currentBlinkingShip = view;
    }

    private void stopShipBlinking(View view) {
        if (view != null && blinkWasStarted) {
            stopBlinking(view);
            blinkWasStarted = false;
        }
        currentBlinkingShip = null;
    }

    private LinearLayout getShipLinearLayout(ShipType shipType) {
        switch (shipType) {
            case BATTLESHIP:
                return battleshipLayout;
            case CARRIER:
                return carrierLayout;
            case CRUISER:
                return cruiserLayout;
            case SUBMARINE:
                return submarineLayout;
        }
        return null;
    }

    private ImageView[] getShipImageViews(ShipType shipType) {
        switch (shipType) {
            case BATTLESHIP:
                return ivBattleship;
            case CARRIER:
                return ivCarrier;
            case CRUISER:
                return ivCruiser;
            case SUBMARINE:
                return ivSubmarine;
        }
        return null;
    }

    private Drawable[] getShipDrawables(ShipType shipType) {
        switch (shipType) {
            case BATTLESHIP:
                return battleship;
            case CARRIER:
                return carrier;
            case CRUISER:
                return cruiser;
            case SUBMARINE:
                return submarine;
        }
        return null;
    }

    private Drawable[] getShipSunkenDrawables(ShipType shipType) {
        switch (shipType) {
            case BATTLESHIP:
                return battleshipSunken;
            case CARRIER:
                return carrierSunken;
            case CRUISER:
                return cruiserSunken;
            case SUBMARINE:
                return submarineSunken;
        }
        return null;
    }

    public void moveShipToIllegalCoords(ShipType shipType, int[] illegalCoords, Orientation orientation) {
        Player player = new Player();
        player.addShip(shipType, illegalCoords[0], illegalCoords[1], orientation);
        Ship ship = player.getOcean().getShip(shipType);
        displayShipWhenAddingShips(ship);

        LinearLayout shipLayout = getShipLinearLayout(shipType);
        setColorFilter(shipLayout);
        startShipBlinking(shipLayout);
    }

    public void printOceanWhenAddingShips(ShipType shipType) {
        printOceanWhenAddingShips();
        if (currentBlinkingShip != null && !currentBlinkingShip.equals(getShipLinearLayout(shipType))) {
            stopShipBlinking(currentBlinkingShip);
        }
        startShipBlinking(getShipLinearLayout(shipType));
    }

    private void clearLayouts() {
        if (oceanBaseLayout != null && oceanBaseLayout.getChildCount() > 0) {
            oceanBaseLayout.removeAllViews();
        }

        if (oceanTopLayout != null && oceanTopLayout.getChildCount() > 0) {
            oceanTopLayout.removeAllViews();
        }
    }

    public void printOceanVisitedWithShips() {
        LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
        LinearLayout.LayoutParams oceanRowParams = new LinearLayout.LayoutParams(sizeOfCell * oceanSize, sizeOfCell);

        clearLayouts();

        for (int i = 0; i < oceanSize; i++) {
            LinearLayout linRowBase = new LinearLayout(context);
            LinearLayout linRowTop = new LinearLayout(context);
            for (int j = 0; j < oceanSize; j++) {
                ivBaseCells[i][j] = new ImageView(context);
                ivTopCells[i][j] = new ImageView(context);
                OceanCell cell = ocean.getCell(i, j);
                // Fill the base layout
                if (cell.getShip() != null) {
                    int slice = cell.getShipSlice();
                    ivBaseCells[i][j].setImageDrawable(getShipDrawables(cell.getShip().getType())[slice]);
                    ivBaseCells[i][j].setAlpha(0.5f);
                    if (cell.getShip().getOrientation().equals(Orientation.VERTICAL)) {
                        ivBaseCells[i][j].setRotation(90f);
                    }
                } else {
                    ivBaseCells[i][j].setImageDrawable(sea);
                }
                // Fill the top layout
                if (cell.wasVisited()) {
                    switch (cell.getShipStateInCell()) {
                        case NO_SHIP:
                            ivTopCells[i][j].setImageDrawable(water);
                            break;
                        case HIT:
                            ivTopCells[i][j].setImageDrawable(shootHit);
                            break;
                        case SUNKEN:
                            int slice = cell.getShipSlice();
                            Drawable[] shipDrawable = getShipSunkenDrawables(cell.getShip().getType());
                            ivTopCells[i][j].setImageDrawable(shipDrawable[slice]);
                            if (cell.getShip().getOrientation().equals(Orientation.VERTICAL)) {
                                ivTopCells[i][j].setRotation(90f);
                            }
                            break;
                    }
                }
                linRowBase.addView(ivBaseCells[i][j], cellParams);
                linRowTop.addView(ivTopCells[i][j], cellParams);
            }
            oceanBaseLayout.addView(linRowBase, oceanRowParams);
            oceanTopLayout.addView(linRowTop, oceanRowParams);
        }
    }

    public void printOceanVisited() {
        LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
        LinearLayout.LayoutParams oceanRowParams = new LinearLayout.LayoutParams(sizeOfCell * oceanSize, sizeOfCell);

        clearLayouts();

        for (int i = 0; i < oceanSize; i++) {
            LinearLayout linRowBase = new LinearLayout(context);
            for (int j = 0; j < oceanSize; j++) {
                ivBaseCells[i][j] = new ImageView(context);
                OceanCell cell = ocean.getCell(i, j);
                if (cell.wasVisited()) {
                    switch (cell.getShipStateInCell()) {
                        case NO_SHIP:
                            ivBaseCells[i][j].setImageDrawable(water);
                            break;
                        case HIT:
                            ivBaseCells[i][j].setImageDrawable(shootHit);
                            break;
                        case SUNKEN:
                            int slice = cell.getShipSlice();
                            Drawable[] shipDrawable = getShipSunkenDrawables(cell.getShip().getType());
                            ivBaseCells[i][j].setImageDrawable(shipDrawable[slice]);
                            if (cell.getShip().getOrientation().equals(Orientation.VERTICAL)) {
                                ivBaseCells[i][j].setRotation(90f);
                            }
                            break;
                    }
                } else {
                    ivBaseCells[i][j].setImageDrawable(sea);
                }
                linRowBase.addView(ivBaseCells[i][j], cellParams);
            }
            oceanBaseLayout.addView(linRowBase, oceanRowParams);
        }
    }

    public void printOceanWhenAddingShips() {
        LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
        LinearLayout.LayoutParams oceanRowParams = new LinearLayout.LayoutParams(sizeOfCell * oceanSize, sizeOfCell);

        clearLayouts();

        for (int i = 0; i < oceanSize; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < oceanSize; j++) {
                ivBaseCells[i][j] = new ImageView(context);
                ivBaseCells[i][j].setImageDrawable(sea);
                linRow.addView(ivBaseCells[i][j], cellParams);
            }
            oceanBaseLayout.addView(linRow, oceanRowParams);
        }
        for (Ship ship : ocean.getShips()) {
            displayShipWhenAddingShips(ship);
            LinearLayout shipLayout = getShipLinearLayout(ship.getType());
            removeAllColorFilters(shipLayout);
        }
    }

    private void displayShipWhenAddingShips(Ship ship) {
        LinearLayout shipLayout = getShipLinearLayout(ship.getType());
        ImageView[] ivShip =  getShipImageViews(ship.getType());
        Drawable[] shipDrawable = getShipDrawables(ship.getType());

        if (shipLayout != null && ivShip != null & shipDrawable != null) {
            if (shipInitialCoordinates.get(ship.getType()) == null) {
                LinearLayout linShip = new LinearLayout(context);
                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);

                for (int k = 0; k < shipDrawable.length; k++) {
                    ivShip[k] = new ImageView(context);
                    ivShip[k].setImageDrawable(shipDrawable[k]);
                    linShip.addView(ivShip[k], cellParams);
                }
                shipLayout.bringToFront();
                int[] coordsOnScreen = calculateRelativeCoordinatesOnScreen(ship, oceanBaseLayout);
                LinearLayout.LayoutParams shipParams = new LinearLayout.LayoutParams(sizeOfCell * shipDrawable.length, sizeOfCell);
                shipLayout.setPivotX(coordsOnScreen[0]);
                shipLayout.setPivotY(coordsOnScreen[1] + sizeOfCell);
                shipParams.setMargins(coordsOnScreen[0], coordsOnScreen[1], 0, 0);
                shipLayout.addView(linShip, shipParams);
                shipInitialCoordinates.put(ship.getType(), ship.getCells().get(0).getCoordinates());
            } else {
                float rotation = ship.getOrientation().equals(Orientation.HORIZONTAL) ? 0f : 90f;
                shipLayout.animate().rotation(rotation);
                float[] xyValues = calculateXYAnimation(oceanBaseLayout, ship.getCells().get(0).getCoordinates(),
                                                            shipInitialCoordinates.get(ship.getType()), ship.getOrientation());
                shipLayout.animate().x(xyValues[0]).y(xyValues[1]).setDuration(MOVEMENT_DURATION);
            }
        }
    }

    private int[] calculateRelativeCoordinatesOnScreen(Ship ship, View view) {
        int[] cordsOnScreen = new int[2];
        int[] coordsStartCell = ship.getCells().get(0).getCoordinates();
        int[] viewCoordsOnScreen = new int[2];
        view.getLocationOnScreen(viewCoordsOnScreen);
        cordsOnScreen[0] = viewCoordsOnScreen[0] + coordsStartCell[1] * sizeOfCell;
        cordsOnScreen[1] = viewCoordsOnScreen[1] + coordsStartCell[0] * sizeOfCell - TOP_OFFSET;
        return cordsOnScreen;
    }

    private float[] calculateXYAnimation(View view, int[] startCoordinates, int[] firstShipCoordinates, Orientation orientation) {
        float[] xyValues = {0f, 0f};
        int[] viewCoordsOnScreen = new int[2];
        view.getLocationOnScreen(viewCoordsOnScreen);
        xyValues[0] =  startCoordinates[1] * sizeOfCell - firstShipCoordinates[1] * sizeOfCell;
        xyValues[1] = startCoordinates[0] * sizeOfCell - firstShipCoordinates[0] * sizeOfCell;
        if (orientation.equals(Orientation.VERTICAL)) {
            xyValues[1] -= sizeOfCell;
        }
        return xyValues;
    }

    public void placeQuarters(KeypadQuarter currentQuarter) {
        int[] oceanCoords = new int[2];
        oceanBaseLayout.getLocationOnScreen(oceanCoords);
        int size = 4 * sizeOfCell;
        oceanCoords[1] -= TOP_OFFSET;

        View quarterUpperLeft = ((Activity) context).findViewById(R.id.quarter_1);
        View quarterUpperRight = ((Activity) context).findViewById(R.id.quarter_2);
        View quarterLowerLeft = ((Activity) context).findViewById(R.id.quarter_3);
        View quarterLowerRight = ((Activity) context).findViewById(R.id.quarter_4);

        placeQuarters(quarterUpperLeft, KeypadQuarter.UPPER_LEFT, oceanCoords, size);
        placeQuarters(quarterUpperRight, KeypadQuarter.UPPER_RIGHT, oceanCoords, size);
        placeQuarters(quarterLowerLeft, KeypadQuarter.LOWER_LEFT, oceanCoords, size);
        placeQuarters(quarterLowerRight, KeypadQuarter.LOWER_RIGHT, oceanCoords, size);
        placeQuarters(quarterFocus, currentQuarter, oceanCoords, size);

        startBlinking(quarterFocus, QUARTER_FOCUS_BLINKING_DURATION, 0.5f);
    }

    private void placeQuarters(View view, KeypadQuarter quarter, int[] oceanCoords, int size) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
        switch (quarter) {
            case UPPER_LEFT:
                layoutParams.setMargins(oceanCoords[0], oceanCoords[1], 0, 0);
                break;
            case UPPER_RIGHT:
                layoutParams.setMargins(oceanCoords[0] + size, oceanCoords[1], 0, 0);
                break;
            case LOWER_LEFT:
                layoutParams.setMargins(oceanCoords[0], oceanCoords[1] + size, 0, 0);
                break;
            case LOWER_RIGHT:
                layoutParams.setMargins(oceanCoords[0] + size, oceanCoords[1] + size, 0, 0);
                break;
        }
        view.setLayoutParams(layoutParams);
    }


    public KeypadQuarter changeQuarterFocus(View view) {
        View quarterFocus = ((Activity) context).findViewById(R.id.quarter_focus);
        int size = 4 * sizeOfCell;
        int[] oceanCoords = new int[2];
        oceanBaseLayout.getLocationOnScreen(oceanCoords);
        oceanCoords[1] -= TOP_OFFSET;

        switch (view.getId()) {
            case R.id.quarter_1:
                quarterFocus.setX(oceanCoords[0]);
                quarterFocus.setY(oceanCoords[1]);
                return KeypadQuarter.UPPER_LEFT;
            case R.id.quarter_2:
                quarterFocus.setX(oceanCoords[0] + size);
                quarterFocus.setY(oceanCoords[1]);
                return KeypadQuarter.UPPER_RIGHT;
            case R.id.quarter_3:
                quarterFocus.setX(oceanCoords[0]);
                quarterFocus.setY(oceanCoords[1] + size);
                return KeypadQuarter.LOWER_LEFT;
            case R.id.quarter_4:
                quarterFocus.setX(oceanCoords[0] + size);
                quarterFocus.setY(oceanCoords[1] + size);
                return KeypadQuarter.LOWER_RIGHT;
            default:
                return null;
        }
    }

    public void fadeInQuarterFocus() {
        quarterFocus.animate().alpha(1f).setDuration(QUARTER_FOCUS_BLINKING_DURATION);
    }

    public void fadeOutQuarterFocus() {
        quarterFocus.animate().alpha(0f).setDuration(QUARTER_FOCUS_BLINKING_DURATION);
    }
}
