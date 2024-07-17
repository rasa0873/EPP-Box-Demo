package com.example.eppdraft1.main.adapters

import android.content.Context
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eppdraft1.main.fragments.AllWoListFragment1
import com.example.eppdraft1.main.fragments.AllWoListFragment2
import com.example.eppdraft1.main.fragments.MyWoListFragment1
import com.example.eppdraft1.main.fragments.MyWoListFragment2

class FragmentPagerAllWoListAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var interface1: AllWoListFragment1.OnClickListener,
    var interface2: AllWoListFragment2.OnClickListener
): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentInstances = SparseArray<Fragment>()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = if (position == 0){
            AllWoListFragment1(interface1)
        } else {
            AllWoListFragment2(interface2)
        }

        fragmentInstances.put(position, fragment) // Add the fragment to the array


        return fragment
    }

    fun getFragmentAtPosition(position: Int) : Fragment? {
        return fragmentInstances.get(position)
    }
}