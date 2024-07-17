package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.example.eppdraft1.R
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.UserNotes
import com.example.eppdraft1.main.utils.Constants

class WorkoutNotes : BaseActivity() {

    private lateinit var toolbarWorkoutNotes: Toolbar
    private var isNoteFromAthlete : Boolean = false
    private var workoutId: String = ""
    private var workoutNotes: String = ""

    private var unsavedData: Boolean = false

    private var myMenu: Menu? = null
    private lateinit var etNotes: EditText

    private var userNotesRetrieved: ArrayList<UserNotes> = ArrayList()

    private var notesTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            unsavedData = true
            if (myMenu != null) {
                val myMenuImage = myMenu!!.findItem(R.id.menu_item_save_workout_notes)
                myMenuImage.icon!!.alpha = 255
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_notes)

        if (intent.hasExtra(Constants.ATHLETE)) {
            isNoteFromAthlete = intent.getBooleanExtra(Constants.ATHLETE, false)
            workoutId = intent.getStringExtra(Constants.ID)!!
            workoutNotes = intent.getStringExtra(Constants.NOTES)!!
        }

        etNotes = findViewById(R.id.et_workout_notes)
        etNotes.addTextChangedListener(notesTextWatcher)
        etNotes.setText(workoutNotes)

        if (!isNoteFromAthlete && !isManagerOrTrainer()) {
            // If Athlete is viewing Trainers notes
            etNotes.isFocusable = false
            etNotes.isEnabled = false
            etNotes.setTextColor(Color.BLACK)
        } else {
            // Hide the soft keyboard initially
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }

        setupActionBar(isNoteFromAthlete)

        unsavedData = false

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                confirmLeaving()
            }
        })
    }

    private fun setupActionBar(isAthlete: Boolean = false) {
        toolbarWorkoutNotes = findViewById(R.id.toolbar_workout_notes_activity)
        setSupportActionBar(toolbarWorkoutNotes)
        toolbarWorkoutNotes.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbarWorkoutNotes.setNavigationOnClickListener {
            confirmLeaving()
        }
        if (isAthlete){
            toolbarWorkoutNotes.title = resources.getString(R.string.user_notes)
        } else {
            toolbarWorkoutNotes.title = resources.getString(R.string.trainer_notes)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // If is Manager or Trainer AND note from trainer
        // OR is Athlete AND note from athlete
        if ( (isManagerOrTrainer() && !isNoteFromAthlete) ||
            (!isManagerOrTrainer() && isNoteFromAthlete) ) {

            menuInflater.inflate(R.menu.menu_workout_notes, menu)
            myMenu = menu
            val myMenuImage = myMenu!!.findItem(R.id.menu_item_save_workout_notes)
            myMenuImage.icon!!.alpha = 128

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save_workout_notes -> {
                // save notes ONLY if unsavedData is true
                if (unsavedData){
                    updateWorkoutNotes()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmLeaving() {
        if (unsavedData){ // If data has been inserted, Dialog window to confirm leaving the activity
            displayCustomDialog(resources.getString(R.string.workout_notes),
                resources.getString(R.string.leave_without_saving))
        } else {
            finish()
        }
    }

    private fun displayCustomDialog(title: String, text2: String) {

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
                finish()
        }

    }

    private fun updateWorkoutNotes() {
        val workoutHashMap = HashMap<String, Any>()

        if (isNoteFromAthlete){
            // User notes edit
            userNotesRetrieved = ViewWorkoutActivity.userNotesSaved // retrieve user notes array
            // update userNotes array only for current id user
            // please notice that initially each user has no notes
            var foundRecord = false // If the userId has already a note recorded
            for (i in userNotesRetrieved.indices) {
                if (userNotesRetrieved[i].userId == getCurrentUserId()){
                    userNotesRetrieved[i].notes = etNotes.text.toString()
                    foundRecord = true
                }
            }
            if (!foundRecord){ // Otherwise, create a new note for this userId
                val newNote = UserNotes(getCurrentUserId(), etNotes.text.toString() )
                userNotesRetrieved.add(newNote)
            }

            workoutHashMap[Constants.USER_NOTES] = userNotesRetrieved

        } else {
            // Trainer notes edit
            workoutHashMap[Constants.TRAINER_NOTES] = etNotes.text.toString()

        }

        showProgressDialog()
        FirestoreClass().updateWorkoutNotes(this, workoutId, workoutHashMap)

    }

    // return from FirestoreClass.updateWorkoutNotes
    fun trainerNotesReturn(){
        hideProgressDialog()

        unsavedData = false
        val myMenuImage = myMenu!!.findItem(R.id.menu_item_save_workout_notes)
        myMenuImage.icon!!.alpha = 128

        // Save trainer or user notes at ViewWorkoutActivity companion level
        if (isNoteFromAthlete) {
            // User notes
            ViewWorkoutActivity.userNotesSaved = userNotesRetrieved
        } else {
            // Trainer notes
            ViewWorkoutActivity.trainerNotesSaved = etNotes.text.toString()
        }

        // Since notes were successfully modified and updated, setResult OK
        setResult(Activity.RESULT_OK)

    }


}