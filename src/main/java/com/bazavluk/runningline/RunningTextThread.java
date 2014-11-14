package com.bazavluk.runningline;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bazavluk.R;
import com.bazavluk.configuration.Configuration;
import com.bazavluk.domain.Book;
import com.bazavluk.services.LookupService;
import com.bazavluk.ui.ActivityReader;
import com.bazavluk.util.Mutable;

public class RunningTextThread extends Thread {

    private Activity mainActivity = LookupService.get(ActivityReader.class);
    private Book book = LookupService.get(Book.class);
    private Configuration configuration = LookupService.get(Configuration.class);

    private TextView speedTextView = (TextView) mainActivity.findViewById(R.id.speed);
    private TextView wordsPerMinuteTextView = (TextView) mainActivity.findViewById(R.id.words_per_minute);

    private boolean suspended = true;
    private boolean stopped = false;

    private ViewGroup runningLineContent = (ViewGroup) mainActivity.findViewById(R.id.running_line_content);
    private LinearLayout.LayoutParams runningLineContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private TextViewWrapper prevLineTextView;
    private TextViewWrapper currentLineTextView;
    private TextViewWrapper nextLineTextView;

    // movement speed variables and constants
    private static final int MAX_DELAY = 800;
    private static final int MIN_DELAY = 50;
    private static final int MAX_SPEED = 50;
    private static final double SHARPNESS = 1.1;
    private int realMaxSpeed = (int) Math.pow(SHARPNESS, MAX_SPEED);
    private float delayStep = ((float) MAX_DELAY - MIN_DELAY) / realMaxSpeed;
    private int speed = 0;//MAX_SPEED / 2;

    // count speed of reading as words per minutes
    private ReadingSpeedObserver readingSpeedObserver = new ReadingSpeedObserver();

    private final Object synchronizeUI = new Object();

    public void run() {
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

        readingSpeedObserver.initialize();

        final Mutable<Boolean> initialized = new Mutable<>(false);
        do {

            updateUiSynchronously(new Runnable() {
                @Override
                public void run() {

                    if (!initialized.get()
                            || speed > 0 && book.isFirstWordInLine()
                            || speed < 0 && book.isLastWordInLine()) {

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

            // wait for calculated time
            try {
                int suspendTimes;
                synchronized (this) {
                    suspendTimes = (int) Math.pow(SHARPNESS, MAX_SPEED - Math.abs(speed) + 1);
                }
                int delay = (int) (
                        (MIN_DELAY + suspendTimes * delayStep)
                        * currentLineTextView.getWordDelay(book.getCurrentWord()).coefficient()
                ) ;
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (speed > 0) {
                // update reading speed information
                final Float wps = readingSpeedObserver.countWord();
                if (wps != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wordsPerMinuteTextView.setText(String.valueOf(wps));
                        }
                    });
                }
            }

            // wait while suspended
            try {
                if (suspended || speed == 0) {
                    readingSpeedObserver.goingToSuspend();
                    synchronized (this) {
                        while (suspended || speed == 0) {
                            wait();
                        }
                    }
                    readingSpeedObserver.resumed();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // break if stopped
            synchronized (this) {
                if (stopped) {
                    break;
                }
            }

            if (speed > 0) {
                if (!book.increaseCurrentWord()) {
                    suspendThread();
                };
            } else {
                if (!book.decreaseCurrentWord()) {
                    suspendThread();
                }
            }

        } while (true);
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

    public void suspendThread() {
        suspended = true;
    }

    public void resumeThread() {
        synchronized (this) {
            suspended = false;
            notify();
        }
    }

    public synchronized void stopThread() {
        stopped = true;
    }

    public synchronized int speedUp() {
        speed++;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        updateSpeed();
        return speed;
    }

    public synchronized int speedDown() {
        speed--;
        if (speed < -MAX_SPEED) {
            speed = -MAX_SPEED;
        }
        updateSpeed();
        return speed;
    }

    public void updateSpeed() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                speedTextView.setText(String.valueOf(RunningTextThread.this.speed));
            }
        });
        synchronized (this) {
            notify();
        }
    }
}
