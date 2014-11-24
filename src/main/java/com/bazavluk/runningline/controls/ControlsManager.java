package com.bazavluk.runningline.controls;

import java.util.ArrayList;
import java.util.List;

public class ControlsManager {
    private List<Control> controls = new ArrayList<>();

    public void addControl(Control control) {
        controls.add(control);
    }

//    public void initialize() {
//        for (Control control: controls) {
//            control.start();
//        }
//    }
}
