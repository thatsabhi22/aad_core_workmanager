package com.theleafapps.pro.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker extends Worker {

    private static final String TAG = "SampleWorker";

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data inputData = getInputData();
        int num = inputData.getInt("number",-1);

        Log.d(TAG, "doWork: number" + num);

        for(int i = num;i>0;i--){
            Log.d(TAG, "doWork: value of i was :" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }
        return Result.success();
    }
}
