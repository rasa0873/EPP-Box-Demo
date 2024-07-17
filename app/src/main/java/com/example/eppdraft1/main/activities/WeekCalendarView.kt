package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.CalendarAdapter
import com.example.eppdraft1.main.adapters.WeekWorkoutListAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.Attendee
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.models.Workout
import com.example.eppdraft1.main.utils.CalendarUtils
import com.example.eppdraft1.main.utils.Constants
import com.example.eppdraft1.main.utils.SwipeGesture
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate


// This class uses two adapters: one for a 7-day week based adapter, and another for
// vertical orientation Workout list

class WeekCalendarView : BaseActivity(), CalendarAdapter.OnItemListener, WeekWorkoutListAdapter.OnClickListener  {

    private lateinit var toolbar: Toolbar
    private lateinit var buttonPrevWeek: ImageView
    private lateinit var buttonNextWeek: ImageView
    private lateinit var monthYearText: TextView

    private lateinit var calendarRecyclerViewWeek: RecyclerView
    private lateinit var recyclerViewWorkoutsWeek: RecyclerView

    private lateinit var tvNoWorkoutAvailable: TextView
    private lateinit var progressBarWorkoutsLoading: ProgressBar

    private var filteredDay: String = "" // Format 17
    private var filteredMonth : String = "" // Format 5 (MAY)
    private var filteredYear: String =  "" // Format 2023

    private var workoutListComplete = ArrayList<Workout>()
    private var filteredList = ArrayList<Workout>()
    var refreshListPosition : Int = 0

    private lateinit var workoutData: Workout
    private var currentTimestamp : Long = 0

    // Create workout intent result
    private val reserveCreateWorkoutIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                refreshSelectedDayWorkoutList(CalendarUtils.selectedDate, "", true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_calendar)

        setupActionBar()

        AndroidThreeTen.init(this)

        initWidgetsAndBarView()
        CalendarUtils.selectedDate = LocalDate.now()
        filteredDay = CalendarUtils.selectedDate.dayOfMonth.toString() // Format 17
        filteredMonth = CalendarUtils.selectedDate.month.toString() // Format "JUNE"
        filteredYear = CalendarUtils.selectedDate.year.toString() // Format 2023

        setWeekView()

        currentTimestamp = getCurrentTimeCCS()

