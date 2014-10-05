package com.bazavluk.ui;

import android.app.Activity;
import android.os.Bundle;
import com.bazavluk.R;
import com.bazavluk.runningline.JoystickThread;
import com.bazavluk.runningline.RunningLineThread;
import com.bazavluk.domain.Book;
import com.bazavluk.services.LookupService;

public class ActivityReader extends Activity {

    private static String TAG = "activity_reader";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LookupService.register(this);
        LookupService.register(new Book(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        RunningLineThread runningLineThread = new RunningLineThread();
        LookupService.register(runningLineThread);
        runningLineThread.start();

        JoystickThread joystickThread = new JoystickThread();
        LookupService.register(joystickThread);
        joystickThread.start();
    }

}

