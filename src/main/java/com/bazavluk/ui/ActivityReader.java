package com.bazavluk.ui;

import android.app.Activity;
import android.os.Bundle;
import com.bazavluk.R;
import com.bazavluk.configuration.Configuration;
import com.bazavluk.configuration.ConfigurationHardcode;
import com.bazavluk.datasource.Book;
import com.bazavluk.runningline.controls.speed.SpeedRegulator;
import com.bazavluk.runningline.controls.ControlsManager;
import com.bazavluk.runningline.RunningText;
import com.bazavluk.datasource.BookFromAssetsFile;
import com.bazavluk.util.LS;

public class ActivityReader extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // register main activity
        LS.register(this);

        // register datasource
        LS.register(
                new BookFromAssetsFile(),
                Book.class);

        // register configuration
        LS.register(
                new ConfigurationHardcode(),
                Configuration.class);

        // register running line representation
        LS.register(new RunningText());

        // register running line controls manager (to be fulfilled later)
        LS.register(new ControlsManager());

        // move on creating UI (FragmentRunningLine)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
    }

}