        FirestoreClass().getWorkoutList(this, false)
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar_week_calendar_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener {
            finish()
        }


    }

    private fun initWidgetsAndBarView() {

        tvNoWorkoutAvailable = findViewById(R.id.tv_no_workout_available_week)
        tvNoWorkoutAvailable.visibility = View.GONE
        recyclerViewWorkoutsWeek = findViewById(R.id.rv_workout_list_week)
        recyclerViewWorkoutsWeek.visibility = View.GONE
        progressBarWorkoutsLoading = findViewById(R.id.progressBarLightStandAlone_week) // initially visible

        calendarRecyclerViewWeek = findViewById(R.id.calendarRecyclerViewWeek)
        monthYearText = findViewById(R.id.monthYearTV_week)
        buttonPrevWeek = findViewById(R.id.button_prev_week)
        buttonNextWeek = findViewById(R.id.button_next_week)

        buttonPrevWeek.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1)
            refreshSelectedDayWorkoutList(CalendarUtils.selectedDate, "Minus")
        }

        buttonNextWeek.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
            refreshSelectedDayWorkoutList(CalendarUtils.selectedDate, "Plus")
        }
    }

    // Setup weekly calendar adapter and update its data, updates toolbar title
    private fun setWeekView(alterDayOfMonth: String = "") {
        val title = "${CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)}"

        val capTitle = title.substring(0, 1).uppercase() + title.substring(1)
        toolbar.title = capTitle  // Set toolBar name
        monthYearText.text = "${resources.getString(R.string.week)} ${CalendarUtils.getWeekNumber(CalendarUtils.selectedDate)}"

        val days : ArrayList<LocalDate> = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate) // this is the array of 7 days

        // Adjust the week's day if Plus or Minus week button was clicked
        /*
        if (alterDayOfMonth.isNotEmpty()) {
            if (alterDayOfMonth == "Plus"){ // Plus 1 week
                // The day will be next week's Sunday
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.withDayOfMonth(days[0].dayOfMonth)
            } else { // Minus 1 week
                // The day will be next week's Saturday
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.withDayOfMonth(days[6].dayOfMonth)
            }
            filteredDay = CalendarUtils.selectedDate.dayOfMonth.toString() // Format 17
        }

         */

        val calendarAdapter = CalendarAdapter(days, this, this) // Adapter showing the days of week
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerViewWeek.layoutManager = layoutManager
        calendarRecyclerViewWeek.adapter = calendarAdapter
    }

    // When clicking on a week date
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date!=null)
            refreshSelectedDayWorkoutList(date)
    }

    private fun refreshSelectedDayWorkoutList(date: LocalDate, weekPlusMinus: String = "", activityResult: Boolean = false){
        if (CalendarUtils.selectedDate == date && weekPlusMinus.isEmpty() && !activityResult) {
            // In case you try to refresh on the already chosen date
            return
        }
        CalendarUtils.selectedDate = date

        filteredDay = CalendarUtils.selectedDate.dayOfMonth.toString() // Format 17
        filteredMonth = CalendarUtils.selectedDate.month.toString() // Format "JUNE"
        filteredYear = CalendarUtils.selectedDate.year.toString() // Format 2023

        //setWeekView(weekPlusMinus)
        setWeekView() // Update weekly calendar and toolbar title

        progressBarWorkoutsLoading.visibility = View.VISIBLE // while fetching and loading new workout list
        tvNoWorkoutAvailable.visibility = View.GONE
        recyclerViewWorkoutsWeek.visibility = View.GONE
        FirestoreClass().getWorkoutList(this, false)
    }

    // Setup workout list adapter and display its data
    fun showWeeklyWorkouts(workoutList: ArrayList<Workout>) {
        progressBarWorkoutsLoading.visibility = View.GONE

        workoutListComplete = workoutList
        filteredList.clear()
        // filteredList = workoutList
        for (workout in workoutList){

            // Workout list filter criteria - Populate only matching days
            if (workout.dateDay == filteredDay && workout.dateMonth == convertMonthToNumber(filteredMonth)
                && workout.dateYear == filteredYear) {
                filteredList.add(workout)
            }
        }

        if (filteredList.size > 0) {
            tvNoWorkoutAvailable.visibility = View.GONE
            recyclerViewWorkoutsWeek.visibility = View.VISIBLE

            recyclerViewWorkoutsWeek.layoutManager = LinearLayoutManager(this)
            recyclerViewWorkoutsWeek.setHasFixedSize(true)

            val adapter = WeekWorkoutListAdapter(this, filteredList) // Adapter showing the workouts in vertical
            adapter.setOnClickListener(this)
            recyclerViewWorkoutsWeek.adapter = adapter

            val swipeGesture = object : SwipeGesture(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            refreshListPosition = viewHolder.layoutPosition
                            reserveExpress(filteredList[refreshListPosition].timeCreatedMillis)
                        }

                    }

                }
            }

            if (!isManager()) { // Swipe gesture only for Athletes, so far
                val touchHelper = ItemTouchHelper(swipeGesture)
                touchHelper.attachToRecyclerView(recyclerViewWorkoutsWeek)
            }

        } else {
            tvNoWorkoutAvailable.visibility = View.VISIBLE
            recyclerViewWorkoutsWeek.visibility = View.GONE
        }
    }

    private fun convertMonthToNumber(monthText : String) : String {
        when (monthText) {
            "JANUARY" -> {
                return "1"
            }
            "FEBRUARY" -> {
                return "2"
            }
            "MARCH" -> {
                return "3"
            }
            "APRIL" -> {
                return "4"
            }
            "MAY" -> {
                return "5"
            }
            "JUNE" -> {
                return "6"
            }
            "JULY" -> {
                return "7"
            }
            "AUGUST" -> {
                return "8"
            }
            "SEPTEMBER" -> {
                return "9"
            }
            "OCTOBER" -> {
                return "10"
            }
            "NOVEMBER" -> {
                return "11"
            }
            "DECEMBER" -> {
                return "12"
            }
        }

        return ""
    }

    override fun onClick(timeDateInMillis: String) {
        val intentReserveWorkout = Intent(this, ViewWorkoutActivity::class.java)
        intentReserveWorkout.putExtra(Constants.DATE_MILLIS, timeDateInMillis)
        reserveCreateWorkoutIntentResult.launch(intentReserveWorkout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isManager())
            menuInflater.inflate(R.menu.menu_workout_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (isOnline(this)) {
            when (item.itemId) {
                R.id.menu_item_add_workout -> {
                    //val intent = Intent(this, CreateWorkoutActivity::class.java)
                    val intent = Intent(this, CreateWorkoutTabsActivity::class.java)
                    // Include workoutList as extra
                    //intent.putExtra("workoutList", workoutListComplete)
                    reserveCreateWorkoutIntentResult.launch(intent)
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        } else {
            showCustomNotificationAlertDialog(getString(R.string.connectivity),
                getString(R.string.no_access_to_internet))
            super.onOptionsItemSelected(item)
        }
    }

    private fun refreshWorkoutListAdapterOnly(){
        val adapter = WeekWorkoutListAdapter(this, filteredList) // Adapter showing the workouts in vertical list
        adapter.setOnClickListener(this)
        recyclerViewWorkoutsWeek.adapter = adapter
    }


    // Beginning of the reservation by swipe gesture
    private fun reserveExpress(timeCreatedMillis : String) {
        if (isOnline(this)) {
            // first, load workout data, then load user data and check if already reserved
            showProgressDialog()
            FirestoreClass().loadWorkoutData(this, timeCreatedMillis)
        } else {
            showCustomNotificationAlertDialog(resources.getString(R.string.connectivity),
                resources.getString(R.string.no_access_to_internet))
            refreshWorkoutListAdapterOnly()
        }

    }

    // 2nd in reservation by swipe gesture - received workout data
    fun reserveExpress2 (workout: Workout){
        workoutData = workout
        // Now attempt to load current user data
        FirestoreClass().loadUserData(this)
    }

    // 3rd in reservation by swipe gesture - received current user data
    fun reserveExpress3 (loggedUserData : User) {
        if (loggedUserData.status != "active") {
            hideProgressDialog()
            showCustomSnackBar(resources.getString(R.string.no_balance_to_reserve), true)
            refreshWorkoutListAdapterOnly()
            return
        }

        var reserved = false
        for (user in workoutData.athleteAttendees) {
            if (user.id == getCurrentUserId()) {
                reserved = true
            }
        }

        // Check time and date
        val timeCreatedMillisHourGap: Long = workoutData.timeCreatedMillis.toLong() - 3600000

        if (currentTimestamp >= timeCreatedMillisHourGap) {
            hideProgressDialog()
            showCustomSnackBar(resources.getString(R.string.not_possible_reserve), true)
            refreshWorkoutListAdapterOnly()
            return
        }

        if (!reserved) {  // if not already reserved

            workoutData.athleteAttendees.add(Attendee(loggedUserData.id, "No"))
            reserveExpress4()

        } else { // else, if it is already reserved

            // Custom alert dialog before calling reserveExpress4
            showWorkoutListAlertDialog(resources.getString(R.string.my_workouts),
                resources.getString(R.string.confirm_wod_reservation_cancel), loggedUserData)
        }

    }

    private fun reserveExpress4() {

        // after you updated the athleteAttendee list
        val workoutHashMap = HashMap<String, Any>()
        workoutHashMap[Constants.ATHLETE_ATTENDEES] = workoutData.athleteAttendees

        FirestoreClass().updateWorkoutData(this,workoutData.timeCreatedMillis,
            workoutHashMap
        )

    }

    fun reserveExpressResult(){
        hideProgressDialog()
        refreshAdapterReloadWorkout()
    }

    private fun refreshAdapterReloadWorkout(){
        //showProgressDialog()
        FirestoreClass().getWorkoutList(this, false)
        showCustomSnackBar(resources.getString(R.string.workout_reserved), false)
    }

    private fun showWorkoutListAlertDialog(title: String, text: String, userData: User){

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

        textView1.text = title
        textView2.text = text

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.show()

        cancelGrayButton.setOnClickListener {
            dialog.dismiss()
            hideProgressDialog()
            refreshWorkoutListAdapterOnly()
        }

        okButton.setOnClickListener {

            var i: Int? = null
            for (y in workoutData.athleteAttendees.indices) {
                if (workoutData.athleteAttendees[y].id == userData.id) // or getCurrentUserId()
                    i = y
            }
            if (i != null) {
                workoutData.athleteAttendees.removeAt(i)
            }

            reserveExpress4()
            dialog.dismiss()
        }
    }
}