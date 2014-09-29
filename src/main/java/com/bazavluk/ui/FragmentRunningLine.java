package com.bazavluk.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bazavluk.R;
import com.bazavluk.runningline.RunningLine;
import com.bazavluk.services.LookupService;

public class FragmentRunningLine extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_running_line, container, false);

        Button btnStart = (Button) result.findViewById(R.id.button_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunningLine();
            }
        });

        Button btnStop = (Button) result.findViewById(R.id.button_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRunningLine();
            }
        });

        Button btnSpeedUp = (Button) result.findViewById(R.id.button_speed_up);
        btnSpeedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedUpRunningLine();
            }
        });

        Button btnSpeedDown = (Button) result.findViewById(R.id.button_speed_down);
        btnSpeedDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedDownRunningLine();
            }
        });

        return result;
    }

    private void stopRunningLine() {
        LookupService.get(RunningLine.class).suspendThread();
    }

    private void startRunningLine() {
        LookupService.get(RunningLine.class).resumeThread();
    }

    private void speedUpRunningLine() {
        LookupService.get(RunningLine.class).speedUp();
    }

    private void speedDownRunningLine() {
        LookupService.get(RunningLine.class).speedDown();
    }

}
