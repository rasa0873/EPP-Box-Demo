package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eppdraft1.R
import com.example.eppdraft1.main.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

open class BaseActivity : AppCompatActivity() {

    private  var mProgressDialog : Dialog? = null
    private  var mProgressDialogLight : Dialog? = null

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }


    fun showCustomSnackBar(message: String, alert: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        val customSnackBarView = layoutInflater.inflate(R.layout.snackbar_custom, null)
        val snackBarView = snackBar.view

        snackBarView.setBackgroundColor(Color.TRANSPARENT)

        val snackBarLayout : Snackbar.SnackbarLayout = snackBarView as Snackbar.SnackbarLayout
        snackBarLayout.setPadding(0, 0, 0, 0)

        val textViewSnackBar : TextView = customSnackBarView.findViewById(R.id.tv_snackBar_c)
        textViewSnackBar.text = message

        val imageViewSnackBar : ImageView = customSnackBarView.findViewById(R.id.iv_snackBar_custom_c)
        if (alert){
            imageViewSnackBar.setImageResource(R.drawable.white_warning_24)
        } else {
            imageViewSnackBar.setImageResource(R.drawable.white_thumb_up_24)
        }
        val linearLayoutAlert : LinearLayout = customSnackBarView.findViewById(R.id.snackBar_icon_layout)
        if (alert) {
            linearLayoutAlert.setBackgroundColor(ContextCompat.getColor(this, R.color.error_red2))
        } else {
            linearLayoutAlert.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        }

        snackBarLayout.addView(customSnackBarView, 0 )
        snackBar.show()
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.custom_progressdialog)
        mProgressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.show()
    }

    fun hideProgressDialog(){
        if (!isProgressDialogNull())
            mProgressDialog!!.dismiss()
    }

    fun isProgressDialogNull() : Boolean {
        return mProgressDialog == null
    }


    fun getCurrentUserId(): String {
        return if (FirebaseAuth.getInstance().currentUser!=null) {
            FirebaseAuth.getInstance().currentUser!!.uid
        } else{
            ""
        }
    }

    fun updatePassword(activity: PasswordUpdateActivity, passwd: String){
        FirebaseAuth.getInstance().currentUser!!.updatePassword(passwd).addOnSuccessListener {
           activity.passwordUpdated()

        }.addOnFailureListener {
            e ->
            hideProgressDialog()
            if (e.message.toString().contains("Log in again before retrying this request")){
                showCustomNotificationAlertDialog(activity.resources.getString(R.string.unsuccessful_update),
                    activity.resources.getString(R.string.unsuccessful_update_detail))
            } else {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }
    }

    fun getCurrentUserEmail() : String {
        return FirebaseAuth.getInstance().currentUser!!.email!!
    }

    fun showCustomAlertDialog(text1: String, text2: String){

        val cancelGrayButton: Button
        val okButton: Button
        val textView1 : TextView
        val textView2 : TextView
        val customAlertDialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val alert = AlertDialog.Builder(this)
        alert.setView(customAlertDialogView)

        cancelGrayButton = customAlertDialogView.findViewById(R.id.cancel_button_custom_dialog)
        okButton = customAlertDialogView.findViewById(R.id.ok_button_custom_dialog)
        textView1 = customAlertDialogView.findViewById(R.id.tv_custom_alert_dialog_1)
        textView2 = customAlertDialogView.findViewById(R.id.tv_custom_alert_dialog_2)

        textView1.text = text1
        textView2.text = text2

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.show()

        cancelGrayButton.setOnClickListener {
            dialog.dismiss()
        }

        okButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showCustomNotificationAlertDialog(text1: String, text2: String){

        val okButton: Button
        val textView1 : TextView
        val textView2 : TextView
        val customAlertDialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val alert = AlertDialog.Builder(this)
        alert.setView(customAlertDialogView)

        okButton = customAlertDialogView.findViewById(R.id.ok_button_custom_notify_dialog)
        textView1 = customAlertDialogView.findViewById(R.id.tv_custom_notify_alert_dialog_1)
        textView2 = customAlertDialogView.findViewById(R.id.tv_custom_notify_alert_dialog_2)

        textView1.text = text1
        textView2.text = text2

        dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.show()

        okButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun isCustomNotificationAlertDialogActive() : Boolean {
        return if (::dialog.isInitialized){
            dialog.isShowing
        } else {
            false
        }

    }



    fun validateForm(param1: String = "", param2: String = "", param3: String = "",
                     param4: String = "", param5: String = "", param6: String = "",
                    param7: Int = -1, param8: Int = -1, param9: String = "") : Boolean {
        return when{
            TextUtils.isEmpty(param1) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param2) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param3) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param4) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param5) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param6) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
           param7 == -1 -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            param8 == -1 -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(param9) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null ){
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null){
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    Log.i(Constants.TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    Log.i(Constants.TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    Log.i(Constants.TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun isManager() : Boolean {
        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        return sharedPreference.getString(Constants.ROLE,"").toString() == Constants.MANAGER
    }

    fun isManagerOrTrainer() : Boolean {
        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        return sharedPreference.getString(Constants.ROLE,"").toString() == Constants.MANAGER ||
                sharedPreference.getString(Constants.ROLE,"").toString() == Constants.TRAINER
    }

    // Get the current time GMT
    fun getCurrentTimeGMT() : Long {
        // Get the current date time in millis
        val currentInstant: org.joda.time.Instant = org.joda.time.Instant.now()
        return currentInstant.millis  // From time zone GMT
    }

    // Get the current time adjusted to CCS -4:00 GMT
    fun getCurrentTimeCCS() : Long {
        // Get the current date time in millis
        val currentInstant: org.joda.time.Instant = org.joda.time.Instant.now()
        return currentInstant.millis - 14400000 // Adjusted to time zone Caracas -4:00 GMT
    }

    // Get the time adjusted to CCS -4:00 GMT plus one hour
    fun getCurrentTimeCCSMinusOneHour() : Long {
        // Get the current date time in millis
        val currentInstant: org.joda.time.Instant = org.joda.time.Instant.now()
        return currentInstant.millis - 14400000 - 3600000 // Adjusted to time zone Caracas -4:00 GMT
    }

    // Time in millis that is later saved to Workout, is created using Calendar
    // Output in millis, long, already adapted for CCS time zone -4:00 GMT
    fun getTimeInMillisCCS(year: Int, month: Int, day: Int, hour: Int,
                           minute: Int = 0, second: Int = 0) : Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // Months are 0-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day)

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis - 14400000 // Correction for -4:00 GMT
    }

    // Calendar in Create Workout related
    fun daysInMonthArray (date: LocalDate) : ArrayList<String> {
        val daysInMonthArray : ArrayList<String> = ArrayList()
        val yearMonth : YearMonth = YearMonth.from(date)
        val daysInMonth : Int = yearMonth.lengthOfMonth()

        val firstDayOfMonth : LocalDate = date.withDayOfMonth(1)
        val dayOfWeek : Int = firstDayOfMonth.dayOfWeek.value // Mon = 1, Sun = 7

        if (dayOfWeek == 7) {
            for (i in 1..35){
                if (i > daysInMonth ){
                    daysInMonthArray.add("")
                } else {
                    daysInMonthArray.add((i).toString())
                }
            }
        } else {

            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                    daysInMonthArray.add("")
                } else {
                    daysInMonthArray.add((i - dayOfWeek).toString())
                }
            }
            val top : Int = daysInMonthArray.size - 1
            val bottom : Int = daysInMonthArray.size - 7
            var emptyCount : Int = 0
            for (i in bottom..top){
                if (daysInMonthArray[i].isEmpty()){
                    emptyCount++
                }
            }
            if (emptyCount == 7){
                for (i in 1..7) {
                    daysInMonthArray.removeLast()
                }
            }
        }


        return daysInMonthArray
    }

    // Calendar in Create Workout related
    fun monthYearFromDate(date: LocalDate) : String {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }


    fun hideSoftKeyboard(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = (context as Activity).currentFocus

        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    fun convertFilteredDay(originalFilteredDay: String): String {
        val trimmedDay: String
        when(originalFilteredDay){
            "01" -> {
                trimmedDay = "1"
            }
            "02" -> {
                trimmedDay = "2"
            }
            "03" -> {
                trimmedDay = "3"
            }
            "04" -> {
                trimmedDay = "4"
            }
            "05" -> {
                trimmedDay = "5"
            }
            "06" -> {
                trimmedDay = "6"
            }
            "07" -> {
                trimmedDay = "7"
            }
            "08" -> {
                trimmedDay = "8"
            }
            "09" -> {
                trimmedDay = "9"
            }
            else -> {
                trimmedDay = originalFilteredDay
            }
        }

        return trimmedDay
    }



}