package com.bazavluk.runningline.controls.buttons;

import android.view.View;
import android.widget.Button;
import com.bazavluk.R;
import com.bazavluk.runningline.RunningTextThread;
import com.bazavluk.services.LookupService;

/**
 * Created by Baziak on 11/19/2014.
 */
public class ButtonsController {
    public ButtonsController(Button btnStart, Button btnStop, Button btnSpeedUp, Button btnSpeedDown) {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunningLine();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRunningLine();
            }
        });
        btnSpeedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedUpRunningLine();
            }
        });
        btnSpeedDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedDownRunningLine();
            }
        });
    }

    private void stopRunningLine() {
        LookupService.get(RunningTextThread.class).suspendThread();
    }

    private void startRunningLine() {
        LookupService.get(RunningTextThread.class).resumeThread();
    }

    private int speedUpRunningLine() {
        return LookupService.get(RunningTextThread.class).speedUp();
    }

    private int speedDownRunningLine() {
        return LookupService.get(RunningTextThread.class).speedDown();
    }
}
