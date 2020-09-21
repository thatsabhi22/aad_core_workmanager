package com.theleafapps.pro.workmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putInt("number", 15)
                .build();

        // Constraints for the background task to be done
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.METERED)
                .setRequiresCharging(true)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                // setInitialDelay method delays the task scheduling by the time specified in the
                // parameters 1st - Amount 2nd - unit of time
                // below example delays the task scheduling with 4 minutes
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("download")
                .build();

        WorkManager.getInstance(this).enqueue(request);
    }
}