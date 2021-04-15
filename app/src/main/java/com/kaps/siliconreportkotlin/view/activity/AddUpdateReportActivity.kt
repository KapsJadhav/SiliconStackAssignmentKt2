package com.kaps.siliconreportkotlin.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kaps.siliconreportkotlin.R
import com.kaps.siliconreportkotlin.databinding.ActivityAddUpdateReportBinding
import com.kaps.siliconreportkotlin.model.Report
import com.kaps.siliconreportkotlin.model.callback.DeleteImageCallback
import com.kaps.siliconreportkotlin.model.helper.AppConstants
import com.kaps.siliconreportkotlin.view.adapter.ImageAdapter
import com.kaps.siliconreportkotlin.viewmodel.AddUpdateViewModel
import java.io.*

class AddUpdateReportActivity : AppCompatActivity() {

    var binding: ActivityAddUpdateReportBinding? = null

    private val PICK_IMAGES = 111
    private val CAPTURE_IMAGES = 222
    private val TAG = "AddUpdateReportActivity"
    private var report: Report? = null
    val pathList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_report)
        binding!!.viewModel = AddUpdateViewModel(this)
        binding!!.viewModel!!.AddUpdateViewModel()
        binding!!.executePendingBindings()

        val sFrom = intent.getStringExtra(AppConstants.Keys.FROM)
        if (sFrom == AppConstants.Status.UPDATE) {
            report = intent.getParcelableExtra(AppConstants.Keys.REPORT)
            if (!report!!.imagePaths.equals("")) {
                var sPath = ""
                if (report!!.imagePaths.contains(",")) {
                    val imageUri = report!!.imagePaths.split(",").toTypedArray()
                    for (i in imageUri.indices) {
                        val sUri = imageUri[i]
                        val uri: Uri = Uri.parse(sUri)
                        pathList.add(uri)
                        sPath += "$uri,"
                        AppConstants.PrintLog("ReportAdapter", "Path : $sUri")
                    }
                    val imageAdapter =
                        ImageAdapter(
                            this@AddUpdateReportActivity,
                            pathList,
                            object : DeleteImageCallback {
                                override fun deleteImage(position: Int) {
                                    deleteImageFromList(position)
                                }
                            })
                    binding!!.recyclerViewReportImages.adapter = imageAdapter
                } else {
                    val uri = Uri.parse(report!!.imagePaths)
                    pathList.add(uri)
                    sPath += "$uri,"
                    val imageAdapter =
                        ImageAdapter(
                            this@AddUpdateReportActivity,
                            pathList,
                            object : DeleteImageCallback {
                                override fun deleteImage(position: Int) {
                                    deleteImageFromList(position)
                                }
                            })
                    binding!!.recyclerViewReportImages.adapter = imageAdapter
                }
                if (sPath.isNotBlank()) {
                    binding!!.viewModel!!
                        .sImagePaths = sPath.substring(0, sPath.length - 1)
                    AppConstants.PrintLog(TAG, sPath.substring(0, sPath.length - 1))
                } else {
                    binding!!.viewModel!!
                        .sImagePaths = ""
                }
            }
            binding!!.viewModel!!.isUpdate = true
            binding!!.viewModel!!.report = report
            binding!!.editTextReport.setText(report!!.report)
            binding!!.buttonSubmit.setText(getString(R.string.update_report))
        }
    }

    fun deleteImageFromList(position: Int) {
        if (pathList != null && pathList.size != 0) {
            pathList.removeAt(position)
            val imageAdapter =
                ImageAdapter(
                    this@AddUpdateReportActivity,
                    pathList,
                    object : DeleteImageCallback {
                        override fun deleteImage(position: Int) {
                            deleteImageFromList(position)
                        }
                    })
            binding!!.recyclerViewReportImages.adapter = imageAdapter
            var sPath = ""
            for (i in pathList.indices) {
                sPath += pathList[i].toString() + ","
            }
            if (sPath.isNotBlank()) {
                binding!!.viewModel!!
                    .sImagePaths = sPath.substring(0, sPath.length - 1)
                AppConstants.PrintLog(TAG, sPath.substring(0, sPath.length - 1))
            } else {
                binding!!.viewModel!!
                    .sImagePaths = ""
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGES) {
                if (data!!.clipData != null) {
                    val mClipData = data.clipData
                    for (i in 0 until mClipData!!.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        val tempUri = Uri.parse(
                            getGalleryRealPathFromURI(
                                this@AddUpdateReportActivity,
                                uri
                            )
                        )
                        pathList.add(tempUri)
                        AppConstants.PrintLog(TAG, "Image Uri : $uri")
                    }
                } else if (data.data != null) {
                    val uri = data.data
                    val tempUri = Uri.parse(
                        getGalleryRealPathFromURI(
                            this@AddUpdateReportActivity,
                            uri
                        )
                    )
                    pathList.add(tempUri)
                    AppConstants.PrintLog(TAG, "Image Uri : $uri")
                    //                    imageView.setImageURI(uri);
                }
            } else if (requestCode == CAPTURE_IMAGES) {
                val photo = data!!.extras!!["data"] as Bitmap?
                val tempUri = getImageUri(applicationContext, photo)
                val finalFile = File(getRealPathFromURI(tempUri))
                pathList.add(tempUri)
            }
            val imageAdapter =
                ImageAdapter(
                    this@AddUpdateReportActivity,
                    pathList,
                    object : DeleteImageCallback {
                        override fun deleteImage(position: Int) {
                            deleteImageFromList(position)
                        }
                    })
            binding!!.recyclerViewReportImages.adapter = imageAdapter
            var sPath = ""
            for (i in pathList.indices) {
                sPath += pathList[i].toString() + ","
            }
            if (sPath.isNotBlank()) {
                binding!!.viewModel!!
                    .sImagePaths = sPath.substring(0, sPath.length - 1)
                AppConstants.PrintLog(TAG, sPath.substring(0, sPath.length - 1))
            } else {
                binding!!.viewModel!!
                    .sImagePaths = ""
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor =
                contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    fun getGalleryRealPathFromURI(
        context: Context?,
        contentUri: Uri?
    ): String? {
        val out: OutputStream
        val file = File(getFilename(context))
        try {
            if (file.createNewFile()) {
                val iStream =
                    if (context != null) context.contentResolver
                        .openInputStream(contentUri!!) else context!!.contentResolver
                        .openInputStream(contentUri!!)
                val inputData = getBytes(iStream)
                out = FileOutputStream(file)
                out.write(inputData)
                out.close()
                return file.absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream!!.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun getFilename(context: Context?): String? {
        val mediaStorageDir =
            File(context!!.getExternalFilesDir(""), "patient_data")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }
        val mImageName =
            "IMG_" + System.currentTimeMillis().toString() + ".png"
        return mediaStorageDir.absolutePath + "/" + mImageName
    }

}