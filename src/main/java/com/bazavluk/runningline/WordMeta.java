package com.bazavluk.runningline;

/**
* Created by Папа on 29.09.14.
*/
class WordMeta {
    private String word;
    private Delays delay;
    private int coordinateOffset;
    private int charOffset;

    public WordMeta(String word, Delays delay, int coordinateOffset, int charOffset) {
        this.word = word;
        this.delay = delay;
        this.coordinateOffset = coordinateOffset;
        this.charOffset = charOffset;
    }

    public String getWord() {
        return word;
    }

    public Delays getDelay() {
        return delay;
    }

    public int getCoordinateOffset() {
        return coordinateOffset;
    }

    public int getCharOffset() {
        return charOffset;
    }
}
