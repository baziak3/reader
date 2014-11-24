package com.bazavluk.runningline.controls.speed;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.bazavluk.R;
import com.bazavluk.runningline.RunningText;
import com.bazavluk.runningline.TextViewWrapper;
import com.bazavluk.runningline.controls.Control;
import com.bazavluk.ui.ActivityReader;
import com.bazavluk.util.LS;

/**
 * Speed regulator.
 */
public class SpeedRegulator implements Control {
    private SpeedRegulatorThread speedRegulatorThread;

    public SpeedRegulator(Button btnStart, Button btnStop, Button btnSpeedUp, Button btnSpeedDown, ViewGroup groupJoystick) {

        speedRegulatorThread = new SpeedRegulatorThread();

        // create buttons
        new Buttons(this, btnStart, btnStop, btnSpeedUp, btnSpeedDown);

        // create joystick
        new Joystick(this, groupJoystick);

        // create thread to control running line movement
        speedRegulatorThread.start();
    }

    void startRunningLine() {
        speedRegulatorThread.resumeThread();
    }

    void stopRunningLine() {
        speedRegulatorThread.suspendThread();
    }

    int speedUpRunningLine() {
        return speedRegulatorThread.speedUp();
    }

    int speedDownRunningLine() {
        return speedRegulatorThread.speedDown();
    }

    /**
     * Controls flow of the running line.
     */
    private static class SpeedRegulatorThread extends Thread {
        private Activity mainActivity;

        private TextView speedTextView;
    //    private TextView wordsPerMinuteTextView = (TextView) mainActivity.findViewById(R.id.words_per_minute);

        private boolean suspended = true;
        private boolean stopped = false;

        // movement speed variables and constants
        private static final int MAX_DELAY = 800;
        private static final int MIN_DELAY = 50;
        private static final int MAX_SPEED = 50;
        private static final double SHARPNESS = 1.1;
        private int realMaxSpeed = (int) Math.pow(SHARPNESS, MAX_SPEED);
        private float delayStep = ((float) MAX_DELAY - MIN_DELAY) / realMaxSpeed;
        private int speed = 0;//MAX_SPEED / 2;

        // count speed  of reading as words per minutes
    //    private ReadingSpeedObserver readingSpeedObserver = new ReadingSpeedObserver();

        public void run() {
            // readingSpeedObserver.initialize();
            mainActivity = LS.get(ActivityReader.class);
            speedTextView = (TextView) mainActivity.findViewById(R.id.speed);
            LS.get(RunningText.class).initialize();

            do {

                TextViewWrapper.WordMeta wm = null;
                if (speed > 0) {
                    wm = LS.get(RunningText.class).increaseCurrentWord();
                } else if (speed < 0) {
                    wm = LS.get(RunningText.class).decreaseCurrentWord();
                }

                LS.get(RunningText.class).update();

                if (wm == null) {

                    suspendThread();

                } else {

                    // wait for calculated time
                    try {
                        int suspendTimes;
                        synchronized (this) {
                            suspendTimes = (int) Math.pow(SHARPNESS, MAX_SPEED - Math.abs(speed) + 1);
                        }
                        int delay = (int) (
                                (MIN_DELAY + suspendTimes * delayStep)
                                        * wm.getDelay()
                        );
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                }

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

                // wait while suspended
                try {
                    if (suspended || speed == 0) {
                        // readingSpeedObserver.goingToSuspend();
                        synchronized (this) {
                            while (suspended || speed == 0) {
                                wait();
                            }
                        }
                        // readingSpeedObserver.resumed();
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

            } while (true);
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
                    speedTextView.setText(String.valueOf(SpeedRegulatorThread.this.speed));
                }
            });
            synchronized (this) {
                notify();
            }
        }

    }
}
