package com.example.eppdraft1.main.activities

import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.adapters.AthleteListAdapter
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.utils.Constants
import kotlinx.android.synthetic.main.activity_athlete_list.*
import kotlinx.android.synthetic.main.activity_athlete_list_recycler.*
import kotlinx.android.synthetic.main.activity_workout_list_recycler.*

class AthleteListActivity : BaseActivity(), AthleteListAdapter.OnClickListener {

    private var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_list)

        setupActionBar()

        showProgressBarAthleteStandAlone(true, showError = false)
        rv_athlete_list.visibility = View.GONE
        tv_no_athletes_available.visibility = View.GONE

        FirestoreClass().getAthletesList(this)

        recyclerView  = findViewById(R.id.rv_athlete_list)

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_athletes_list_activity)
        toolbar_athletes_list_activity.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar_athletes_list_activity.setNavigationOnClickListener {
            finish()
        }
    }

    fun populateAthleteListToUI(athleteList: ArrayList<User>) {

        //hideProgressDialogLight()
        showProgressBarAthleteStandAlone(false, showError = false)

        if (athleteList.size > 0){
            tv_no_athletes_available.visibility = View.GONE
            rv_athlete_list.visibility = View.VISIBLE

            rv_athlete_list.layoutManager =LinearLayoutManager(this)
            rv_athlete_list.setHasFixedSize(true)

            val adapter = AthleteListAdapter(this, athleteList, isManager())
            adapter.setOnClickListener(this)
            rv_athlete_list.adapter = adapter

        } else {
            tv_no_athletes_available.visibility = View.VISIBLE
            rv_athlete_list.visibility = View.GONE
        }
    }

    override fun onClick(userData: User) {

        val intentToSeeUpdateUser = Intent(this, ProfileViewActivity::class.java)
        intentToSeeUpdateUser.putExtra(Constants.USER_DATA, userData)
        startActivity(intentToSeeUpdateUser)

    }

    override fun onClick2(userData: User) {
        Toast.makeText(this, "Click on item check", Toast.LENGTH_SHORT).show()
    }

    fun showProgressBarAthleteStandAlone(show: Boolean, showError: Boolean){
        if (show){
            progressBarAthleteStandAlone.visibility = View.VISIBLE
        } else {
            progressBarAthleteStandAlone.visibility = View.GONE
        }

        if (showError){
            Toast.makeText(this, resources.getString(R.string.error_loading_athletes),
                Toast.LENGTH_LONG)
                .show()
        }
    }


}