package com.example.bugtracker

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class IssueFormViewModel(
    private val savedState: SavedStateHandle
) : ViewModel() {
    var draftTitle: String
        get() = savedState["title"] ?: ""
        set(value) { savedState["title"] = value }

    var draftDescription: String
        get() = savedState["description"] ?: ""
        set(value) { savedState["description"] = value }
}

class SyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // In a full app, the repository sync would be invoked here.
        return Result.success()
    }
}

fun scheduleSync(context: Context) {
    val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .build()
    WorkManager.getInstance(context).enqueue(syncRequest)
}