package com.bazavluk.ui;

import android.app.Activity;
import android.os.Bundle;
import com.bazavluk.R;
import com.bazavluk.configuration.Configuration;
import com.bazavluk.configuration.ConfigurationHardcode;
import com.bazavluk.domain.Book;
import com.bazavluk.runningline.JoystickThread;
import com.bazavluk.runningline.RunningTextThread;
import com.bazavluk.domain.BookFromAssetsFile;
import com.bazavluk.services.LookupService;

public class ActivityReader extends Activity {

    private static String TAG = "activity_reader";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LookupService.register(this);
        LookupService.register(
                new BookFromAssetsFile(getApplicationContext()),
                Book.class);
        LookupService.register(
                new ConfigurationHardcode(),
                Configuration.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        RunningTextThread runningTextThread = new RunningTextThread();
        LookupService.register(runningTextThread);
        runningTextThread.start();

        JoystickThread joystickThread = new JoystickThread();
        LookupService.register(joystickThread);
        joystickThread.start();
    }

}

