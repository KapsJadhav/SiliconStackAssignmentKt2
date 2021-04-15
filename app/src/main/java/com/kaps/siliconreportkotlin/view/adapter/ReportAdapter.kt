package com.kaps.siliconreportkotlin.view.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaps.siliconreportkotlin.R
import com.kaps.siliconreportkotlin.model.Report
import com.kaps.siliconreportkotlin.model.callback.DeleteReportCallback
import com.kaps.siliconreportkotlin.model.helper.AppConstants
import com.kaps.siliconreportkotlin.view.activity.AddUpdateReportActivity
import java.util.*

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    var context: Context? = null
    var reportList: List<Report>? = null
    var deleteReportCallback: DeleteReportCallback? = null

    constructor(
        context: Context?,
        reportList: List<Report>?,
        deleteReportCallback: DeleteReportCallback?
    ) {
        this.context = context
        this.reportList = reportList
        this.deleteReportCallback = deleteReportCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_adapter_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reportList!!.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        var report = reportList!!.get(position)
        holder.textViewTitle.setText(report!!.report)
        holder.textViewTime.setText(report!!.created_at)

        val uriList: MutableList<Uri> =
            ArrayList()
        AppConstants.PrintLog("ReportAdapter", "Path :- " + report.imagePaths)
        if (!report.imagePaths.equals("")) {
            if (report.imagePaths.contains(",")) {
                val imageUri = report.imagePaths.split(",").toTypedArray()
                for (i in imageUri.indices) {
                    val sPath: String = imageUri[i]
                    val uri = Uri.parse(sPath)
                    uriList.add(uri)
                    AppConstants.PrintLog("ReportAdapter", "Path : $sPath")
                }
                val imageAdapter = ImageAdapter(context, uriList, null)
                holder.recyclerViewReportImages.adapter = imageAdapter
            } else {
                val uri = Uri.parse(report.imagePaths)
                uriList.add(uri)
                val imageAdapter = ImageAdapter(context, uriList, null)
                holder.recyclerViewReportImages.adapter = imageAdapter
            }
        }


        holder.imageViewEdit.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, AddUpdateReportActivity::class.java)
            intent.putExtra(AppConstants.Keys.FROM, AppConstants.Status.UPDATE)
            intent.putExtra(AppConstants.Keys.REPORT, report)
            context!!.startActivity(intent)
        })

        holder.imageViewDelete.setOnClickListener {
            val dialogDelete =
                Dialog(context!!, android.R.style.Theme_Translucent)
            dialogDelete.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialogDelete.window!!.setGravity(Gravity.CENTER)
            dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogDelete.setCancelable(false)
            dialogDelete.setContentView(R.layout.layout_dialog_alert)
            val textViewCancel =
                dialogDelete.findViewById<TextView>(R.id.textViewCancel)
            val textViewSubmit =
                dialogDelete.findViewById<TextView>(R.id.textViewSubmit)
            textViewCancel.setOnClickListener { dialogDelete.cancel() }
            textViewSubmit.setOnClickListener {
                dialogDelete.cancel()
                deleteReportCallback!!.deleteReport(report)
            }
            dialogDelete.show()
        }
    }

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        var imageViewEdit: ImageView = itemView.findViewById(R.id.imageViewEdit)
        var imageViewDelete: ImageView = itemView.findViewById(R.id.imageViewDelete)
        var recyclerViewReportImages: RecyclerView =
            itemView.findViewById(R.id.recyclerViewReportImages)
    }
}