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
import com.bazavluk.runningline.controls.buttons.ButtonsController;
import com.bazavluk.runningline.controls.joystick.JoystickController;
import com.bazavluk.runningline.controls.joystick.JoystickThread;
import com.bazavluk.runningline.RunningTextThread;
import com.bazavluk.runningline.controls.swiper.SwiperController;
import com.bazavluk.services.LookupService;

public class FragmentRunningLine extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_running_line, container, false);

        new JoystickController((ViewGroup) result.findViewById(R.id.layout_joystick), R.drawable.image_button);
        new SwiperController((ViewGroup) result.findViewById(R.id.layout_swiper));
        new ButtonsController(
                (Button) result.findViewById(R.id.button_start),
                (Button) result.findViewById(R.id.button_stop),
                (Button) result.findViewById(R.id.button_speed_up),
                (Button) result.findViewById(R.id.button_speed_down));

        return result;
    }
}
