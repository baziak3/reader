package com.bazavluk.runningline;

/**
* Describes delays' coefficients
*/
enum Delays {
    NEW_LINE(2),
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
