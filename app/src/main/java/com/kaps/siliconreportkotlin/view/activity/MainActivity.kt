package com.kaps.siliconreportkotlin.view.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kaps.siliconreportkotlin.R
import com.kaps.siliconreportkotlin.databinding.ActivityMainBinding
import com.kaps.siliconreportkotlin.model.Report
import com.kaps.siliconreportkotlin.model.callback.DeleteReportCallback
import com.kaps.siliconreportkotlin.model.helper.AppConstants
import com.kaps.siliconreportkotlin.view.adapter.ReportAdapter
import com.kaps.siliconreportkotlin.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    var noteList: List<Report>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding!!.viewModel = MainViewModel(this)
        binding!!.executePendingBindings()
        binding!!.viewModel!!.MainViewModel()
        binding!!.viewModel!!.allReports!!.observe(
            this,
            Observer<List<Report>>() { notes: List<Report>? ->
                noteList = notes
                var reportAdapter = ReportAdapter(this, noteList, object : DeleteReportCallback {
                    override fun deleteReport(report: Report?) {
                        if (report != null) {
                            binding!!.viewModel!!.delete(report)
                            AppConstants.showToastMessage(
                                this@MainActivity,
                                "Report Deleted Successfully"
                            )
                        }
                    }
                })
                binding!!.recyclerViewReports!!.adapter = reportAdapter
                if (notes!!.size==0){
                    binding!!.recyclerViewReports!!.visibility = GONE
                    binding!!.linearLayoutNoReports!!.visibility = VISIBLE
                }else{
                    binding!!.recyclerViewReports!!.visibility = VISIBLE
                    binding!!.linearLayoutNoReports!!.visibility = GONE
                }
            });
    }
}