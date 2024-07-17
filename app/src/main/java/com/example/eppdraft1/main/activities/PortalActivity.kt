package com.example.eppdraft1.main.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eppdraft1.R
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_portal.*
import kotlinx.android.synthetic.main.app_bar_portal.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.portal_main_layout.*
import java.util.Calendar
import java.util.TimeZone

class PortalActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userInfo: User
    private var reloadUser  = false

    // Create profile intent result
    // is Activity.RESULT_OK, we returned from CreateUserActivity with the newly created user logged in
    // and without progress dialog showing
    private val createUserIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                reloadUser = true

                //showCustomSnackBar("Profile successfully added", false)
                Log.i(Constants.TAG, "Current userId after adding a new one is: ${getCurrentUserId()}")

                // Logout and log in again with previous user email and pass, saved in sharedPref
                FirebaseAuth.getInstance().signOut()

                // Sign the newly created user out
                val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
                val sharedPrefEmail : String? = sharedPreference.getString(Constants.LOGGED_EMAIL, "")
                val sharedPrefPass : String? = sharedPreference.getString(Constants.PASSWORD, "")

                val auth : FirebaseAuth = Firebase.auth
                auth.signInWithEmailAndPassword(sharedPrefEmail!!, sharedPrefPass!!)
                    .addOnCompleteListener(this){ task ->
                        if (task.isSuccessful) {
                            reloadUserData()
                        } else {
                            Log.w(Constants.TAG, "signInWithEmail:failure", task.exception)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }

            }
        }

    // Update profile intent result
    private val updateUserIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                reloadUserData()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portal)

        setupActionBar()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.menu.getItem(1).setActionView(R.layout.drawer_menu_icon) // Profile
        nav_view.menu.getItem(2).setActionView(R.layout.drawer_menu_icon) // Add User
        nav_view.menu.getItem(3).setActionView(R.layout.drawer_menu_icon) // My Workouts
        nav_view.menu.getItem(4).setActionView(R.layout.drawer_menu_icon) // All Workouts

        //drawer_layout.setStatusBarBackgroundColor(resources.getColor(R.color.dark_gray1)) // For Nav Drawer Over status bar as transparent
        drawer_layout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray1))

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (isDrawerOpen()){
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })

        val linearLayoutPortalCalendar : LinearLayout = findViewById(R.id.ll_portal_classes)
        linearLayoutPortalCalendar.setOnClickListener {
            checkCurrentDate()
            val tempIntentToCalendar = Intent(this, WeekCalendarView::class.java)
            startActivity(tempIntentToCalendar)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val linearLayoutPortalTrainers : LinearLayout = findViewById(R.id.ll_portal_trainers)
        linearLayoutPortalTrainers.setOnClickListener {
            // Intent to Trainers profiles
            val intentToTrainers = Intent(this, TrainersProfileActivity::class.java)
            startActivity(intentToTrainers)
        }

        val linearLayoutPortalPlans: LinearLayout = findViewById(R.id.ll_portal_plans)
        linearLayoutPortalPlans.setOnClickListener {
            // Intent to payment plans
            val intentToPlans = Intent(this, PlansPortalActivity::class.java)
            startActivity(intentToPlans)
        }

        val linearLayoutPortalAthletes : LinearLayout = findViewById(R.id.ll_portal_athletes)
        linearLayoutPortalAthletes.setOnClickListener {
            // Intent to Athlete list
            val intentToAthleteList = Intent(this, AthleteListActivity::class.java)
            startActivity(intentToAthleteList)
        }

        reloadUserData()

    }

    fun updateNavigationUserDetails(userInfo: User) {
        this.userInfo = userInfo

        Glide
            .with(this)
            .load(userInfo.image) // url to the user image
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.person_128)
            .into(nav_user_image)

        tv_drawer_header_username.text = userInfo.name
        tv_drawer_header_email.text = userInfo.email

        updateSharedPref(userInfo)

        FirestoreClass().getAthletesListCount(this)

        if (isManager()) {
            nav_view.menu.getItem(3).isVisible = false // Hide My Workouts
        } else {
            nav_view.menu.getItem(2).isVisible = false // Hide Add User
            nav_view.menu.getItem(4).isVisible = false // Hide All Workouts
        }
        
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_portal_activity)
        toolbar_portal_activity.setNavigationIcon(R.drawable.ic_nav_menu_icon_white)
        toolbar_portal_activity.setNavigationOnClickListener {
            if (!isDrawerOpen()){
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun isDrawerOpen() : Boolean {
        return drawer_layout.isDrawerOpen(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                val intentToUpdateUser = Intent(this, ProfileViewActivity::class.java)
                intentToUpdateUser.putExtra(Constants.USER_DATA, userInfo)
                updateUserIntentResult.launch(intentToUpdateUser)
            }
            R.id.nav_add_profile -> {
                if (isOnline(this)) {
                    Log.i(Constants.TAG, "Clicked on Add New Profile")
                    val intentToCreateUser = Intent(this, CreateUserActivity::class.java)
                    createUserIntentResult.launch(intentToCreateUser)
                } else {
                    showCustomNotificationAlertDialog(resources.getString(R.string.connectivity),
                        resources.getString(R.string.no_access_to_internet))
                }
            }
            R.id.nav_my_workouts -> {
                val intentToMyWorkouts = Intent(this, MyTabsWorkoutListActivity::class.java)
                intentToMyWorkouts.putExtra(Constants.MY_LIST, true)
                startActivity(intentToMyWorkouts)
            }
            R.id.nav_all_workouts -> {
                //val intentToAllWorkouts = Intent(this, WorkoutListActivity::class.java)
                val intentToAllWorkouts = Intent(this, TabsWorkoutListActivity::class.java)
                intentToAllWorkouts.putExtra(Constants.MY_LIST, false)
                startActivity(intentToAllWorkouts)
            }
            R.id.nav_sign_out -> {
                Log.i(Constants.TAG, "Clicked on Log out")
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun showAthleteListSize(athleteListSize : Int) {
        hideProgressDialog()
        tv_portal_athlete_count.text = athleteListSize.toString()

        if (reloadUser){
            // After the entire reload user data finishes, show the snackBar for
            // successful user creation
            showCustomSnackBar(resources.getString(R.string.profile_created), false)
            reloadUser = false
        }
    }

    private fun updateSharedPref(userData: User){

        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Constants.EMAIL, userData.email)
        editor.putString(Constants.NAME, userData.name)
        editor.putString(Constants.IMAGE, userData.image)
        editor.putString(Constants.CITIZEN_ID, userData.citizenId)
        editor.putLong(Constants.MOBILE, userData.mobile)
        editor.putString(Constants.ROLE, userData.role)
        editor.apply()
    }

    private fun checkCurrentDate(){

        val timeZone = TimeZone.getTimeZone("America/Caracas")
        val calendar = Calendar.getInstance(timeZone)
        val timeInMillis = calendar.timeInMillis // Long
        Log.i(Constants.TAG, "Date -4:00UTC in millis is: $timeInMillis")
    }


    private fun reloadUserData() {
        showProgressDialog()
        FirestoreClass().loadUserData(this)

    }


}