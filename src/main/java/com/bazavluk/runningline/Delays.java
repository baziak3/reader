package com.bazavluk.runningline;

/**
* Created by Папа on 29.09.14.
*/
enum Delays {
    NO(1),
    COMMA(1.5),
    DOT(2),
    DASH(1.5),
    COLON(1.5),
    SEMI_COLON(1.5),
    QUESTION(2),
    EXCLAMATION(2);

    private double coefficient;

    private Delays(double value) {
        coefficient = value;
    }

    public double coefficient() {
        return coefficient;
    }
}
