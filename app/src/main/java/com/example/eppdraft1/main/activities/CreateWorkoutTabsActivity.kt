package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.FragmentPagerOwnAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.fragments.CreateWorkoutFragment1
import com.example.eppdraft1.main.fragments.CreateWorkoutFragment2
import com.example.eppdraft1.main.models.Attendee
import com.example.eppdraft1.main.models.UserNotes
import com.example.eppdraft1.main.models.Workout
import com.google.android.material.tabs.TabLayout
import org.threeten.bp.LocalDate
import kotlin.collections.ArrayList

class CreateWorkoutTabsActivity : BaseActivity(), CreateWorkoutFragment1.OnTextEnteredListener,
                                    CreateWorkoutFragment2.OnDateModifiedListener{

    private lateinit var toolbar: Toolbar
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: FragmentPagerOwnAdapter

    private var day : String = ""  // 23 format
    private var month : String = "" // 5 format
    private var year: String = ""  // 2023 format

    private var dayArray : ArrayList<String> = ArrayList() // array of day
    private var monthArray : ArrayList<String> = ArrayList() // array of month
    private var yearArray : ArrayList<String> = ArrayList() // array of year

    private var description = ""
    private var trainerName = ""
    private var finalDateTimeInMillis : Long = 0
    private var finalDateTimeInMillisArray : ArrayList<Long> = ArrayList()

    private var capacity : Int = -1 // initial invalid value
    private var details = ""
    //private var duration : Int = 1 // initial value

    companion object {
        var datesPicked : ArrayList<LocalDate> = ArrayList()
        var time = "" // 7:00 AM format
        var timeHour : Int = 0
        var timeMinute : Int = 0
        var workoutList : ArrayList<Workout> = ArrayList()

        var duration : Int = 1 // initial value

        var unsavedData : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout_tabs)

        setupActionBar()

        time = ""  // Clear time whenever onCreate is run
        timeHour = 0
        timeMinute = 0

        viewPager2 = findViewById(R.id.viewPager2)
        tabLayout = findViewById(R.id.tabLayout)

        adapter = FragmentPagerOwnAdapter(supportFragmentManager, lifecycle, this, this)

        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.workout_description)))
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.workout_dates)))

        viewPager2.adapter = adapter


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                hideSoftKeyboard(this@CreateWorkoutTabsActivity)

                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {  }

            override fun onTabReselected(tab: TabLayout.Tab?) {  }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })


        // Initially clear the datesPicked list, when the activity is created
        datesPicked.clear()

        // Initially clearing the workoutList
        workoutList.clear()

        // reset to False when creating the activity
        unsavedData = false

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (unsavedData){ // If data has been inserted, Dialog window to confirm leaving the activity
                    displayCustomDialog(resources.getString(R.string.create_workouts),
                        resources.getString(R.string.leave_without_saving), true)
                } else {
                    finish()
                }
            }
        })

    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar_create_workout_tabs)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener {
            if (unsavedData){ // If data has been inserted, Dialog window to confirm leaving the activity
                displayCustomDialog(resources.getString(R.string.create_workouts),
                                    resources.getString(R.string.leave_without_saving), true)
            } else {
                finish()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_workouts_tabs, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save_workout_tab -> {
                if (isOnline(this)) {
                    initiateWorkoutCreation()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initiateWorkoutCreation() {

        if (datesPicked.size > 0) { // at least one WO to be created

            year = datesPicked[0].year.toString()
            month = convertMonthToNumber(datesPicked[0].month.toString()).toString()
            day = datesPicked[0].dayOfMonth.toString()

            if (validateForm(year, month, day, time, trainerName, description, capacity, duration, details)) {

                    if (datesPicked.size == 1) { // one WO to be created
                        displayCustomDialog(
                            resources.getString(R.string.create_workouts),
                            resources.getString(R.string.create_wo_dialog, "1", "")
                        )
                    } else { // more than one WO to be created
                        displayCustomDialog(
                            resources.getString(R.string.create_workouts),
                            resources.getString(R.string.create_wo_dialog, datesPicked.size.toString(),"s")
                        )
                    }
            }

        } else {
            showCustomSnackBar(resources.getString(R.string.pls_select_dates), true)

        }

    }

    private fun completeWorkoutCreation() {

        // Construct the finalDateTimeInMillisArray
        constructDateTimeMillisArray()

        showProgressDialog()
        // Ask for the creation of workout. No verification of existing workout here
        val athleteAttendees : ArrayList<Attendee> = ArrayList() // empty initial ArrayList of User
        val userNotes: ArrayList<UserNotes> = ArrayList() // empty initial ArrayList of UserNotes

        val workoutArray : ArrayList<Workout> = ArrayList()
        for (i in datesPicked.indices){
            val workout = Workout(
                finalDateTimeInMillisArray[i].toString(),
                description, yearArray[i], monthArray[i], dayArray[i], time, trainerName,
                "Yes", athleteAttendees, capacity, details, duration, "", userNotes
            )

            workoutArray.add(workout)
        }
        //FirestoreClass().registerWorkout(this, workout)
        FirestoreClass().registerWorkoutArray(this, workoutArray)
    }

    // Generates finalDateTimeInMillisArray, yearArray, monthArray, dayArray
    private fun constructDateTimeMillisArray()  {

        // Initially delete the content of timeDate in millis, year array, month array, day array
        finalDateTimeInMillisArray.clear()
        yearArray.clear()
        monthArray.clear()
        dayArray.clear()

        // Create yearArray, monthArray and dayArray
        for (i in datesPicked.indices){
            year = datesPicked[i].year.toString()
            yearArray.add(year)

            month = convertMonthToNumber(datesPicked[i].month.toString()).toString()
            monthArray.add(month)

            day = datesPicked[i].dayOfMonth.toString()
            dayArray.add(day)

            finalDateTimeInMillis = getTimeInMillisCCS(
                yearArray[i].toInt(), monthArray[i].toInt(), dayArray[i].toInt(),
                timeHourToInt(time), timeMinuteToInt(time)
            )

            finalDateTimeInMillisArray.add(finalDateTimeInMillis)
        }

    }


    private fun timeHourToInt(timeString: String) : Int {
        // timeString format "7:00 AM"
        val hourString = timeString.split(":")[0]
        val amPM = timeString.split(" ")[1]
        return if (amPM == "AM"){
            if (hourString == "12"){
                0
            } else {
                hourString.toInt()
            }

        } else { // PM
            if (hourString == "12"){
                hourString.toInt()
            } else {
                hourString.toInt() + 12
            }
        }

    }

    private fun timeMinuteToInt(timeString: String) : Int {
        // timeString format "7:00 AM"
        return timeString.split(":")[1].split(" ")[0].toInt()

    }


    // From Interface at Fragment 1, listener, Wo Name
    override fun onEditTextWoName(text: String) {
        description = text
        unsavedData = true
    }

    // From Interface at Fragment 1, listener, Duration selected
    override fun onAutoCompleteDuration(durationFragment1: String) {

        val durationGathered = if (durationFragment1.length>1) {
            durationFragment1.split(" ")[0].toInt()
        } else {
            1 // Undefined duration means 1 hour
        }
        if (durationGathered != duration){
            duration = durationGathered
            datesPicked.clear()
            unsavedData = true
        }

    }

    // From Interface at Fragment 1, listener, Trainer selected
    override fun onAutoCompleteTrainers(text: String) {
        trainerName = text
        unsavedData = true
    }

    // From Interface at Fragment 1, listener, Wo Details edited
    override fun onEditTextWoDetails(text: String) {
        details = text
        unsavedData = true
    }

    // From Interface at Fragment 1, listener, Wo Capacity
    override fun onAutoCompleteCapacity(capacity: String) {
        if (capacity.length>1) {
            this.capacity = capacity.split(" ")[0].toInt()
        } else {
            this.capacity = 32 // Infinite capacity established as 32
        }
        unsavedData = true
    }

    // From Interface at Fragment 2, listener, date added
    override fun dateAdded(date: LocalDate) {
        //Add to the ArrayList of LocalDate
        if (!datesPicked.contains(date)){
            datesPicked.add(date)
        }
        unsavedData = true
    }

    // From Interface at Fragment 2, listener, date removed
    override fun dateRemoved(date: LocalDate) {
        //Remove from the ArrayList of LocalDate
        if (datesPicked.contains(date)){
            datesPicked.remove(date)
        }
    }

    // From Interface at Fragment 2, Alert about the time set
    override fun timePicked(year: Int, month: Int, timeText: String) {
        time = timeText // Format "5:00 PM" String
        timeHour = timeHourToInt(time)
        timeMinute = timeMinuteToInt(time)
        datesPicked.clear() // Since time was updated, pickedDates should be cleared

        unsavedData = true

        // Go retrieve the list of workouts with Year, Month and time that matches
        showProgressDialog()
        // FirestoreClass().listWorkoutInYearMonthTime(this, year.toString(), month.toString(), time)
        FirestoreClass().listWorkoutInYearMonth(this, year.toString(), month.toString())

    }

    // From Interface at Fragment 2, listener Month changed
    override fun monthChanged(year: Int, month: Int, time: String) {
        // Go retrieve the list of workouts with Year, Month and time that matches
        showProgressDialog()
        // FirestoreClass().listWorkoutInYearMonthTime(this, year.toString(), month.toString(), time)
        FirestoreClass().listWorkoutInYearMonth(this, year.toString(), month.toString())
    }

    // From Interface at Fragment 2, listener to sho snack-bar
    override fun showSnack(text: String, alert: Boolean) {
        showCustomSnackBar(text, alert)
    }

    private fun convertMonthToNumber(monthText: String): Int {
        when(monthText){
            "JANUARY" -> {
                return 1
            }
            "FEBRUARY" -> {
                return 2
            }
            "MARCH" -> {
                return 3
            }
            "APRIL" -> {
                return 4
            }
            "MAY" -> {
                return 5
            }
            "JUNE" -> {
                return 6
            }
            "JULY" -> {
                return 7
            }
            "AUGUST" -> {
                return 8
            }
            "SEPTEMBER" -> {
                return 9
            }
            "OCTOBER" -> {
                return 10
            }
            "NOVEMBER" -> {
                return 11
            }
            "DECEMBER" -> {
                return 12
            }
        }
        return 0
    }

    fun workoutsCreatedSuccess(){
        unsavedData = false
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    // Return from FirestoreClass().listWorkoutInYearMonth called from
    // overriding timePicked defined in fragment 2
    fun saveTheWorkoutListForTime(listWorkoutForTime: ArrayList<Workout>) {
        hideProgressDialog()
        workoutList = listWorkoutForTime

        // Tell the fragment2 to run setMonthView
        val fragment = adapter.getFragmentAtPosition(1)
        if (fragment is CreateWorkoutFragment2){
            fragment.setMonthView()
        }

    }


    private fun displayCustomDialog(title: String, text2: String, leaveActivity: Boolean = false) {

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
        textView2.text = text2

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.show()

        cancelGrayButton.setOnClickListener {
            dialog.dismiss()
        }

        okButton.setOnClickListener {
            if (leaveActivity){
                finish()
            } else {
                completeWorkoutCreation()
            }
        }

    }

}