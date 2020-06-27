package com.diego.hanbackbattleship.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diego.hanbackbattleship.R;
import com.diego.hanbackbattleship.control.Player;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.Orientation;
import com.diego.hanbackbattleship.model.Ship;
import com.diego.hanbackbattleship.model.ShipType;

import java.util.HashMap;

public class OceanPrinter {

    private static final int HORIZONTAL_MARGIN = 15;

    private static final int BLINKING_DURATION = 700;
    private static final int MOVEMENT_DURATION = 600;

    private HashMap<ShipType, int[]> shipInitialCoordinates;

    private View currentBlinkingView;
    private boolean blinkWasStarted;

    private Context context;
    private Ocean ocean;
    private int oceanSize; // HEIGHT = WIDTH

    private int sizeOfCell;

    private ImageView[][] ivCells;
    private ImageView[] ivBattleship;
    private ImageView[] ivCarrier;
    private ImageView[] ivCruiser;
    private ImageView[] ivSubmarine;

    private Drawable[] battleship = new Drawable[4];
    private Drawable[] carrier = new Drawable[5];
    private Drawable[] cruiser = new Drawable[3];
    private Drawable[] submarine = new Drawable[3];
    private Drawable sea;

    private LinearLayout oceanLayout;
    private LinearLayout battleshipLayout;
    private LinearLayout carrierLayout;
    private LinearLayout cruiserLayout;
    private LinearLayout submarineLayout;

    public OceanPrinter(Context context, Ocean ocean) {
        this.context = context;
        this.ocean = ocean;
        this.oceanSize = Ocean.getHeight();
        initializeResources();
    }

    private void initializeResources() {
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

        sea = context.getResources().getDrawable(R.drawable.ic_sea);

        sizeOfCell = Math.round((float) context.getResources().getDisplayMetrics().widthPixels / oceanSize) - HORIZONTAL_MARGIN;

        oceanLayout = (LinearLayout) ((Activity) context).findViewById(R.id.ocean);
        battleshipLayout = (LinearLayout) ((Activity) context).findViewById(R.id.battleship);
        carrierLayout = (LinearLayout) ((Activity) context).findViewById(R.id.carrier);
        cruiserLayout = (LinearLayout) ((Activity) context).findViewById(R.id.cruiser);
        submarineLayout = (LinearLayout) ((Activity) context).findViewById(R.id.submarine);

        ivCells = new ImageView[oceanSize][oceanSize];
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

    private void startBlinking(View view) {
        if (view != null && !blinkWasStarted) {
            Animation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(BLINKING_DURATION);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            view.startAnimation(anim);
            blinkWasStarted = true;
        }
        currentBlinkingView = view;
    }

    private void stopBlinking(View view) {
        if (view != null && blinkWasStarted) {
            view.clearAnimation();
            blinkWasStarted = false;
        }
        currentBlinkingView = null;
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

    public void moveShipToIllegalCoords(ShipType shipType, int[] illegalCoords, Orientation orientation) {
        Player player = new Player();
        player.addShip(shipType, illegalCoords[0], illegalCoords[1], orientation);
        Ship ship = player.getOcean().getShip(shipType);
        displayShip(ship);

        LinearLayout shipLayout = getShipLinearLayout(shipType);
        setColorFilter(shipLayout);
        startBlinking(shipLayout);
    }

    public void printOcean(ShipType shipType) {
        printOcean();
        if (currentBlinkingView != null && !currentBlinkingView.equals(getShipLinearLayout(shipType))) {
            stopBlinking(currentBlinkingView);
        }
        startBlinking(getShipLinearLayout(shipType));
    }

    public void printOcean() {
        if(oceanLayout.getChildCount() > 0) {
            oceanLayout.removeAllViews();
        }

        LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
        LinearLayout.LayoutParams oceanRowParams = new LinearLayout.LayoutParams(sizeOfCell * oceanSize, sizeOfCell);

        for (int i = 0; i < oceanSize; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < oceanSize; j++) {
                ivCells[i][j] = new ImageView(context);
                ivCells[i][j].setBackground(sea);
                linRow.addView(ivCells[i][j], cellParams);
            }
            oceanLayout.addView(linRow, oceanRowParams);
        }
        for (Ship ship : ocean.getShips()) {
            LinearLayout shipLayout = getShipLinearLayout(ship.getType());
            displayShip(ship);
            removeAllColorFilters(shipLayout);
        }
    }

    private void displayShip(Ship ship) {
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
                int[] coordsOnScreen = calculateRelativeCoordinatesOnScreen(ship, oceanLayout);
                LinearLayout.LayoutParams shipParams = new LinearLayout.LayoutParams(sizeOfCell * shipDrawable.length, sizeOfCell);
                shipLayout.setPivotX(coordsOnScreen[0]);
                shipLayout.setPivotY(coordsOnScreen[1] + sizeOfCell);
                shipParams.setMargins(coordsOnScreen[0], coordsOnScreen[1], 0, 0);
                shipLayout.addView(linShip, shipParams);
                shipInitialCoordinates.put(ship.getType(), ship.getCells().get(0).getCoordinates());
            } else {
                float rotation = ship.getOrientation().equals(Orientation.HORIZONTAL) ? 0f : 90f;
                shipLayout.animate().rotation(rotation);
                float[] xyValues = calculateXYAnimation(oceanLayout, ship.getCells().get(0).getCoordinates(),
                                                            shipInitialCoordinates.get(ship.getType()), ship.getOrientation());
                shipLayout.animate().x(xyValues[0]).y(xyValues[1]).setDuration(MOVEMENT_DURATION);
            }
        }
    }

    private int[] calculateRelativeCoordinatesOnScreen(Ship ship, View view) {
        int topOffset = 38;
        int[] cordsOnScreen = new int[2];
        int[] coordsStartCell = ship.getCells().get(0).getCoordinates();
        int[] viewCoordsOnScreen = new int[2];
        view.getLocationOnScreen(viewCoordsOnScreen);
        cordsOnScreen[0] = viewCoordsOnScreen[0] + coordsStartCell[1] * sizeOfCell;
        cordsOnScreen[1] = viewCoordsOnScreen[1] + coordsStartCell[0] * sizeOfCell - topOffset;
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
}
