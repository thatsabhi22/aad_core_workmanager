package com.theleafapps.pro.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button cancel_job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancel_job = findViewById(R.id.cancel_task_btn);

        Data data = new Data.Builder()
                .putInt("number", 15)
                .build();

        // Constraints for the background task to be done
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.METERED)
                .setRequiresCharging(true)
                .build();

        // Below creates a one time request i.e. the the job has to be done once only
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                // setInitialDelay method delays the task scheduling by the time specified in the
                // parameters 1st - Amount 2nd - unit of time
                // below example delays the task scheduling with 4 minutes
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("download")
                .build();

//        WorkManager.getInstance(this).enqueue(request);

        // Below created the Periodic request i.e. the job will run periodically in intervals specified
        final PeriodicWorkRequest pRequest = new PeriodicWorkRequest.Builder(SampleWorker.class,10,TimeUnit.SECONDS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("Periodic")
                .build();

        WorkManager.getInstance(this).enqueue(pRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(pRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d(TAG, "Observer: " + workInfo.getState());
            }
        });

        cancel_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(MainActivity.this).cancelWorkById(pRequest.getId());
            }
        });

        //Below is the chaining of the work request one after the another
        // here we can implement data exchange too
        // output data from one execution can be input data for the another work request
        // https://www.youtube.com/watch?v=-prIcKMlrYQ
        WorkManager.getInstance(this).beginWith(request)
                .then(request)
                .then(request)
                .enqueue();




    }
}