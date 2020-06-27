package com.diego.hanbackbattleship.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diego.hanbackbattleship.R;
import com.diego.hanbackbattleship.model.Ocean;
import com.diego.hanbackbattleship.model.OceanCell;
import com.diego.hanbackbattleship.model.ShipState;

public class OceanPrinter_copy {

    private static final int HORIZONTAL_MARGIN = 10;

    private Context context;
    private Ocean ocean;
    private int oceanSize; // HEIGHT = WIDTH

    private int sizeOfCell;

    private ImageView[][] cells;

    private Drawable[] battleShip = new Drawable[4];
    private Drawable[] carrier = new Drawable[5];
    private Drawable[] cruiser = new Drawable[3];
    private Drawable[] submarine = new Drawable[3];
    private Drawable sea;

    private LinearLayout oceanLayout;

    public OceanPrinter_copy(Context context, Ocean ocean) {
        this.context = context;
        this.ocean = ocean;
        this.oceanSize = Ocean.getHeight();
        cells = new ImageView[oceanSize][oceanSize];
        loadResources();
        sizeOfCell = Math.round((float) context.getResources().getDisplayMetrics().widthPixels / oceanSize) - HORIZONTAL_MARGIN;
        oceanLayout = (LinearLayout) ((Activity) context).findViewById(R.id.ocean);
    }

    private void loadResources() {
        battleShip[0] = context.getResources().getDrawable(R.drawable.ic_battleship_1);
        battleShip[1] = context.getResources().getDrawable(R.drawable.ic_battleship_2);
        battleShip[2] = context.getResources().getDrawable(R.drawable.ic_battleship_3);
        battleShip[3] = context.getResources().getDrawable(R.drawable.ic_battleship_4);

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
    }

    public void printOcean() {
        if(oceanLayout.getChildCount() > 0) {
            oceanLayout.removeAllViews();
        }

        LinearLayout.LayoutParams row = new LinearLayout.LayoutParams(sizeOfCell * oceanSize, sizeOfCell);
        LinearLayout.LayoutParams cell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);

        for (int i = 0; i < oceanSize; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < oceanSize; j++) {
                cells[i][j] = new ImageView(context);
                OceanCell oceanCell = ocean.getAllCells()[i][j];
                if (oceanCell.getShipStateInCell() == ShipState.NO_SHIP) {
                    cells[i][j].setBackground(sea);
                } else {
                    switch (oceanCell.getShip().getType()) {
                        case BATTLESHIP:
                            cells[i][j].setBackground(battleShip[oceanCell.getShipSlice()]);
                            break;
                        case CARRIER:
                            cells[i][j].setBackground(carrier[oceanCell.getShipSlice()]);
                            break;
                        case CRUISER:
                            cells[i][j].setBackground(cruiser[oceanCell.getShipSlice()]);
                            break;
                        case SUBMARINE:
                            cells[i][j].setBackground(submarine[oceanCell.getShipSlice()]);
                            break;
                    }
                }
                linRow.addView(cells[i][j], cell);
            }
            oceanLayout.addView(linRow, row);
        }
    }
}
