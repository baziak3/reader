package com.bazavluk.runningline;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bazavluk.R;
import com.bazavluk.domain.Lines;
import com.bazavluk.services.LookupService;
import com.bazavluk.ui.ActivityReader;

public class RunningTextThread extends Thread {
//    int contentLeftOffset;

    Activity mainActivity = LookupService.get(ActivityReader.class);
    Lines book = LookupService.get(Lines.class);
//    private LinearLayout.LayoutParams runningLineContentLayoutParams = new LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//    TextView speedTextView = (TextView) mainActivity.findViewById(R.id.speed);
//    TextView wordsPerMinuteTextView = (TextView) mainActivity.findViewById(R.id.words_per_minute);
//
//    private boolean suspended = true;
//    private boolean stopped = false;

    ViewGroup runningLineContent = (ViewGroup) mainActivity.findViewById(R.id.running_line_content);
    LinearLayout.LayoutParams runningLineContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private TextViewWrapper prevLineTextView;
    private TextViewWrapper currentLineTextView;
    private TextViewWrapper nextLineTextView;

//    // movement speed variables and constants
//    private static final int MAX_DELAY = 800;
//    private static final int MIN_DELAY = 50;
    private static final int MAX_SPEED = 50;
//    private static final double SHARPNESS = 1.1;
//    private int realMaxSpeed = (int) Math.pow(SHARPNESS, MAX_SPEED);
//    private float delayStep = ((float) MAX_DELAY - MIN_DELAY) / realMaxSpeed;
    private int speed = 0;//MAX_SPEED / 2;
//    private int lineHeight;
//
//    private Directions direction = Directions.FORWARD;
//
//    // count speed of reading as words per minutes
//    private ReadingSpeedObserver readingSpeedObserver = new ReadingSpeedObserver();

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

        do {

            updateUiSynchronously(new Runnable() {
                @Override
                public void run() {

                    prevLineTextView.setWords(book.getPreviousLineWords());
                    currentLineTextView.setWords(book.getCurrentLineWords());
                    nextLineTextView.setWords(book.getNextLineWords());

                    prevLineTextView.updatePosition(0, 0);
                    currentLineTextView.updatePosition(prevLineTextView.getWidth(), getLineHeight());
                    nextLineTextView.updatePosition(prevLineTextView.getWidth() + currentLineTextView.getWidth(), getLineHeight() * 2);

                    runningLineContentLayoutParams.setMargins(
                            getLineWidth() / 2 - prevLineTextView.getWidth() - currentLineTextView.getWordOffset(book.getCurrentWord()),
                            0,
                            0,
                            0);
                    runningLineContent.setLayoutParams(runningLineContentLayoutParams);

                }
            });
            book.increaseCurrentWord();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } while (true);
    }

    private int getLineHeight() {
        return 24;
    }

    private int getLineWidth() {
        return 480;
    }

