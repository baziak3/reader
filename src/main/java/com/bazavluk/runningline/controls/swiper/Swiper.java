package com.bazavluk.runningline.controls.swiper;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.bazavluk.runningline.RunningText;
import com.bazavluk.runningline.controls.Control;
import com.bazavluk.util.LS;

/**
 * Created by Baziak on 11/19/2014.
 */
public class Swiper implements Control {

    public Swiper(ViewGroup view) {
        view.setOnTouchListener(new SwiperListener());
    }

    private static class SwiperListener implements View.OnTouchListener {
        private float lastX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    return true;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    moveRunningLine(event.getX());
                    return true;
            }
            return false;
        }

        private void moveRunningLine(float newX) {
            float movedFor = LS.get(RunningText.class).move(newX - lastX);
            if (movedFor > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LS.get(RunningText.class).update();
                    }
                }).start();
                lastX += movedFor;
            }
        }
    }
}
