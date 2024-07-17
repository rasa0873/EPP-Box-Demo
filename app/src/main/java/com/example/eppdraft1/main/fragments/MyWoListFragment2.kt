package com.example.eppdraft1.main.fragments

import android.os.Bundle
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
import com.example.eppdraft1.main.activities.TabsWorkoutListActivity
import com.example.eppdraft1.main.adapters.MyWorkoutListAdapterForFragment
import com.example.eppdraft1.main.adapters.WorkoutListAdapterForFragment

class MyWoListFragment2(private val fromActivity: OnClickListener) : Fragment(),
    MyWorkoutListAdapterForFragment.OnClickListener {

    private lateinit var rvWorkoutListFragment2 : RecyclerView
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
        val view : View = inflater.inflate(R.layout.fragment_my_workout_list2, container, false)

        rvWorkoutListFragment2 = view.findViewById(R.id.rv_workout_list_fragment2)

        tvNoWorkoutAvailable = view.findViewById(R.id.tv_no_workout_available_fragment2)
        tvNoWorkoutAvailable.visibility = View.GONE

        // Initialize Interface
        onClickListener = fromActivity

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initial Adapter and RV setup
        workoutListAdapter = context?.let {
            MyWorkoutListAdapterForFragment(
                it,
                MyTabsWorkoutListActivity.completedWoList,
                BaseActivity().getCurrentTimeCCS())
        }!!
        workoutListAdapter.setOnClickListener(this)

        rvWorkoutListFragment2.layoutManager = LinearLayoutManager(context)
        rvWorkoutListFragment2.adapter = workoutListAdapter

        if (MyTabsWorkoutListActivity.completedWoList.size > 0){
            showTvNoWoAvailable(false)
        } else {
            showTvNoWoAvailable(true)
        }
    }


    private fun showTvNoWoAvailable(show: Boolean){
        if (show) {
            tvNoWorkoutAvailable.visibility = View.VISIBLE
        } else {
            tvNoWorkoutAvailable.visibility = View.GONE
        }
    }




    override fun onClick(timeDateInMillis: String, posFromAdapter: Int) {
        Log.i(com.example.eppdraft1.main.utils.Constants.TAG, "Fragment2 notified")
        if (onClickListener!=null){
            onClickListener!!.onClickToActivityFragment2(timeDateInMillis)
        }
    }

    interface OnClickListener{
        fun onClickToActivityFragment2(timeDateInMillisToActivity: String)
    }

}