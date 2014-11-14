package com.bazavluk.configuration;

/**
 * Created by Baziak on 11/13/2014.
 */
public class ConfigurationHardcode implements Configuration {
    @Override
    public String getPreviousLineColor() {
        return "#333333";
    }

    @Override
    public String getCurrentLineColor() {
        return "#444444";
    }

    @Override
    public String getCurrentWordColor() {
        return "#FFFFFF";
    }

    @Override
    public String getNextLineColor() {
        return "#333333";
    }

    @Override
    public int getLineHeight() {
        return 30;
    }

    @Override
    public int getLineWidth() {
        return 480;
    }
}
