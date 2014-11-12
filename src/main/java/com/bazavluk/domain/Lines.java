package com.bazavluk.domain;

import java.util.List;

/**
 * Created by Baziak on 11/6/2014.
 */
public interface Lines {
    List<String> getPreviousLineWords();
    List<String> getCurrentLineWords();
    List<String> getNextLineWords();
    int getCurrentWord();
    void increaseCurrentWord();
}
