package com.bazavluk.runningline.controls.joystick;

import com.bazavluk.runningline.RunningTextThread;
import com.bazavluk.services.LookupService;

/**
 * Created by Папа on 05.10.14.
 */
public class JoystickThread extends Thread {
    private boolean suspended = true;
    private boolean stopped = false;
    private int joystickY;

    public void run() {
        LookupService.get(RunningTextThread.class).updateSpeed();
        do {

            if (joystickY < 0) {
                LookupService.get(RunningTextThread.class).speedUp();
            } else if (joystickY > 0) {
                LookupService.get(RunningTextThread.class).speedDown();
            }

            // wait for calculated time
            try {
                Thread.sleep(100);
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

    public void setJoystickY(int joystickY) {
        this.joystickY = joystickY;
    }

    public int getJoystickY() {
        return joystickY;
    }
}
