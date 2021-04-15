package com.kaps.siliconreportkotlin.model.callback

import com.kaps.siliconreportkotlin.model.Report

interface DeleteReportCallback {
    fun deleteReport(report: Report?)
}