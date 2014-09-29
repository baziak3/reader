package com.bazavluk.ui;

import android.app.Activity;
import android.os.Bundle;
import com.bazavluk.R;
import com.bazavluk.runningline.RunningLine;
import com.bazavluk.domain.Book;
import com.bazavluk.services.LookupService;

public class ActivityReader extends Activity {

    private static String TAG = "activity_reader";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LookupService.register(this);
        LookupService.register(new Book());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        RunningLine rlt = new RunningLine();
        LookupService.register(rlt);
        rlt.start();

    }

}
