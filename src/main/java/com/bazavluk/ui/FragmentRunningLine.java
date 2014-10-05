package com.bazavluk.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bazavluk.R;
import com.bazavluk.runningline.JoystickController;
import com.bazavluk.runningline.JoystickThread;
import com.bazavluk.runningline.RunningLineThread;
import com.bazavluk.services.LookupService;

public class FragmentRunningLine extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_running_line, container, false);

        final TextView getX = (TextView) result.findViewById(R.id.get_x);
        final TextView getY = (TextView) result.findViewById(R.id.get_y);

        RelativeLayout joystickLayout = (RelativeLayout) result.findViewById(R.id.layout_joystick);
        final JoystickController joystick = new JoystickController(
                getActivity().getApplicationContext(),
                joystickLayout,
                R.drawable.image_button);
        joystick.setStickSize(20, 20);
        joystick.setLayoutSize(200, 200);
//        joystick.setLayoutAlpha(150);
//        joystick.setStickAlpha(100);
        joystick.setOffset(50);
        joystick.setMinimumDistance(50);
        joystickLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                joystick.drawStick(arg1);
                getX.setText(String.valueOf(joystick.getX()));
                getY.setText(String.valueOf(joystick.getY()));

                if (joystick.isInTouchState()) {
                    startRunningLine();
                } else {
                    stopRunningLine();
                }

                if (joystick.getY() == 0) {
                    LookupService.get(JoystickThread.class).suspendThread();
                } else {
                    LookupService.get(JoystickThread.class).setJoystickY(joystick.getY());
                    LookupService.get(JoystickThread.class).resumeThread();
                }
                return true;
            }
        });

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
        LookupService.get(RunningLineThread.class).suspendThread();
    }

    private void startRunningLine() {
        LookupService.get(RunningLineThread.class).resumeThread();
    }

    private int speedUpRunningLine() {
        return LookupService.get(RunningLineThread.class).speedUp();
    }

    private int speedDownRunningLine() {
        return LookupService.get(RunningLineThread.class).speedDown();
    }
}
