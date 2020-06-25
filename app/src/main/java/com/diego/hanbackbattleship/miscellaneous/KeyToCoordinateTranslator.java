package com.diego.hanbackbattleship.miscellaneous;

public class KeyToCoordinateTranslator {
    public static int[] translate(int keyCode, KeypadQuarter quarter) {
        int sumX = 0;
        int sumY = 0;
        switch (quarter) {
            case UPPER_LEFT:
                break; // we don't need to sum
            case UPPER_RIGHT:
                sumY += 4;
                break;
            case LOWER_LEFT:
                sumX += 4;
                break;
            case LOWER_RIGHT:
                sumX += 4;
                sumY += 4;
                break;
        }

        int[] coords = {sumX, sumY};
        switch (keyCode) {
            case 7: // (0, 0)
                break;
            case 8: // (0, 1)
                coords[1] = 1 + sumY;
                break;
            case 9: // (0, 2)
                coords[1] = 2 + sumY;
                break;
            case 10: // (0, 3)
                coords[1] = 3 + sumY;
                break;
            case 11: // (1, 0)
                coords[0] = 1 + sumX;
                break;
            case 12: // (1, 1)
                coords[0] = 1 + sumX;
                coords[1] = 1 + sumY;
                break;
            case 13: // (1, 2)
                coords[0] = 1 + sumX;
                coords[1] = 2 + sumY;
                break;
            case 14: // (1, 3)
                coords[0] = 1 + sumX;
                coords[1] = 3 + sumY;
                break;
            case 15: // (2, 0)
                coords[0] = 2 + sumX;
                break;
            case 16: // (2, 1)
                coords[0] = 2 + sumX;
                coords[1] = 1 + sumY;
                break;
            case 45: // (2, 2)
                coords[0] = 2 + sumX;
                coords[1] = 2 + sumY;
                break;
            case 51: // (2, 3)
                coords[0] = 2 + sumX;
                coords[1] = 3 + sumY;
                break;
            case 33: // (3, 0)
                coords[0] = 3 + sumX;
                break;
            case 46: // (3, 1)
                coords[0] = 3 + sumX;
                coords[1] = 1 + sumY;
                break;
            case 48: // (3, 2)
                coords[0] = 3 + sumX;
                coords[1] = 2 + sumY;
                break;
            case 53: // (3, 3)
                coords[0] = 3 + sumX;
                coords[1] = 3 + sumY;
                break;
        }
        return coords;
    }
}
