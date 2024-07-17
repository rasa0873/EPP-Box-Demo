package com.example.eppdraft1.main.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.FragmentPagerAllWoListAdapter
import com.example.eppdraft1.main.adapters.FragmentPagerWoListAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.fragments.AllWoListFragment1
import com.example.eppdraft1.main.fragments.AllWoListFragment2
import com.example.eppdraft1.main.fragments.MyWoListFragment1
import com.example.eppdraft1.main.models.Workout
import com.example.eppdraft1.main.utils.Constants
import com.google.android.material.tabs.TabLayout

class TabsWorkoutListActivity : BaseActivity(), AllWoListFragment1.OnClickListener, AllWoListFragment2.OnClickListener {

    // Tabs related *************************************
    private lateinit var toolbar: Toolbar
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapterTab: FragmentPagerAllWoListAdapter


    // Workout list related  ****************************
    private var newReservationMade: Boolean = false
    private lateinit var workoutData: Workout
    private var currentTimestamp : Long = 0


    // filter the original workoutList if wanted
    companion object {
        var completedWoList = ArrayList<Workout>()
        var pendingWoList = ArrayList<Workout>()
    }

    // When a workout is removed, the position in the recyclerView adapter list
    // Initially set to -1
    private var refreshListPosition : Int = -1

    // Remove of edit workout intent result
    private val removeWorkoutIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                refreshWorkoutLists()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs_workout_list)

        completedWoList.clear()
        pendingWoList.clear()

        // Toolbar setup
        setupActionBar()

        // Tabs fragments setup
        tabsSetup()

        // Initial Workout list load
        refreshWorkoutLists()


    }

    private fun setupActionBar(){
        toolbar = findViewById(R.id.toolbar_workout_list_tabs)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun tabsSetup() {

        viewPager2 = findViewById(R.id.viewPager2_workout_list)
        tabLayout = findViewById(R.id.tabLayout_workout_list)

        adapterTab = FragmentPagerAllWoListAdapter(supportFragmentManager, lifecycle, this, this )

        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.next_wo)))
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.completed)))

        viewPager2.adapter = adapterTab

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {

                hideSoftKeyboard(this@TabsWorkoutListActivity)

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

    private fun refreshWorkoutLists() {
        if (isProgressDialogNull())
            showProgressDialog()
        FirestoreClass().getWorkoutList(this, false)
    }

    fun populateAllWorkoutListToUI(workoutList: ArrayList<Workout>) {

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
        val fragment1AllWo = adapterTab.getFragmentAtPosition(0)
        if (fragment1AllWo != null && fragment1AllWo is AllWoListFragment1) {
            fragment1AllWo.refreshRecyclerView(refreshListPosition)
            if (pendingWoList.size > 0) { // There are pending Workouts
                fragment1AllWo.showTvNoWoAvailable(false)
                refreshListPosition = -1 // reset refreshListPosition after using it
            } else { // No pending Workouts
                fragment1AllWo.showTvNoWoAvailable(true)
            }
        }


        // Fragment 2 - Completed workout list
        val fragment2AllWo = adapterTab.getFragmentAtPosition(1)
        if (fragment2AllWo != null && fragment2AllWo is AllWoListFragment2){
            fragment2AllWo.refreshRecyclerView(refreshListPosition)
            if (completedWoList.size > 0) { // There are completed Workouts
                fragment2AllWo.showTvNoWoAvailable(false)
                refreshListPosition = -1 // reset refreshListPosition after using it
            } else { // No completed Workouts
                fragment2AllWo.showTvNoWoAvailable(true)
            }
        }

        hideProgressDialog()
    }

    override fun onClickToActivityFragment1(timeDateInMillisToActivity: String, pos: Int) {
        refreshListPosition = pos
        intentToViewWorkout(timeDateInMillisToActivity)
    }

    override fun onClickToActivityFragment2(timeDateInMillisToActivity: String, pos: Int) {
        refreshListPosition = pos
        intentToViewWorkout(timeDateInMillisToActivity)
    }

    private fun intentToViewWorkout(timeDateInMillisToActivity: String){
        val intentToWorkout = Intent(this, ViewWorkoutActivity::class.java)
        intentToWorkout.putExtra(Constants.DATE_MILLIS, timeDateInMillisToActivity)
        removeWorkoutIntentResult.launch(intentToWorkout)
    }
}