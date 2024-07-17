package com.example.eppdraft1.main.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.eppdraft1.R
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.Plan
import com.example.eppdraft1.main.utils.Constants
import kotlinx.android.synthetic.main.activity_portal.*
import kotlin.math.absoluteValue
import kotlin.random.Random

class PlansPortalActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans_portal)

        setupActionBar()

        val ivPlansCheckIcon1 : ImageView = findViewById(R.id.iv_check_icon_plan1)
        ivPlansCheckIcon1.visibility = View.INVISIBLE
        val ivPlansCheckIcon2 : ImageView = findViewById(R.id.iv_check_icon_plan2)
        ivPlansCheckIcon2.visibility = View.INVISIBLE
        
        val cvPlans1 : CardView = findViewById(R.id.cv_plans_1)
        cvPlans1.setOnClickListener {
            if (!isManagerOrTrainer()) {
                if (ivPlansCheckIcon1.visibility == View.VISIBLE) {
                    ivPlansCheckIcon1.visibility = View.INVISIBLE
                } else {
                    ivPlansCheckIcon1.visibility = View.VISIBLE
                    ivPlansCheckIcon2.visibility = View.INVISIBLE
                }
            }
        }

        val cvPlans2 : CardView = findViewById(R.id.cv_plans_2)
        cvPlans2.setOnClickListener {
            if (!isManagerOrTrainer()) {
                if (ivPlansCheckIcon2.visibility == View.VISIBLE) {
                    ivPlansCheckIcon2.visibility = View.INVISIBLE
                } else {
                    ivPlansCheckIcon2.visibility = View.VISIBLE
                    ivPlansCheckIcon1.visibility = View.INVISIBLE
                }
            }
        }

        // One-time creating Plan
        val randomStringNumber = Random.nextInt().absoluteValue.toString()
        //createPlan(Plan(randomStringNumber,"Plan Basico", 25f, "Mes"))

    }

    private fun setupActionBar(){

        val toolbar: Toolbar = findViewById(R.id.toolbar_portal_plans_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.white_arrow_back_ios_24)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.run {
            finish()
        }
        return true
    }

    private fun createPlan(plan: Plan){
        showProgressDialog()
        FirestoreClass().registerPlan(this, plan)

    }

    fun planCreated() {
        Log.i(Constants.TAG, "Plan created")
        Toast.makeText(this, "Plan Created", Toast.LENGTH_SHORT).show()
        hideProgressDialog()
    }
}