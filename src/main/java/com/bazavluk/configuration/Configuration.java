package com.bazavluk.configuration;

/**
 * Created by Baziak on 11/13/2014.
 */
public interface Configuration {
    String getPreviousLineColor();
    String getCurrentLineColor();
    String getCurrentWordColor();
    String getNextLineColor();
    int getLineHeight();
    int getLineWidth();
}
