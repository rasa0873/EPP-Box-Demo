package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.WorkoutListAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.Attendee
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.models.Workout
import com.example.eppdraft1.main.utils.Constants
import com.example.eppdraft1.main.utils.SwipeGesture
import kotlinx.android.synthetic.main.activity_view_workout.*
import kotlinx.android.synthetic.main.activity_workout_list.*
import kotlinx.android.synthetic.main.activity_workout_list_recycler.*
import kotlinx.android.synthetic.main.item_workout.view.*

class WorkoutListActivity : BaseActivity(), WorkoutListAdapter.OnClickListener {

    private var newReservationMade: Boolean = false
    private lateinit var workoutData: Workout
    private var currentTimestamp : Long = 0

    // filter the original workoutList if wanted
    var filteredList = ArrayList<Workout>()
    var refreshListPosition : Int = 0

    // Create workout intent result
    private val reserveWorkoutIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                refreshAdapterReloadWorkout()
            }
        }

    private var myList : Boolean = false
    private var filteredDay: String = ""
    private var filteredMonth : String = ""
    private var filteredYear: String =  ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list)

        rv_workout_list.visibility = View.GONE
        tv_no_workout_available.visibility = View.GONE

        if (intent.hasExtra(Constants.MY_LIST)){
            myList = intent.getBooleanExtra(Constants.MY_LIST, false)
        }
        if (intent.hasExtra(Constants.DATE_DAY)){
            filteredDay = intent.getStringExtra(Constants.DATE_DAY).toString()
            filteredDay = convertFilteredDay(filteredDay)
        }
        if (intent.hasExtra(Constants.DATE_MONTH)){
            filteredMonth = intent.getStringExtra(Constants.DATE_MONTH).toString()
        }
        if (intent.hasExtra(Constants.DATE_YEAR)){
            filteredYear = intent.getStringExtra(Constants.DATE_YEAR).toString()
        }

        setupActionBar()
        showProgressBarStandAlone(true, showError = false)
        FirestoreClass().getWorkoutList(this, myList)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (newReservationMade){
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
        })

        val currentInstant: org.joda.time.Instant = org.joda.time.Instant.now()
        currentTimestamp = currentInstant.millis - 14400000 // Adjusted to time zone Caracas -4:00 GMT

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_workout_list_activity)
        /*
        if (!myList) { // Only if NOT My List
            toolbar_workout_list_activity.subtitle =
                "${filteredDay}.${filteredMonth}.${filteredYear}"
        }
         */
        toolbar_workout_list_activity.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar_workout_list_activity.setNavigationOnClickListener {
            if (newReservationMade){
                setResult(Activity.RESULT_OK)
            }
            finish()
        }
        if (myList) {
            toolbar_workout_list_activity.title = resources.getString(R.string.my_workouts)
        } else {
            toolbar_workout_list_activity.title = resources.getString(R.string.workouts)
        }
    }


    fun populateWorkoutListToUI(workoutList: ArrayList<Workout>) {
        showProgressBarStandAlone(false, showError = false)
        hideProgressDialog()

        filteredList.clear()

        if (filteredDay != "") {

            for (workout in workoutList) {
                // Populate only matching days
                if (workout.dateDay == filteredDay && workout.dateMonth == filteredMonth
                    && workout.dateYear == filteredYear) {
                    filteredList.add(workout)
                }
            }
        } else {
            filteredList = workoutList
        }

        if (filteredList.size > 0){
            tv_no_workout_available.visibility = View.GONE
            rv_workout_list.visibility = View.VISIBLE

            rv_workout_list.layoutManager = LinearLayoutManager(this)
            rv_workout_list.setHasFixedSize(true)

            val adapter = WorkoutListAdapter(this, filteredList)
            adapter.setOnClickListener(this)
            rv_workout_list.adapter = adapter

            val swipeGesture = object : SwipeGesture(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            refreshListPosition = viewHolder.layoutPosition
                            reserveExpress(filteredList[refreshListPosition].timeCreatedMillis)
                        }

                        ItemTouchHelper.RIGHT -> {

                        }
                    }

                }
            }

            if (!isManager()) { // Swipe gesture only for Athletes, so far
                val touchHelper = ItemTouchHelper(swipeGesture)
                touchHelper.attachToRecyclerView(rv_workout_list)
            }

        } else {
            tv_no_workout_available.visibility = View.VISIBLE
            rv_workout_list.visibility = View.GONE
        }
    }



    fun showProgressBarStandAlone(show: Boolean, showError: Boolean) {
        if (show){
            progressBarLightStandAlone.visibility = View.VISIBLE
        } else {
            progressBarLightStandAlone.visibility = View.GONE
        }
    }

    override fun onClick(timeDateInMillis: String) {
        val intentReserveWorkout = Intent(this, ViewWorkoutActivity::class.java)
        intentReserveWorkout.putExtra(Constants.DATE_MILLIS, timeDateInMillis)
        reserveWorkoutIntentResult.launch(intentReserveWorkout)
    }


    private fun reserveExpress(timeCreatedMillis : String){
        if (isOnline(this)) {
            // first, load workout data, then load user data and check if already reserved
            showProgressDialog()
            FirestoreClass().loadWorkoutData(this, timeCreatedMillis)
        } else {
            showCustomNotificationAlertDialog(resources.getString(R.string.connectivity),
                resources.getString(R.string.no_access_to_internet))
            refreshAdapterOnly()
        }

    }

    fun reserveExpress2 (workout: Workout){
        workoutData = workout
        FirestoreClass().loadUserData(this)

    }

    fun reserveExpress3 (loggedUserData : User) {
        if (loggedUserData.status != "active"){
            hideProgressDialog()
            showCustomSnackBar(resources.getString(R.string.no_balance_to_reserve), true)
            refreshAdapterOnly()
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
            refreshAdapterOnly()
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
        showProgressDialog()
        FirestoreClass().getWorkoutList(this, myList)
        showCustomSnackBar(resources.getString(R.string.workout_reserved), false)
        newReservationMade = true
    }

    fun refreshAdapterOnly(){
        val adapter = WorkoutListAdapter(this, filteredList)
        adapter.setOnClickListener(this)
        rv_workout_list.adapter = adapter
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
            refreshAdapterOnly()
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