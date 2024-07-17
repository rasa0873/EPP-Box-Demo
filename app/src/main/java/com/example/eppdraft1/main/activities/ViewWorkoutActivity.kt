package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.AthleteReservationListAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.*
import com.example.eppdraft1.main.utils.Constants
import kotlinx.android.synthetic.main.activity_view_workout.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ViewWorkoutActivity : BaseActivity(), AthleteReservationListAdapter.OnClickListener {

    private var workoutInfo: Workout? = null
    private var reserved: Boolean = false

    private var updateData: Boolean = false

    private var currentTimestamp : Long = 0
    private var timeCreatedMillisHourGap : Long = 0

    private lateinit var tvViewWorkoutName: TextView
    private lateinit var tvViewWorkoutTrainer: TextView
    private lateinit var tvViewWorkoutTime: TextView
    private lateinit var tvViewWorkoutDescription: TextView
    private lateinit var tvViewWorkoutDate: TextView
    private lateinit var tvViewWorkoutDuration: TextView

    private val userAttendeeList : ArrayList<UserAttendee> = ArrayList()

    private lateinit var ivWorkoutCancelDelete : ImageView

    private lateinit var cardViewNotes: CardView
    private lateinit var rlUserNotes: RelativeLayout
    private lateinit var tvUserNotes: TextView
    private lateinit var ivUserNotesPen: ImageView
    private lateinit var rlTrainerNotes: RelativeLayout
    private lateinit var tvTrainerNotes: TextView
    private lateinit var ivTrainerNotePen: ImageView

    companion object {
        var attendeeSaveMode : Boolean = false
        var trainerNotesSaved: String = ""
        var userNotesSaved: ArrayList<UserNotes> = ArrayList()
    }

    // Return from WorkoutNotes
    private val resultFromNotes =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                if (isManagerOrTrainer()) { // viewer is Manager or Trainer
                    workoutInfo!!.trainerNotes = trainerNotesSaved
                } else { // viewer is Athlete
                    workoutInfo!!.userNotes = userNotesSaved
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_workout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        cardViewNotes = findViewById(R.id.card_view_workout_notes)
        cardViewNotes.visibility = View.GONE // Initially GONE, while workout info is retrieved

        rlUserNotes = findViewById(R.id.rl_user_notes)
        tvUserNotes = findViewById(R.id.tv_user_notes_title)
        tvUserNotes.setOnClickListener {
            jumpToWorkoutNotes(true, workoutInfo!!.timeCreatedMillis)
        }
        ivUserNotesPen = findViewById(R.id.iv_view_workout_user_note_pen)
        ivUserNotesPen.setOnClickListener {
            jumpToWorkoutNotes(true, workoutInfo!!.timeCreatedMillis)
        }

        rlTrainerNotes = findViewById(R.id.rl_trainer_notes)
        tvTrainerNotes = findViewById(R.id.tv_trainer_notes_title)
        tvTrainerNotes.setOnClickListener {
            jumpToWorkoutNotes(false, workoutInfo!!.timeCreatedMillis)
        }

        ivTrainerNotePen = findViewById(R.id.iv_view_workout_trainer_note_pen)
        ivTrainerNotePen.setOnClickListener {
            jumpToWorkoutNotes(false, workoutInfo!!.timeCreatedMillis)
        }

        tvViewWorkoutTime = findViewById(R.id.tv_view_workout_time)
        tvViewWorkoutTime.text = "" // Initially blank

        tvViewWorkoutName = findViewById(R.id.tv_view_workout_name)
        tvViewWorkoutName.text = "" // Initially blank

        tvViewWorkoutTrainer = findViewById(R.id.tv_view_workout_trainer)
        tvViewWorkoutTrainer.text = "" // Initially blank

        tvViewWorkoutDescription = findViewById(R.id.tv_view_workout_description)
        tvViewWorkoutDescription.text = "" // Initially blank

        tvViewWorkoutDate = findViewById(R.id.tv_view_workout_date)
        tvViewWorkoutDate.text = "" // Initially blank

        tvViewWorkoutDuration = findViewById(R.id.tv_view_workout_duration)
        tvViewWorkoutDuration.text = "" // Initially blank

        ivWorkoutCancelDelete = findViewById(R.id.iv_workout_cancel_delete)
        ivWorkoutCancelDelete.visibility = View.INVISIBLE // Initially invisible
        ivWorkoutCancelDelete.setOnClickListener {

            if (checkWodTimePassed()){
                // Wod time already passed
                showCustomAlertDialogCancelWo(resources.getString(R.string.workout_status),
                    resources.getString(R.string.delete_wo_question), true)
            } else {
                // Wod time hasn't passed
                if (workoutInfo!!.active == "Yes"){
                    // Suspend Workout
                    showCustomAlertDialogCancelWo(resources.getString(R.string.workout_status),
                        resources.getString(R.string.cancel_wo_question), false)
                } else {
                    // Delete Workout
                    showCustomAlertDialogCancelWo(resources.getString(R.string.workout_status),
                        resources.getString(R.string.delete_wo_question), true)
                }
            }

        }
        ivWorkoutCancelDelete.setOnLongClickListener {
            if (!checkWodTimePassed()){
                // Long click only applicable if the Workout time hasn't passed
                if (workoutInfo!!.active == "No"){
                    // Also only applicable to reverse Workout active from "No" to "Yes"
                    showCustomAlertDialogCancelWo(resources.getString(R.string.workout_status),
                        resources.getString(R.string.reactivate_wo), delete = false, reactivate = true)
                }
            }
            true
        }

        if (intent.hasExtra(Constants.DATE_MILLIS)) {
            val timeDateMillis = intent.getStringExtra(Constants.DATE_MILLIS)
            if (timeDateMillis != null) {
                showProgressDialog()
                FirestoreClass().loadWorkoutData(this, timeDateMillis)
            }
        }


        if (!isManagerOrTrainer()) { // User is an Athlete
            btn_reserve_workout.visibility = View.VISIBLE
        } else { // User is either Manager or Trainer
            btn_reserve_workout.visibility = View.GONE
        }

        btn_reserve_workout.setOnClickListener {
            if (!isManagerOrTrainer()) {
                // User is Athlete
                if (workoutInfo!!.active == "Yes") {
                    // Workout is Active
                    if (workoutInfo!!.athleteAttendees.size == workoutInfo!!.capacity) {
                        showCustomSnackBar(resources.getString(R.string.wod_capacity_reached), true)

                    } else {

                        if (isOnline(this)) {
                            // First, check the loggedIn user data
                            showProgressDialog()
                            FirestoreClass().loadUserData(this)

                        } else {
                            showCustomNotificationAlertDialog(
                                resources.getString(R.string.connectivity),
                                resources.getString(R.string.no_access_to_internet)
                            )
                        }
                    }
                } else {
                    // Workout is suspended
                    showCustomSnackBar(resources.getString(R.string.wod_is_suspended), true)
                }

            } else { // User is either Manager or Trainer
                if (attendeeSaveMode) { // Already in edit attending mode
                    // Reload attending list with alpha 0.5f
                    updateWorkoutData(true)

                } else { // Not in edit attending mode
                    // Reload attending list with alpha 1.0f
                    btn_reserve_workout.text = resources.getString(R.string.update)
                    attendeeSaveMode = true // Change it to saving mode

                    val adapter = AthleteReservationListAdapter(this, userAttendeeList,
                        isManagerOrTrainer() && checkWodTimePassed(), attendeeSaveMode)
                    rv_athlete_reservation_list.adapter = adapter
                    adapter.setOnClickListener(this@ViewWorkoutActivity)

                }

            }

        }

        currentTimestamp = getCurrentTimeCCS()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (updateData){
                    reloadWorkoutData()
                } else {
                    finish()
                }
            }
        })

    }

    // Returning from FirestoreClass().updateWorkoutActive
    fun displayWoActiveData(){
        hideProgressDialog()
        if (workoutInfo!!.active == "Yes"){
            ivWorkoutCancelDelete.setImageResource(R.drawable.grey_cancel_24)
            showCustomSnackBar(resources.getString(R.string.workout_activated), false)
        } else {
            ivWorkoutCancelDelete.setImageResource(R.drawable.black_delete_24)
            showCustomSnackBar(resources.getString(R.string.workout_suspended), true)
        }

        updateData = true
    }

    // Returning from FirestoreClass().loadWorkoutData(this, timeDateMillis)
    fun displayWorkoutData(workoutData: Workout) {
        workoutInfo = workoutData // workoutInfo updated

        tvViewWorkoutName.text = workoutData.description
        tvViewWorkoutTrainer.text = workoutData.trainerName
        tvViewWorkoutTime.text = workoutData.dateTime

        constructAttendingUserList()

        // Having the workout data, check if Attending button can be displayed
        if (checkWodTimePassed() && isManagerOrTrainer() && workoutInfo!!.athleteAttendees.size > 0){
            btn_reserve_workout.visibility = View.VISIBLE
            btn_reserve_workout.text = resources.getString(R.string.attending)
        }

        tvViewWorkoutDescription.text = workoutData.details

        tvViewWorkoutDate.text = "${workoutData.dateDay}/${workoutData.dateMonth}/${workoutData.dateYear}"

        if (workoutData.duration == 1) {
            tvViewWorkoutDuration.text =
                resources.getString(R.string.duration_hours, "${workoutData.duration}", "")
        } else {
            // more than one hour duration
            tvViewWorkoutDuration.text =
                resources.getString(R.string.duration_hours, "${workoutData.duration}", "s")
        }

        if (isManager()){ // Show the cancel / delete icon if Manager only
            if (checkWodTimePassedSharp()){
                // If the Wod time passed, you can delete only
                ivWorkoutCancelDelete.setImageResource(R.drawable.black_delete_24)
            } else {
                // If the Wod time has not passed, you can cancel and delete
                if (workoutData.active == "Yes") { // if the Workout active is "Yes"
                    ivWorkoutCancelDelete.setImageResource(R.drawable.grey_cancel_24)
                } else { // if the Workout active is "No", so it it ready to be deleted
                    ivWorkoutCancelDelete.setImageResource(R.drawable.black_delete_24)
                }
            }
            ivWorkoutCancelDelete.visibility = View.VISIBLE
        } else {
            ivWorkoutCancelDelete.visibility = View.INVISIBLE
        }

        // Display Workout notes

        // If WO is suspended, despite being Athlete, Manager or Trainer
        if (workoutInfo!!.active == "No") {

            cardViewNotes.visibility = View.VISIBLE
            ivTrainerNotePen.setImageResource(R.drawable.ic_red_warning_24)
            ivTrainerNotePen.visibility = View.VISIBLE
            rlUserNotes.visibility = View.GONE

        }  else {
        // WO is Active

            if (isManagerOrTrainer()) {
                // viewer is Manager or Trainer
                cardViewNotes.visibility = View.VISIBLE
                rlUserNotes.visibility = View.GONE
            } else {
                // viewer is Athlete

                // Check if athlete is subscribed
                var reservationMade = false
                for (attendee in workoutInfo!!.athleteAttendees) {
                    if (attendee.id == getCurrentUserId()) {
                        reservationMade = true
                    }
                }
                if (reservationMade) {
                    // viewer has reserved this workout
                    cardViewNotes.visibility = View.VISIBLE

                    if (workoutInfo!!.trainerNotes.isEmpty()) {
                        // As Athlete, if Trainer notes is empty, hide it
                        rlTrainerNotes.visibility = View.GONE
                    } else {
                        rlTrainerNotes.visibility = View.VISIBLE
                        ivTrainerNotePen.visibility = View.INVISIBLE
                    }
                } else {
                    // viewer han no reservation to this workout
                    cardViewNotes.visibility = View.GONE
                }
            }
        }

        // update notes at companion data level
        trainerNotesSaved = workoutInfo!!.trainerNotes
        userNotesSaved = workoutInfo!!.userNotes

    }

    // Send to FirestoreClass a list of attendees, and expect to receive a list of UserAttendee
    private fun constructAttendingUserList() {

        userAttendeeList.clear() // Clear userAttendeeList before initiating its construction
        if (workoutInfo!!.athleteAttendees.size > 0) {
            FirestoreClass().loadUserDataForAttendee(
                this, 0,
                workoutInfo!!.athleteAttendees
            )
        } else {
            showAttendees(userAttendeeList)
        }
    }

    fun userDataToBuildUserAttendee(userData: User, attended: String){
        val userAttendee = UserAttendee(userData.id, userData.name, userData.citizenId,
        userData.email, userData.role, userData.mobile, userData.image, userData.status, attended)
        userAttendeeList.add(userAttendee)
        if (userAttendeeList.size < workoutInfo!!.athleteAttendees.size){
            FirestoreClass().loadUserDataForAttendee(
                this, userAttendeeList.size, // count, or userAttendeeList index
                workoutInfo!!.athleteAttendees
            )
        } else {
            showAttendees(userAttendeeList)
        }
    }

    // Receive back from FirestoreClass the list of UserAttendee so we can display them
    private fun showAttendees(userAttendingList: ArrayList<UserAttendee>) {
        hideProgressDialog()
        if (workoutInfo!!.athleteAttendees.size > 0 ) {
            tv_view_workout_vacancies.text = "${workoutInfo!!.athleteAttendees.size}/${workoutInfo!!.capacity}"
            rv_athlete_reservation_list.layoutManager = LinearLayoutManager(this)
            rv_athlete_reservation_list.setHasFixedSize(true)

            val adapter = AthleteReservationListAdapter(this, userAttendingList,
                          isManagerOrTrainer() && checkWodTimePassed())
            adapter.setOnClickListener(this)
            rv_athlete_reservation_list.adapter = adapter

            for (user in workoutInfo!!.athleteAttendees) {
                if (user.id == getCurrentUserId()) {
                    btn_reserve_workout.text = resources.getString(R.string.cancel_reservation)
                    reserved = true
                }
            }
        } else {
            tv_view_workout_vacancies.text = "0/${workoutInfo!!.capacity}"
        }
    }

    fun reloadWorkoutData() {
        hideProgressDialog() // Hide progress dialog in case is showing
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun reserveWithThisUserData(userData: User) {
        hideProgressDialog()
        if (userData.status == "active") {

            if (checkWodTimePassed()) {

                showCustomSnackBar(resources.getString(R.string.not_possible_reserve), true)

            } else {
                if (!reserved) {  // if not already reserved

                    workoutInfo!!.athleteAttendees.add(Attendee(getCurrentUserId(), "No"))

                } else { // else, if it is already reserved
                    var i: Int? = null
                    for (y in workoutInfo!!.athleteAttendees.indices) {
                        if (workoutInfo!!.athleteAttendees[y].id == userData.id) // or getCurrentUserId()
                            i = y
                    }
                    if (i != null) {
                        workoutInfo!!.athleteAttendees.removeAt(i)
                    }
                }

                // after you updated the athleteAttendee list
                updateWorkoutData()
            }
        } else {
            showCustomSnackBar(resources.getString(R.string.user_is_inactive), true)
        }

    }

    private fun updateWorkoutData(updateAttending: Boolean = false) {
        val workoutHashMap = HashMap<String, Any>()
        workoutHashMap[Constants.ATHLETE_ATTENDEES] = workoutInfo!!.athleteAttendees

        showProgressDialog()
        FirestoreClass().updateWorkoutData(
            this,
            workoutInfo!!.timeCreatedMillis,
            workoutHashMap,
            updateAttending
        )
    }

    private fun updateWorkoutActive() {
        val workoutHashMap = HashMap<String, Any>()
        workoutHashMap[Constants.ACTIVE] = workoutInfo!!.active

        showProgressDialog()
        FirestoreClass().updateWorkoutActive(
            this,
            workoutInfo!!.timeCreatedMillis,
            workoutHashMap
        )
    }

    fun returnFromUpdatingAttending(){

        btn_reserve_workout.text = resources.getString(R.string.attending)
        attendeeSaveMode = false // Change it to not saving mode

        val adapter = AthleteReservationListAdapter(this, userAttendeeList,
            isManagerOrTrainer() && checkWodTimePassed(), attendeeSaveMode)
        rv_athlete_reservation_list.adapter = adapter
        adapter.setOnClickListener(this@ViewWorkoutActivity)

        hideProgressDialog()
    }

    private fun checkWodTimePassed(): Boolean {
        // Check if time and date passed
        timeCreatedMillisHourGap = workoutInfo!!.timeCreatedMillis.toLong() - 3600000 // minus one hour
        return currentTimestamp >= timeCreatedMillisHourGap

    }

    private fun checkWodTimePassedSharp(): Boolean {
        // Check if time and date passed
        timeCreatedMillisHourGap = workoutInfo!!.timeCreatedMillis.toLong()  // minus one hour
        return currentTimestamp >= timeCreatedMillisHourGap

    }

    private fun showCustomAlertDialogCancelWo(text1: String, text2: String, delete: Boolean = false,
                                                reactivate : Boolean = false){

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
            if (delete) {

                dialog.dismiss()
                showProgressDialog()
                FirestoreClass().deleteWorkout(this, workoutInfo!!.timeCreatedMillis)

            } else {
                dialog.dismiss()
                if (reactivate){
                    workoutInfo!!.active = "Yes"
                } else {
                    workoutInfo!!.active = "No"
                }
                updateWorkoutActive()
            }
        }
    }

    // Intent to WorkoutNotes with extras: isAthleteNote, workoutID in millis, and notes
    private fun jumpToWorkoutNotes(isAthleteNote: Boolean, workoutTimeStamp: String) {

        val intentToNote = Intent(this, WorkoutNotes::class.java)
        intentToNote.putExtra(Constants.ATHLETE, isAthleteNote)
        intentToNote.putExtra(Constants.ID, workoutTimeStamp)

        if (isAthleteNote) {
            var userNotes = ""
            // Gather the Athlete's notes
            for (notes in workoutInfo!!.userNotes){
                if (notes.userId == getCurrentUserId()){
                    userNotes = notes.notes
                }
            }
            intentToNote.putExtra(Constants.NOTES, userNotes)
        } else {
            intentToNote.putExtra(Constants.NOTES, workoutInfo!!.trainerNotes)
        }

        resultFromNotes.launch(intentToNote)

        //startActivity(intentToNote)

    }


    override fun onClickUser(userDataAttendee: UserAttendee) {
        if (isManagerOrTrainer()) {
            val userData = User(userDataAttendee.id, userDataAttendee.name,userDataAttendee.citizenId,
            userDataAttendee.email,userDataAttendee.role, userDataAttendee.mobile,
            userDataAttendee.image,userDataAttendee.status)

            val intentToUpdateUser = Intent(this, ProfileViewActivity::class.java)
            intentToUpdateUser.putExtra(Constants.USER_DATA, userData)
            startActivity(intentToUpdateUser)
        }
    }

    override fun onClickAttending(position: Int, attended: String) {
        if (attendeeSaveMode){
            userAttendeeList[position].attended = attended // Updated for adapter purposes
            workoutInfo!!.athleteAttendees[position].attended = attended // Updated for Firebase eventual update
        }
    }

}