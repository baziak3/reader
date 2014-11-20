package com.bazavluk.runningline.controls.swiper;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.bazavluk.R;
import com.bazavluk.runningline.RunningTextThread;
import com.bazavluk.services.LookupService;

/**
 * Created by Baziak on 11/19/2014.
 */
public class SwiperController {

    public SwiperController(ViewGroup view) {
        view.setOnTouchListener(new SwiperController.RelativeLayoutTouchListener());
    }

    private static class RelativeLayoutTouchListener implements View.OnTouchListener {

        static final String logTag = "ActivitySwipeDetector";
        static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
        private float downX;
        private float downY;

        public void onRightToLeftSwipe() {
            LookupService.get(RunningTextThread.class).speedDown();
        }

        public void onLeftToRightSwipe() {
            LookupService.get(RunningTextThread.class).speedUp();
        }

        public void onTopToBottomSwipe() {
        }

        public void onBottomToTopSwipe() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    return true;

                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // swipe horizontal?
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    // swipe vertical?
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            this.onTopToBottomSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            this.onBottomToTopSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    return false; // no swipe horizontally and no swipe vertically
            }
            return false;
        }

    }
}
