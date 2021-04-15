package com.kaps.siliconreportkotlin.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Report")
data class Report(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "report") var report: String = "",
    @ColumnInfo(name = "created_at") var created_at: String = "",
    @ColumnInfo(name = "imagePaths") var imagePaths: String = ""
): Parcelable  {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Report> {
            override fun createFromParcel(parcel: Parcel) = Report(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Report>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        report = parcel.readString().toString(),
        created_at = parcel.readString().toString(),
        imagePaths = parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(report)
        parcel.writeString(created_at)
        parcel.writeString(imagePaths)
    }

    override fun describeContents() = 0
}