//    private void initialize() {
//        prevLine = new SingleLine(runningLineContent.getContext(), "");
//        runningLineContent.addView(prevLine.getTextView());
//
//        mainLine = new SingleLine(runningLineContent.getContext(), book.getNextLine());
//        runningLineContent.addView(mainLine.getTextView());
//
//        initializeLineHeight(mainLine.getTextView().getLineHeight());
//
//        // TODO something
//        contentLeftOffset = getLineWidth() / 2;
//
//        // TODO something
//        runningLineContentLayoutParams.height = getLineHeight() * 2;
//        runningLineContent.setLayoutParams(runningLineContentLayoutParams);
//
//        updateUiSynchronously(new Runnable() {
//            @Override
//            public void run() {
//                prevLine.setYPosition(0);
//                mainLine.setYPosition(getLineHeight());
//                updateRunningLinePosition();
//            }
//        });
//
//        readingSpeedObserver.initialize();
//    }
//
//    private void mainLoop() {
//        do {
//            WordMeta wm;
//            do {
//                final WordMeta temporaryWm;
//                if (speed > 0) {
//                    temporaryWm = mainLine.getNextWordMeta();
//                } else if (speed < 0) {
//                    temporaryWm = mainLine.getPreviousWordMeta();
//                } else {
//                    wm = null;
//                    break;
//                }
//                updateUiSynchronously(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateRunningLineContent(temporaryWm);
//                        updateRunningLinePosition();
//                    }
//                });
//                if (temporaryWm != null) {
//                    wm = temporaryWm;
//                    break;
//                }
//            } while (true); // while speed != 0 and temporaryWm == null
//
//            if (speed > 0) {
//                // update reading speed information
//                final Float wps = readingSpeedObserver.countWord();
//                if (wps != null) {
//                    mainActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            wordsPerMinuteTextView.setText(String.valueOf(wps));
//                        }
//                    });
//                }
//            }
//
//            if (wm != null) {
//                // wait for calculated time
//                try {
//                    int suspendTimes;
//                    synchronized (this) {
//                        suspendTimes = (int) Math.pow(SHARPNESS, MAX_SPEED - Math.abs(speed) + 1);
//                    }
//                    int delay = (int) ((MIN_DELAY + suspendTimes * delayStep) * wm.getDelay().coefficient());
//                    Thread.sleep(delay);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//
//            // wait while suspended
//            try {
//                if (suspended || speed == 0) {
//                    readingSpeedObserver.goingToSuspend();
//                    synchronized (this) {
//                        while (suspended || speed == 0) {
//                            wait();
//                        }
//                    }
//                    readingSpeedObserver.resumed();
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//
//            // break if stopped
//            synchronized (this) {
//                if (stopped) {
//                    break;
//                }
//            }
//
//        } while (true);
//    }
//
//    private void initializeLineHeight(int lineHeight) {
//        this.lineHeight = lineHeight;
//    }
//
//    private int getLineHeight() {
//        return lineHeight;
//    }
//
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
//
//    private void updateRunningLineContent(WordMeta wm) {
//
//        if (wm == null) {
//
//            if (speed > 0) {
//
//                // it is time to move next line to the main position
//
//                runningLineContent.removeView(prevLine.getTextView());
//                runningLineContent.removeView(mainLine.getTextView());
//                if (nextLine != null) {
//                    runningLineContent.removeView(nextLine.getTextView());
//                }
//
//                contentLeftOffset = contentLeftOffset + prevLine.getWidth();
//
//                prevLine = mainLine;
//                prevLine.setXPosition(0);
//
//                mainLine = nextLine;
//                mainLine.setXPosition(prevLine.getWidth());
//
//                nextLine = null;
//
//                runningLineContent.addView(prevLine.getTextView());
//                runningLineContent.addView(mainLine.getTextView());
//
//                prevLine.shadowInactiveWords();
//
//                prevLine.setYPosition(0);
//                mainLine.setYPosition(getLineHeight());
//
//                mainLine.shadowInactiveWords();
//
//            } else {
//
//                // it is time to move previous line to the main position
//                // contentLeftOffset = contentLeftOffset -
//
//            }
//
//        } else {
//
//            contentLeftOffset = getLineWidth() / 2 - prevLine.getWidth() - wm.getCoordinateOffset();
//            mainLine.shadowInactiveWords();
//
//        }
//
//        if (nextLine == null && mainLine.wordsLeft() < 2) {
//
//            // it is time to create next line
//
//            nextLine = new SingleLine(
//                    runningLineContent.getContext(),
//                    book.getNextLine(),
//                    mainLine.getXPosition() + mainLine.getWidth(),
//                    getLineHeight() * 2);
//            runningLineContent.addView(nextLine.getTextView());
//        }
//
//    }
//
//    private void updateRunningLinePosition() {
//        runningLineContentLayoutParams.setMargins(contentLeftOffset, 0, 0, 0);
//        runningLineContent.setLayoutParams(runningLineContentLayoutParams);
//    }
//
    public void suspendThread() {
//        suspended = true;
    }
//
    public void resumeThread() {
//        synchronized (this) {
//            suspended = false;
//            notify();
//        }
    }
//
//    public synchronized void stopThread() {
//        stopped = true;
//    }
//
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
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                speedTextView.setText(String.valueOf(RunningTextThread.this.speed));
//            }
//        });
//        synchronized (this) {
//            notify();
//        }
    }
//
//    private enum Directions {
//        FORWARD,
//        BACKWARD
//    }
}
