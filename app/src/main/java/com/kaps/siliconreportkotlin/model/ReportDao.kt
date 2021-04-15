package com.kaps.siliconreportkotlin.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReportDao {

    @Insert
    fun Insert(report: Report?)

    @Update
    fun Update(report: Report?)

    @Delete
    fun Delete(report: Report?)

    @Query("DELETE FROM Report")
    fun DeleteAllReports()

    @Query("SELECT * FROM Report")
    fun getAllReports(): LiveData<List<Report>>?
}