package com.kaps.siliconreportkotlin.view.adapter

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaps.siliconreportkotlin.R
import com.kaps.siliconreportkotlin.model.callback.DeleteImageCallback
import com.kaps.siliconreportkotlin.model.helper.AppConstants

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    var context: Context? = null
    var uriList: List<Uri>? = null
    var deleteImageCallback: DeleteImageCallback? = null

    constructor(
        context: Context?,
        uriList: List<Uri>?,
        deleteImageCallback: DeleteImageCallback?
    ) {
        this.context = context
        this.uriList = uriList
        this.deleteImageCallback = deleteImageCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_adapter_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return uriList!!.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = uriList!![position]
        holder.imageViewReport.setImageURI(uri)
        AppConstants.PrintLog("ImageAdapter","URL : "+uri)
        if (deleteImageCallback != null) {
            holder.imageViewDelete.visibility = View.VISIBLE
        }
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
            val textViewTitle =
                dialogDelete.findViewById<TextView>(R.id.textViewTitle)
            val textViewLabel =
                dialogDelete.findViewById<TextView>(R.id.textViewLabel)
            val textViewCancel =
                dialogDelete.findViewById<TextView>(R.id.textViewCancel)
            val textViewSubmit =
                dialogDelete.findViewById<TextView>(R.id.textViewSubmit)
            textViewTitle.text = context!!.getString(R.string.delete_image)
            textViewLabel.text =
                context!!.getString(R.string.are_you_sure_you_want_to_delete_this_image)
            textViewCancel.setOnClickListener { dialogDelete.cancel() }
            textViewSubmit.setOnClickListener {
                dialogDelete.cancel()
                deleteImageCallback!!.deleteImage(position)
            }
            dialogDelete.show()
        }

    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewReport: ImageView = itemView.findViewById(R.id.imageViewReport)
        var imageViewDelete: ImageView = itemView.findViewById(R.id.imageViewDelete)
    }

}