package com.bazavluk.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bazavluk.R;
import com.bazavluk.runningline.controls.ControlsManager;
import com.bazavluk.runningline.controls.speed.SpeedRegulator;
import com.bazavluk.runningline.controls.swiper.Swiper;
import com.bazavluk.util.LS;

public class FragmentRunningLine extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_running_line, container, false);

        // create and register controls into controls manager
        LS.get(ControlsManager.class).addControl(
                new Swiper(
                        (ViewGroup) result.findViewById(R.id.layout_swiper)));
        LS.get(ControlsManager.class).addControl(
                new SpeedRegulator(
                        (Button) result.findViewById(R.id.button_start),
                        (Button) result.findViewById(R.id.button_stop),
                        (Button) result.findViewById(R.id.button_speed_up),
                        (Button) result.findViewById(R.id.button_speed_down),
                        (ViewGroup) result.findViewById(R.id.layout_joystick)));

        return result;
    }

}
