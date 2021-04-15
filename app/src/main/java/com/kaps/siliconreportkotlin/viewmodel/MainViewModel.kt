package com.kaps.siliconreportkotlin.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kaps.siliconreportkotlin.model.DatabaseHelper
import com.kaps.siliconreportkotlin.model.Report
import com.kaps.siliconreportkotlin.model.ReportRepo
import com.kaps.siliconreportkotlin.model.helper.AppConstants
import com.kaps.siliconreportkotlin.view.activity.AddUpdateReportActivity

class MainViewModel(var context: Context) : ViewModel() {

    var reportRepo: ReportRepo? = null
    var allReports: LiveData<List<Report>>? = null

    fun MainViewModel() {
        DatabaseHelper.getDatabaseClient(context)
        reportRepo = ReportRepo()
        reportRepo!!.ReportRepo(context)
        fetchNotes()
    }


    fun insert(report: Report?) {
        reportRepo!!.insert(report)
    }

    fun update(report: Report?) {
        reportRepo!!.update(report)
    }

    fun delete(report: Report?) {
        reportRepo!!.delete(report)
    }

    fun deleteAllNotes() {
        reportRepo!!.deleteAllReports()
    }

    fun onAddReportButton() {
        val intent = Intent(context, AddUpdateReportActivity::class.java)
        intent.putExtra(AppConstants.Keys.FROM, AppConstants.Status.ADD)
        context.startActivity(intent)
    }

    fun fetchNotes() {
        allReports = reportRepo!!.getAllReports()
    }

}

