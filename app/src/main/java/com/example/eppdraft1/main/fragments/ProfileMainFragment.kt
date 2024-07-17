package com.example.eppdraft1.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.eppdraft1.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileMainFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var etName : EditText
    private lateinit var etCitizenId : EditText
    private lateinit var etMobile : EditText
    private lateinit var etRole : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile_main, container, false)

        val bundle = arguments
        val userName = bundle!!.getString("userNameToFragment")
        val userCitizenId = bundle.getString("userCitizenIdToFragment")
        val userMobile = bundle.getString("userMobileToFragment")
        val userRole = bundle.getString("userRoleToFragment")

        etName = view.findViewById(R.id.et_profile_main_fragment_name)
        etName.setText(userName)

        etCitizenId = view.findViewById(R.id.et_profile_main_fragment_id)
        etCitizenId.setText(userCitizenId)

        etMobile = view.findViewById(R.id.et_profile_main_fragment_phone)
        etMobile.setText(userMobile)

        etRole = view.findViewById(R.id.et_profile_main_fragment_role)
        etRole.setText(userRole)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileMainFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}