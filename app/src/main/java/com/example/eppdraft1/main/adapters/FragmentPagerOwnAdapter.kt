package com.example.eppdraft1.main.adapters

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eppdraft1.main.fragments.CreateWorkoutFragment1
import com.example.eppdraft1.main.fragments.CreateWorkoutFragment2

class FragmentPagerOwnAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var myInterface: CreateWorkoutFragment1.OnTextEnteredListener,
    private var myInterface2: CreateWorkoutFragment2.OnDateModifiedListener
): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentInstances = SparseArray<Fragment>()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = if (position == 0)
            CreateWorkoutFragment1(myInterface)
        else
            CreateWorkoutFragment2(myInterface2)

        fragmentInstances.put(position, fragment) // Add the fragment to the array


        return fragment
    }

    fun getFragmentAtPosition(position: Int) : Fragment? {
        return fragmentInstances.get(position)
    }


}