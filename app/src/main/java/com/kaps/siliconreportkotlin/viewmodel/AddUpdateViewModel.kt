package com.kaps.siliconreportkotlin.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kaps.siliconreportkotlin.model.DatabaseHelper
import com.kaps.siliconreportkotlin.model.Report
import com.kaps.siliconreportkotlin.model.ReportRepo
import com.kaps.siliconreportkotlin.model.helper.AppConstants
import com.kaps.siliconreportkotlin.view.activity.AddUpdateReportActivity
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateViewModel(var context: Context) : ViewModel() {

    var PICK_IMAGES = 111
    var CAPTURE_IMAGES = 222
    var sReport: String = ""
    var sImagePaths = ""
    var report: Report? = null
    var isUpdate = false
    var reportRepo: ReportRepo? = null
    var allReports: LiveData<List<Report>>? = null


    fun AddUpdateViewModel() {
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

    fun fetchNotes() {
        allReports = reportRepo!!.getAllReports()
    }


    fun afterReportTextChanged(s: CharSequence) {
        sReport = s.toString()
    }

    fun onClose() {
        val activity: AddUpdateReportActivity = context as AddUpdateReportActivity
        activity.finish()
    }


    fun onCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        (context as Activity).startActivityForResult(intent, CAPTURE_IMAGES)
    }

    fun onGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        (context as Activity).startActivityForResult(intent, PICK_IMAGES)
    }

    fun onAddReport() {
        if (sReport.trim { it <= ' ' } == "") {
            AppConstants.showToastMessage(context, "Please Enter Report Details")
        } else {
            if (!isUpdate) {
                val calendar = Calendar.getInstance()
                val simpleDateFormat =
                    SimpleDateFormat(AppConstants.Formats.USER_DATE_TIME_FORMAT)
                val sCreatedAt = simpleDateFormat.format(calendar.time)
                val report = Report()
                report.report = sReport
                report.imagePaths = sImagePaths
                report.created_at = sCreatedAt
                insert(report)
                fetchNotes()
                AppConstants.showToastMessage(context, "Report Added Successfully")
                (context as Activity).finish()
            } else {
                val calendar = Calendar.getInstance()
                val simpleDateFormat =
                    SimpleDateFormat(AppConstants.Formats.USER_DATE_TIME_FORMAT)
                val sCreatedAt = simpleDateFormat.format(calendar.time)
                report!!.report = sReport
                report!!.imagePaths = sImagePaths
                report!!.created_at = sCreatedAt
                update(report)
                AppConstants.showToastMessage(context, "Report Updated Successfully")
                fetchNotes()
                (context as Activity).finish()
            }
        }
    }
}