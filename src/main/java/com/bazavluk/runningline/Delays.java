package com.bazavluk.runningline;

/**
* Created by Папа on 29.09.14.
*/
enum Delays {
    NO(1),
    COMMA(2),
    DOT(3),
    DASH(2),
    COLON(2),
    SEMI_COLON(2),
    QUESTION(3),
    EXCLAMATION(3);

    private double coefficient;

    private Delays(double value) {
        coefficient = value;
    }

    public double coefficient() {
        return coefficient;
    }
}
