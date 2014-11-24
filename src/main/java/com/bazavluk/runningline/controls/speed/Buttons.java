package com.bazavluk.runningline.controls.speed;

import android.view.View;
import android.widget.Button;

public class Buttons {
    SpeedRegulator speedRegulator;

    public Buttons(SpeedRegulator speedRegulator, Button btnStart, Button btnStop, Button btnSpeedUp, Button btnSpeedDown) {
        this.speedRegulator = speedRegulator;

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.this.speedRegulator.startRunningLine();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.this.speedRegulator.stopRunningLine();
            }
        });
        btnSpeedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.this.speedRegulator.speedUpRunningLine();
            }
        });
        btnSpeedDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.this.speedRegulator.speedDownRunningLine();
            }
        });
    }
}
