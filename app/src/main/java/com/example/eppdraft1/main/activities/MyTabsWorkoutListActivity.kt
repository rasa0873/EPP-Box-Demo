package com.example.eppdraft1.main.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.FragmentPagerWoListAdapter
import com.example.eppdraft1.main.adapters.MyWorkoutListAdapterForFragment
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.fragments.MyWoListFragment1
import com.example.eppdraft1.main.fragments.MyWoListFragment2
import com.example.eppdraft1.main.models.Workout
import com.example.eppdraft1.main.utils.Constants
import com.google.android.material.tabs.TabLayout

class MyTabsWorkoutListActivity : BaseActivity(), MyWoListFragment1.OnClickListener,
                                                    MyWoListFragment2.OnClickListener{


    // Tabs related *************************************
    private lateinit var toolbar: Toolbar
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapterTab: FragmentPagerWoListAdapter


    // Workout list related  ****************************
    private var newReservationMade: Boolean = false
    private lateinit var workoutData: Workout
    private var currentTimestamp : Long = 0


    // filter the original workoutList if wanted
    companion object {
        var completedWoList = ArrayList<Workout>()
        var pendingWoList = ArrayList<Workout>()
    }

    // When reservation to a workout is removed, the position in the recyclerView adapter list
    // Initially set to -1
    private var refreshListPosition : Int = -1

    private var myList : Boolean = false

    // Reserve workout intent result
    private val reserveWorkoutIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                refreshWorkoutLists()
            }
        }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tabs_workout_list)

        completedWoList.clear()
        pendingWoList.clear()

        if (intent.hasExtra(Constants.MY_LIST)){
            myList = intent.getBooleanExtra(Constants.MY_LIST, false)
        }

        // Toolbar setup
        setupActionBar()

        // Tabs fragments setup
        tabsSetup()

        // Initial Workout list load
        refreshWorkoutLists()

    }

    private fun tabsSetup() {

        viewPager2 = findViewById(R.id.viewPager2_my_workout_list)
        tabLayout = findViewById(R.id.tabLayout_my_workout_list)

        adapterTab = FragmentPagerWoListAdapter(supportFragmentManager, lifecycle, this, this)

        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.next_wo)))
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.completed)))

        viewPager2.adapter = adapterTab

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {

                hideSoftKeyboard(this@MyTabsWorkoutListActivity)

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
    }

    private fun setupActionBar(){
        toolbar = findViewById(R.id.toolbar_my_workout_list_tabs)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun populateWorkoutListToUI(workoutList: ArrayList<Workout>) {

        completedWoList.clear()
        pendingWoList.clear()

        // Get the current time and date in millis Long
        for (workout in workoutList) {
            if (workout.timeCreatedMillis.toLong() >= getCurrentTimeCCS()){
                pendingWoList.add(workout)
            } else {
                completedWoList.add(workout)
            }
        }

        // Fragment 1 - Pending workout list
        val fragment1 = adapterTab.getFragmentAtPosition(0)
        if (fragment1 is MyWoListFragment1) {
            fragment1.refreshRecyclerView(refreshListPosition)
            if (pendingWoList.size > 0) { // There are pending Workouts
                fragment1.showTvNoWoAvailable(false)
                refreshListPosition = -1 // reset refreshListPosition after using it
            } else { // No pending Workouts
                fragment1.showTvNoWoAvailable(true)
            }
        }

        hideProgressDialog()
    }

    private fun refreshWorkoutLists() {
        if (isProgressDialogNull())
            showProgressDialog()
        FirestoreClass().getWorkoutList(this, myList)
    }

    // onClick at MyWoListFragment1
    override fun onClickToActivityFragment1(timeDateInMillisToActivity: String, pos: Int) {
        refreshListPosition = pos
        intentToViewWorkout(timeDateInMillisToActivity)

    }

    // onClick at MyWoListFragment2
    override fun onClickToActivityFragment2(timeDateInMillisToActivity: String) {
        intentToViewWorkout(timeDateInMillisToActivity)
    }

    private fun intentToViewWorkout(timeDateInMillisToActivity: String){
        val intentReserveWorkout = Intent(this, ViewWorkoutActivity::class.java)
        intentReserveWorkout.putExtra(Constants.DATE_MILLIS, timeDateInMillisToActivity)
        reserveWorkoutIntentResult.launch(intentReserveWorkout)
    }


}