package com.bazavluk.domain;

import java.util.List;

/**
 * Created by Baziak on 11/6/2014.
 */
public interface Book {
    List<String> getPreviousLineWords();
    List<String> getCurrentLineWords();
    List<String> getNextLineWords();
    int getCurrentWord();
    boolean increaseCurrentWord();
    boolean decreaseCurrentWord();
    boolean isFirstWordInLine();
    boolean isLastWordInLine();
}
