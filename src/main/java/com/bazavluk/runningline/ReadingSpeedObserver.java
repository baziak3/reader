package com.bazavluk.runningline;

/**
 * Created by Baziak on 11/11/2014.
 */
public class ReadingSpeedObserver {
    private int wordsSoFar = 0;
    private int millisecondsSoFar = 0;
    long startedMilliseconds;

    public Float countWord() {
        wordsSoFar++;
        long currentCurrent = System.currentTimeMillis();
        if (currentCurrent - startedMilliseconds > 3000) { // once per 3 seconds
            millisecondsSoFar += currentCurrent - startedMilliseconds;
            startedMilliseconds = currentCurrent;
            return (float) wordsSoFar * 60000 / millisecondsSoFar;
        } else {
            return null;
        }
    }

    public void initialize() {
        startedMilliseconds = System.currentTimeMillis();
    }

    public void goingToSuspend() {
        millisecondsSoFar += System.currentTimeMillis() - startedMilliseconds;
    }

    public void resumed() {
        startedMilliseconds = System.currentTimeMillis();
    }
}
