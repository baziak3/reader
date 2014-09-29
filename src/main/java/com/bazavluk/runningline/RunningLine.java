package com.bazavluk.runningline;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bazavluk.R;
import com.bazavluk.domain.Book;
import com.bazavluk.services.LookupService;
import com.bazavluk.ui.ActivityReader;

public class RunningLine extends Thread {
    int contentLeftOffset;

    Activity mainActivity = LookupService.get(ActivityReader.class);
    Book book = LookupService.get(Book.class);
    private LinearLayout.LayoutParams runningLineContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    ViewGroup runningLineContent = (ViewGroup) mainActivity.findViewById(R.id.running_line_content);

    private boolean suspended = true;
    private boolean stopped = false;

    private SingleLine prevLine;
    private SingleLine mainLine;
    private SingleLine nextLine;

    private static final int FPS = 25;
    private static final int MAX_DELAY = 1000;
    private static final int MIN_DELAY = 100;
    private static final int MAX_SPEED = 10;
    private static final double SHARPNESS = 2;

    private int realMaxSpeed = (int) Math.pow(SHARPNESS, MAX_SPEED);
    private float delayStep = ((float) MAX_DELAY - MIN_DELAY) / realMaxSpeed;

    private int speed = 1;
    private int lineHeight;

    public void run() {

        prevLine = new SingleLine(runningLineContent.getContext(), "");
        runningLineContent.addView(prevLine.getTextView());

        mainLine = new SingleLine(runningLineContent.getContext(), book.getNextLine());
        runningLineContent.addView(mainLine.getTextView());

        initializeLineHeight(mainLine.getTextView().getLineHeight());

        // TODO something
        contentLeftOffset = getLineWidth() / 2;

        // TODO something
        runningLineContentLayoutParams.height = getLineHeight() * 2;
        runningLineContent.setLayoutParams(runningLineContentLayoutParams);

        updateUiSynchronously(new Runnable() {
            @Override
            public void run() {
                prevLine.setYPosition(0);
                mainLine.setYPosition(getLineHeight());
            }
        });

        do {
            final WordMeta wm = mainLine.getNextWordMeta();
            updateUiSynchronously(new Runnable() {
                @Override
                public void run() {
                    updateRunningLineContent(wm);
                    updateRunningLinePosition();
                }
            });

            if (wm != null) {
                // wait for calculated time
                try {
                    int suspendTimes;
                    synchronized (this) {
                        suspendTimes = (int) Math.pow(SHARPNESS, MAX_SPEED - speed + 1);
                    }
                    int delay = (int) ((MIN_DELAY + suspendTimes * delayStep) * wm.getDelay().coefficient());
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // wait while suspended
                try {
                    synchronized (this) {
                        while (suspended) {
                            wait();
                        }
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
            }
        } while (true);
    }

    private void initializeLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    private int getLineHeight() {
        return lineHeight;
    }

    private void updateUiSynchronously(final Runnable runnable) {
        final Object synchronizeUI = new Object();
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

    private void updateRunningLineContent(WordMeta wm) {

        // is it time to move next line to the main position?
        if (wm == null) {

            runningLineContent.removeView(prevLine.getTextView());
            runningLineContent.removeView(mainLine.getTextView());
            if (nextLine != null) {
                runningLineContent.removeView(nextLine.getTextView());
            }

            contentLeftOffset = contentLeftOffset + prevLine.getWidth();

            prevLine = mainLine;
            prevLine.setXPosition(0);

            mainLine = nextLine;
            mainLine.setXPosition(prevLine.getWidth());

            nextLine = null;

            runningLineContent.addView(prevLine.getTextView());
            runningLineContent.addView(mainLine.getTextView());

            prevLine.darknessPreviousWords();

            prevLine.setYPosition(0);
            mainLine.setYPosition(getLineHeight());

        } else {

            contentLeftOffset = getLineWidth() / 2 - prevLine.getWidth() - wm.getCoordinateOffset();
            mainLine.darknessPreviousWords();

        }

        // is it time to create next line?
        if (nextLine == null && mainLine.wordsLeft() < 2) {
            nextLine = new SingleLine(
                    runningLineContent.getContext(),
                    book.getNextLine(),
                    mainLine.getXPosition() + mainLine.getWidth(),
                    getLineHeight() * 2);
            runningLineContent.addView(nextLine.getTextView());
        }

    }

    private int getLineWidth() {
        return 480;
    }

    private void updateRunningLinePosition() {
        runningLineContentLayoutParams.setMargins(contentLeftOffset, 0, 0, 0);
        runningLineContent.setLayoutParams(runningLineContentLayoutParams);
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

    public synchronized void speedUp() {
        speed++;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
    }

    public synchronized void speedDown() {
        speed--;
        if (speed < 1) {
            speed = 1;
        }
    }

}
