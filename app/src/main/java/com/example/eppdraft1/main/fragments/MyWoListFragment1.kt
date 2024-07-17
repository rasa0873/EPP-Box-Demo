package com.example.eppdraft1.main.fragments

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.activities.BaseActivity
import com.example.eppdraft1.main.activities.MyTabsWorkoutListActivity
import com.example.eppdraft1.main.adapters.MyWorkoutListAdapter
import com.example.eppdraft1.main.adapters.MyWorkoutListAdapterForFragment
import com.example.eppdraft1.main.models.Workout

class MyWoListFragment1(private val fromActivity: OnClickListener ) : Fragment(),
    MyWorkoutListAdapterForFragment.OnClickListener {

    private lateinit var rvWorkoutListFragment1 : RecyclerView
    private lateinit var tvNoWorkoutAvailable : TextView

    private var onClickListener : OnClickListener? = null

    private lateinit var workoutListAdapter : MyWorkoutListAdapterForFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_my_workout_list1, container, false)

        rvWorkoutListFragment1 = view.findViewById(R.id.rv_workout_list_fragment1)

        tvNoWorkoutAvailable = view.findViewById(R.id.tv_no_workout_available_fragment1)
        tvNoWorkoutAvailable.visibility = View.GONE

        // Initialize Interface
        onClickListener = fromActivity

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initial Adapter and RV setup
        setupAdapter()
    }

    private fun setupAdapter(){
        // Adapter and RV setup
        workoutListAdapter = context?.let {
            MyWorkoutListAdapterForFragment(
                it,
                MyTabsWorkoutListActivity.pendingWoList,
                BaseActivity().getCurrentTimeCCS())
        }!!
        workoutListAdapter.setOnClickListener(this)

        rvWorkoutListFragment1.layoutManager = LinearLayoutManager(context)
        rvWorkoutListFragment1.adapter = workoutListAdapter
    }


    fun showTvNoWoAvailable(show: Boolean){
        if (show) {
            tvNoWorkoutAvailable.visibility = View.VISIBLE
        } else {
            tvNoWorkoutAvailable.visibility = View.GONE
        }
    }


    fun refreshRecyclerView(pos: Int = -1) {

        setupAdapter()
        /*
        if (pos != -1) {
            workoutListAdapter.notifyItemRemoved(pos)
        } else {
            workoutListAdapter.notifyDataSetChanged()
        }
         */

    }

    // onClick from MyWorkoutListAdapterForFragment adapter
    override fun onClick(timeDateInMillis: String, posFromAdapter: Int) {
        Log.i(com.example.eppdraft1.main.utils.Constants.TAG, "Fragment notified")
        if (onClickListener!=null){
            onClickListener!!.onClickToActivityFragment1(timeDateInMillis, posFromAdapter)
        }
    }

    // Interface definition and initialization
    interface OnClickListener {
        fun onClickToActivityFragment1(timeDateInMillisToActivity: String, pos: Int = 0)
    }


}