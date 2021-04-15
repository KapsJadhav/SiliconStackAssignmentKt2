package com.kaps.siliconreportkotlin.model.helper

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.kaps.siliconreportkotlin.BuildConfig
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppConstants {

    @SuppressLint("MissingPermission")
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    fun isConnectedFast(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && isConnectionFast(
            info.type,
            info.subtype
        )
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    fun isConnectionFast(type: Int, subType: Int): Boolean {
        return if (type == ConnectivityManager.TYPE_WIFI) {
            true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        } else {
            false
        }
    }

    private object ApiConfiguration {
       const val SERVER_URL = "https://freeapi.rdewan.dev/" // Production
    }


    class ApiNames {
        companion object {
          const val API_URL = ApiConfiguration!!.SERVER_URL + "api/no_auth/"
          const val GET_ALL_TASK = "get_all_task"
          const val ADD_TASK = "add_task"
          const val EDIT_TASK = "update_task"
          const val DELETE_TASK = "delete_task"
          const val SEARCH_TASK = "search/"
        }
    }

    object Status {
        var ADD = "0"
        var UPDATE = "1"
        var SUCCESS = 200
        var FAILED = 404
        var SESSION_OUT = 201
        var NOT_ACCESS = 401
    }

    object Keys {
        const val REPORT = "REPORT"
        const val FROM = "FROM"
    }

    object Formats {
        var DATE_FORMAT = "yyyy-MM-dd"
        var TIME_FORMAT = "hh:mm:ss a"
        var TIME_FORMAT_24 = "HH:mm:ss"
        var DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss a"
        var USER_DATE_TIME_FORMAT = "EEE, dd MMM yyyy | HH:mm"
        var USER_DATE_FORMAT = "dd MMM yyyy\nEEEE"
        var USER_DATE_FORMAT_SHOW = "EEE dd MMM yyyy HH:mm"
        var USER_TIME_FORMAT = "HH:mm"
        var DATE_TIME_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        var USER_DATE_TIME_FORMAT_SERVER = "yyyy-MM-dd HH:mm"
        var USER_DATE_TIME_FORMAT_SERVER_NEW = "yyyy-MM-dd HH:mm"
        fun getStringInNumberFormat(dNumber: Double): String {
            val formatter = NumberFormat.getNumberInstance()
            formatter.minimumFractionDigits = 3
            formatter.maximumFractionDigits = 3
            return formatter.format(dNumber)
        }

        fun getStringInCCNumberFormat(dNumber: Double): String {
            val formatter = NumberFormat.getNumberInstance()
            formatter.minimumFractionDigits = 5
            formatter.maximumFractionDigits = 5
            return formatter.format(dNumber)
        }

        fun getDate(sDate: String?): String {
            var simpleDateFormat =
                SimpleDateFormat(DATE_TIME_FORMAT_SERVER)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            var date: Date? = null
            var sConvertDate = ""
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
                sConvertDate = simpleDateFormat.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            return sConvertDate
        }

        fun getServerDate(sDate: String?): String {
            var simpleDateFormat =
                SimpleDateFormat(USER_DATE_FORMAT)
            var date: Date? = null
            var sConvertDate = ""
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
                sConvertDate = simpleDateFormat.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            return sConvertDate
        }

        fun getUserDate(sDate: String?): String {
            val locale = Locale("en")
            var simpleDateFormat =
                SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
            var date: Date? = null
            var sConvertDate = ""
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", locale)
                sConvertDate = simpleDateFormat.format(date)
                PrintLog("APPPCONSTANTS", "DATE : $sConvertDate")
            } catch (e1: ParseException) {
                e1.printStackTrace()
                PrintLog("APPPCONSTANTS", "DATE : " + e1.message)
            }
            return sConvertDate
        }

        fun getTime(sDate: String?): String {
            var simpleDateFormat =
                SimpleDateFormat(DATE_TIME_FORMAT_SERVER)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            var sTime = ""
            var date: Date? = null
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat(TIME_FORMAT)
                sTime = simpleDateFormat.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            return sTime
        }

        fun getDateTime(sDate: String?): String {
            var simpleDateFormat =
                SimpleDateFormat(DATE_TIME_FORMAT_SERVER)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            var date: Date? = null
            var sConvertDate = ""
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat(DATE_TIME_FORMAT)
                sConvertDate = simpleDateFormat.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            return sConvertDate
        }

        fun getUserDateTime(sDate: String?): String {
            val locale = Locale("en")
            var simpleDateFormat = SimpleDateFormat(
                USER_DATE_TIME_FORMAT_SERVER,
                Locale.ENGLISH
            )
            var date: Date? = null
            var sConvertDate = ""
            try {
                date = simpleDateFormat.parse(sDate)
                simpleDateFormat = SimpleDateFormat(USER_DATE_TIME_FORMAT, locale)
                sConvertDate = simpleDateFormat.format(date)
            } catch (e1: ParseException) {
                PrintLog("AppConstants", "Error : " + e1.message)
                e1.printStackTrace()
            }
            return sConvertDate
        }
    }


    fun getDeviceName(context: Context?): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        if (model.startsWith(manufacturer)) {
            //return capitalize(model);
            return model
        }
        Log.d("Log", "Manufacturer : $manufacturer")
        Log.d("Log", "Device Model : $model")
        //        Toast.makeText(context, "Device : " + model + manufacturer, Toast.LENGTH_SHORT).show();
        // return capitalize(manufacturer) + " " + model;
        return manufacturer
    }

    //  Get & Set Values in Shared Preferences
    object Preferences {
        // Boolean Preferences (Checkbox)
        fun getBooleanPreference(
            context: Context?,
            key: String?,
            defaultValue: Boolean
        ): Boolean {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(key, defaultValue)
        }

        fun setBooleanPreferences(
            context: Context,
            key: String?,
            value: Boolean
        ) {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val preferenceEditor = preferences.edit()
            preferenceEditor.putBoolean(key, value)
            preferenceEditor.apply()
        }

        fun getStringPreference(
            context: Context?,
            key: String?
        ): String? {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, null)
        }

        fun getRecentRatePreference(
            context: Context?,
            key: String?
        ): String? {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, "0")
        }

        fun setStringPreferences(
            context: Context,
            key: String?,
            value: String?
        ) {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val preferenceEditor = preferences.edit()
            preferenceEditor.putString(key, value)
            preferenceEditor.apply()
        }

        fun getStringImagePreference(
            context: Context?,
            key: String?
        ): String? {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, null)
        }

        fun setStringImagePreferences(
            context: Context,
            key: String?,
            value: String?
        ) {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val preferenceEditor = preferences.edit()
            preferenceEditor.putString(key, value)
            preferenceEditor.apply()
        }

        fun getIntPreference(
            context: Context?,
            key: String?,
            defValue: Int
        ): Int {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(key, defValue)
        }

        fun setIntPreferences(
            context: Context,
            key: String?,
            value: Int
        ) {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val preferenceEditor = preferences.edit()
            preferenceEditor.putInt(key, value)
            preferenceEditor.apply()
        }
    }

    //  copy wallet address
    object Copy {
        fun copyText(context: Context, sText: String?) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("WalletAddress", sText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                context, "Wallet address has been copied",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //  copy Key
    object CopyKey {
        fun copyText(context: Context, sText: String?) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Key", sText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                context,
                "Authentication key has been copied, Please paste this key in Google Authenticator app",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    object Paste {
        fun pasteText(context: Context): String {
            var pasteText = ""
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (clipboard.hasPrimaryClip() == true) {
                val item = clipboard.primaryClip!!.getItemAt(0)
                pasteText = item.text.toString()
            } else {
                Toast.makeText(context, "Nothing to Paste", Toast.LENGTH_SHORT).show()
            }
            return pasteText
        }
    }

    // Print Logs
    fun PrintLog(sFrom: String?, sMessage: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(sFrom, sMessage!!)
        }
    }

    // Print Toast
    public fun showToastMessage(
        context: Context?,
        sMessage: String
    ) {
        Toast.makeText(context, "" + sMessage, Toast.LENGTH_SHORT).show()
    }

}