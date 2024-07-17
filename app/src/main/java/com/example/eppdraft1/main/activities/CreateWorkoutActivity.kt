package com.example.eppdraft1.main.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.CalendarWoCreateAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.Workout
import com.example.eppdraft1.main.utils.Constants
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_create_workout.*
import kotlinx.android.synthetic.main.item_workout.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class CreateWorkoutActivity : BaseActivity(), CalendarWoCreateAdapter.OnItemListener {

    private lateinit var day : String
    private lateinit var month : String
    private lateinit var year: String

    private var description = ""
    private var time = ""
    private var trainerName = ""
    private var finalDateTimeInMillis : Long = 0

    // Calendar related variables * * *
    private lateinit var buttonPrevMonth: ImageView
    private lateinit var buttonNextMonth: ImageView
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate
    private val datesPicked: ArrayList<LocalDate> = ArrayList()
    // * * *

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Calendar related, for LocalDate's
        AndroidThreeTen.init(this)
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()

        val trainerNames = arrayOf("Gabriela Terán", "Ernesto Carrera", "José Salazar")
        val adapterTrainerNames = ArrayAdapter(this, R.layout.dropdown_item, trainerNames)
        autocomplete_trainers.setAdapter(adapterTrainerNames)
        autocomplete_trainers.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideKeyboard()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    autocomplete_trainers.performClick()
                    true
                }
                else -> false
            }
        }

        val times = arrayOf("7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
                            "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM",
                            "5:00 PM", "6:00 PM", "7:00 PM")
        val adapterTimes = ArrayAdapter(this, R.layout.dropdown_item, times)
        autocomplete_times.setAdapter(adapterTimes)

        val selectedDateTimeInMillis = calendar_view_create_workout.date // Long

        val format = SimpleDateFormat("yyyy M d", Locale.getDefault())
        val formattedDate = format.format(selectedDateTimeInMillis) // String
        year = formattedDate.split(" ")[0] // 2023 format
        month = formattedDate.split(" ")[1] // 5 format
        day = formattedDate.split(" ")[2]  // 23 format

        calendar_view_create_workout.setOnDateChangeListener(
            CalendarView.OnDateChangeListener { _, year, month, dayOfMonth ->
                hideKeyboard()
                this.year = year.toString() // 2023 format
                this.month = (month + 1).toString() // 5 format
                this.day = dayOfMonth.toString() // 23 format

                Log.i(Constants.TAG, "Year: ${this.year} Month: ${this.month} Day: ${this.day}")

                //selectedDateTimeInMillis = calendar_view_create_workout.date // Long
            })

        btn_create_workout.setOnClickListener {
           createWorkout()
        }


    }

    private fun initWidgets(){

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView_create_wo)
        monthYearText = findViewById(R.id.monthYearTV_create_wo)
        buttonPrevMonth = findViewById(R.id.button_prev_month_create_wo)
        buttonNextMonth = findViewById(R.id.button_next_month_create_wo)

        buttonPrevMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        buttonNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
    }


    private fun createWorkout(){
        description = et_create_workout_name.text.toString()
        time = autocomplete_times.text.toString() // Format 7:00 AM
        trainerName = autocomplete_trainers.text.toString()

        if (validateForm(year, month, day, time, trainerName, description )){

            finalDateTimeInMillis = getTimeInMillisCCS(year.toInt(), month.toInt(), day.toInt(),
                                        timeToInt(time))

            showProgressDialog()
            FirestoreClass().findWorkout(this, finalDateTimeInMillis.toString())

        }
    }

    fun workoutCreatedSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun hideKeyboard(){
        val view: View? = this.currentFocus
        if (view!=null){
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }
    

    private fun timeToInt(timeString: String) : Int {
        when (timeString){
            "7:00 AM" -> {
                return 7 }
            "8:00 AM" -> {
                return 8  }
            "9:00 AM" -> {
                return 9 }
            "10:00 AM" -> {
                return 10 }
            "11:00 AM" -> {
                return 11 }
            "12:00 PM" -> {
                return 12 }
            "1:00 PM" -> {
                return 13 }
            "2:00 PM" -> {
                return 14 }
            "3:00 PM" -> {
                return 15 }
            "4:00 PM" -> {
                return 16 }
            "5:00 PM" -> {
                return 17 }
            "6:00 PM" -> {
                return 18 }
            "7:00 PM" -> {
                return 19 }
        }
        return 0
    }

    fun workoutFoundVerification(found: Boolean){
        hideProgressDialog()
        if (found){
            // Workout already created Error Alert
            showCustomSnackBar(resources.getString(R.string.workout_already_created), true)
        } else {
            val workout = Workout(finalDateTimeInMillis.toString(),
                description, year, month, day, time, trainerName)
            showProgressDialog()
            FirestoreClass().registerWorkout(this, workout)
        }

    }

    // Calendar related
    private fun setMonthView() {
        val upperLetterMonthDate = monthYearFromDate(selectedDate).replaceFirstChar { it.uppercase() }
        monthYearText.text = upperLetterMonthDate
        val daysInMonth : ArrayList<String> = daysInMonthArray(selectedDate) // this is the array of days

        // From selectedDate, get year, month, dayInMonth
        val dayInMonth = selectedDate.dayOfMonth
        val month = selectedDate.month.value
        val year = selectedDate.year
        var tempDate = LocalDate.of(year, month, dayInMonth)

        val pickedBooleanList : ArrayList<Boolean>  = ArrayList()

        for (day in daysInMonth){
            if (day.isNotEmpty()) {
                tempDate = tempDate.withDayOfMonth(day.toInt())
                if (datesPicked.contains(tempDate)) {
                    Log.i(Constants.TAG, "Date $tempDate is picked")
                    pickedBooleanList.add(true)

                } else {
                    Log.i(Constants.TAG, "Date $tempDate is NOT picked")
                    pickedBooleanList.add(false)
                }
            } else {
                Log.i(Constants.TAG, "Date $tempDate is NOT picked")
                pickedBooleanList.add(false)
            }
        }

        Log.i(Constants.TAG, "Size of calendar list: ${daysInMonth.size}")
        Log.i(Constants.TAG, "Size of picked days list: ${pickedBooleanList.size}")


        val calendarAdapter = CalendarWoCreateAdapter(daysInMonth, pickedBooleanList, this, this)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    // Calendar related
    override fun onItemClick(dayText: String?) {

    }


}