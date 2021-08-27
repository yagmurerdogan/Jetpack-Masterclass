package com.yagmurerdogan.favdish.model.notification

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    
    override fun doWork(): Result {

        Log.i("Notify Worker", "doWork function is called...")

        return success()
    }
}