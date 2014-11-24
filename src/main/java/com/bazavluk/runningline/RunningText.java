package com.bazavluk.runningline;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bazavluk.R;
import com.bazavluk.configuration.Configuration;
import com.bazavluk.datasource.Book;
import com.bazavluk.util.Initializeable;
import com.bazavluk.util.LS;
import com.bazavluk.ui.ActivityReader;
import com.bazavluk.util.Mutable;

/**
 * Creates and updates the running line representation.
 */
public class RunningText {

    private Activity mainActivity;
    private Book book;
    private Configuration configuration;

    private ViewGroup runningLineContent;
    private LinearLayout.LayoutParams runningLineContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private TextViewWrapper prevLineTextView;
    private TextViewWrapper currentLineTextView;
    private TextViewWrapper nextLineTextView;

    private final Object synchronizeUI = new Object();
    private Mutable<Boolean> initialized = new Mutable<>(false);

    public void initialize() {
        mainActivity = LS.get(ActivityReader.class);
        book = LS.get(Book.class);
        configuration = LS.get(Configuration.class);
        runningLineContent = (ViewGroup) mainActivity.findViewById(R.id.running_line_content);

        updateUiSynchronously(new Runnable() {
            @Override
            public void run() {

                runningLineContentLayoutParams.height = getLineHeight() * 3;
                runningLineContent.setLayoutParams(runningLineContentLayoutParams);

                prevLineTextView = new TextViewWrapper(runningLineContent.getContext());
                currentLineTextView = new TextViewWrapper(runningLineContent.getContext());
                nextLineTextView = new TextViewWrapper(runningLineContent.getContext());

                runningLineContent.addView(prevLineTextView.get());
                runningLineContent.addView(currentLineTextView.get());
                runningLineContent.addView(nextLineTextView.get());

            }
        });
    }

    public void update() {

        updateUiSynchronously(new Runnable() {
            @Override
            public void run() {

                if (!initialized.get()
                        || book.isFirstWordInLine()
                        || book.isLastWordInLine()) {

                    initialized.set(true);

                    prevLineTextView.setWords(book.getPreviousLineWords());
                    currentLineTextView.setWords(book.getCurrentLineWords());
                    nextLineTextView.setWords(book.getNextLineWords());

                    prevLineTextView
                            .updatePosition(0, 0)
                            .shadowPreviousLine();
                    currentLineTextView
                            .updatePosition(prevLineTextView.getWidth(), getLineHeight());
                    nextLineTextView
                            .updatePosition(prevLineTextView.getWidth() + currentLineTextView.getWidth(), getLineHeight() * 2)
                            .shadowNextLine();

                }

                currentLineTextView.shadowCurrentLine(book.getCurrentWord());

                runningLineContentLayoutParams.setMargins(
                        getLineWidth() / 2 - prevLineTextView.getWidth() - currentLineTextView.getWordOffset(book.getCurrentWord()),
                        0,
                        0,
                        0);
                runningLineContent.setLayoutParams(runningLineContentLayoutParams);

            }
        });

    }

    public TextViewWrapper.WordMeta increaseCurrentWord() {
        if (book.increaseCurrentWord()) {
            int currentWord = book.getCurrentWord();
            if (currentWord > 0) {
                return currentLineTextView.getWordMeta(currentWord);
            } else {
                return nextLineTextView.getWordMeta(currentWord);
            }
        } else {
            return null;
        }
    }

    private TextViewWrapper.WordMeta getPreviousWordMeta() {
        if (book.getCurrentWord() == 0) {
            return prevLineTextView.getWordMeta(prevLineTextView.wordsCount() - 1);
        } else {
            return currentLineTextView.getWordMeta(book.getCurrentWord() - 1);
        }
    }

    private TextViewWrapper.WordMeta getCurrentWordMeta() {
        return currentLineTextView.getWordMeta(book.getCurrentWord());
    }

    private TextViewWrapper.WordMeta getNextWordMeta() {
        if (book.getCurrentWord() == currentLineTextView.wordsCount() - 1) {
            return nextLineTextView.getWordMeta(0);
        } else {
            return currentLineTextView.getWordMeta(book.getCurrentWord() + 1);
        }
    }

    public TextViewWrapper.WordMeta decreaseCurrentWord() {
        boolean usePreviousLine = book.getCurrentWord() == 0;
        if (book.decreaseCurrentWord()) {
            int currentWord = book.getCurrentWord();
            if (usePreviousLine) {
                return prevLineTextView.getWordMeta(currentWord);
            } else {
                return currentLineTextView.getWordMeta(currentWord);
            }
        } else {
            return null;
        }
    }

    public float move(float v) {
        if (v > 0) {
            TextViewWrapper.WordMeta wm = getCurrentWordMeta();
            if (wm != null) {
                if (v < wm.getWidth()) {
                    return 0;
                } else {
                    increaseCurrentWord();
                    return wm.getWidth();
                }
            }
        } else if (v < 0) {
            TextViewWrapper.WordMeta wm = getPreviousWordMeta();
            if (wm != null) {
                if (-v < wm.getWidth()) {
                    return 0;
                } else {
                    decreaseCurrentWord();
                    return wm.getWidth();
                }
            }
        }
        return 0;
    }

    private int getLineHeight() {
        return configuration.getLineHeight();
    }

    private int getLineWidth() {
        return configuration.getLineWidth();
    }

    private void updateUiSynchronously(final Runnable runnable) {
        synchronized (synchronizeUI) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                    synchronized (synchronizeUI) {
                        synchronizeUI.notify();
                    }
                }
            });
            try {
                synchronizeUI.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
