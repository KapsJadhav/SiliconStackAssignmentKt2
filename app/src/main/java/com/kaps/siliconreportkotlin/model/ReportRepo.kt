package com.kaps.siliconreportkotlin.model

import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReportRepo {

    var reportDao: ReportDao? = null
    private var allReports: LiveData<List<Report>>? = null
    var instance: DatabaseHelper? = null;

    fun ReportRepo(context: Context) {
        instance = DatabaseHelper.getDatabaseClient(context)
        reportDao = instance!!.noteDao()
        allReports = reportDao!!.getAllReports()
    }

    fun insert(report: Report?) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            reportDao!!.Insert(report)
        })
    }

    fun update(report: Report?) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            reportDao!!.Update(report)
        })
    }

    fun delete(report: Report?) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            reportDao!!.Delete(report)
        })
    }

    fun deleteAllReports() {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            reportDao!!.DeleteAllReports()
        })
    }


    fun getAllReports(): LiveData<List<Report>>? {
        return allReports
    }

}