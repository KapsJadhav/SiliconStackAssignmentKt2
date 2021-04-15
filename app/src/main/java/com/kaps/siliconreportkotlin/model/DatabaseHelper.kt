package com.kaps.siliconreportkotlin.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Database(entities = [Report::class], version = 1)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun noteDao(): ReportDao

    companion object {
        @Volatile
        var instance: DatabaseHelper? = null

        fun getDatabaseClient(context: Context): DatabaseHelper {

            if (instance != null) return instance!!

            synchronized(this) {
                instance = Room
                    .databaseBuilder(
                        context,
                        DatabaseHelper::class.java,
                        "SiliconStackReportKotlin"
                    ).addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    })
                    .build()
                return instance!!
            }
        }
    }


}