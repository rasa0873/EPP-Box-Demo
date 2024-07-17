package com.example.eppdraft1.main.activities

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eppdraft1.R
import com.example.eppdraft1.main.fragments.ProfileMainFragment
import com.example.eppdraft1.main.fragments.ProfileScheduleFragment
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.utils.Constants
import kotlinx.android.synthetic.main.activity_profile_view.*
import kotlinx.android.synthetic.main.fragment_profile_main.*

class ProfileViewActivity : BaseActivity() {

    private lateinit var userInfo: User
    private var updatedUserProfile : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
           window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Retrieve userInfo from Portal
        if (intent.hasExtra(Constants.USER_DATA)){
            userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Constants.USER_DATA, User::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<User>(Constants.USER_DATA)!!
            }
        }

        // Call to display user pic and data
        if (this::userInfo.isInitialized) {
            displayUserData(userInfo)
        }

        iv_profile_edit_pen.setOnClickListener {
            if (isOnline(this)) {
                val intentToUpdateUser = Intent(this, ProfileEditActivity::class.java)
                intentToUpdateUser.putExtra(Constants.USER_DATA, userInfo)
                //val options = ActivityOptionsCompat.
                //makeSceneTransitionAnimation(this, iv_profile_view, "userPicture")
                //startActivity(intentToUpdateUser, options.toBundle())
                startActivity(intentToUpdateUser)
            } else {
                showCustomNotificationAlertDialog(resources.getString(R.string.connectivity),
                             resources.getString(R.string.no_access_to_internet))
            }

        }

        iv_profile_back_arrow.setOnClickListener {
            if (updatedUserProfile){
                setResult(Activity.RESULT_OK)
            }
            finish()
        }

        tv_profile_view_profile.setOnClickListener {
            switchFragment(ProfileMainFragment())
        }
        tv_profile_view_calendar.setOnClickListener {
            switchFragment(ProfileScheduleFragment())
        }

        // Initialize fragmentTransaction
        if (savedInstanceState == null)
            switchFragment(ProfileMainFragment())

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (updatedUserProfile){
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
        })

    }

    // Relevant when returning from ProfileEditActivity and PROFILE_UPDATED shared pref is "yes"
    override fun onStart() {
        super.onStart()
        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val profileUpdated : String? = sharedPreference.getString(Constants.PROFILE_UPDATED, "no")
        if (profileUpdated.equals("yes")){
            // Retrieve user profile data from shared preference
            userInfo.image = sharedPreference.getString(Constants.IMAGE, "").toString()
            userInfo.name = sharedPreference.getString(Constants.NAME, "").toString()
            userInfo.mobile = sharedPreference.getLong(Constants.MOBILE, 0)
            userInfo.role = sharedPreference.getString(Constants.ROLE,"").toString()

            updatedUserProfile = true
            displayUserData(userInfo)

            // Refresh main fragment
            switchFragment(ProfileMainFragment())


            // Set image shared pref updated to "no"
            val editor = sharedPreference.edit()
            editor.putString(Constants.PROFILE_UPDATED, "no")
            editor.apply()
        }
    }


    private fun displayUserData(userInfo: User){
        this.userInfo = userInfo
        Glide
            .with(this)
            .load(userInfo.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.person_128)
            .into(iv_profile_view)

        tv_profile_view_head_email.text = userInfo.email

        checkOwnUser(userInfo.email)
    }

    private fun switchFragment(fragment : Fragment){
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = fragment

        val mBundle = Bundle()
        mBundle.putString("userNameToFragment", userInfo.name)
        mBundle.putString("userCitizenIdToFragment", userInfo.citizenId)
        mBundle.putString("userMobileToFragment", userInfo.mobile.toString())
        mBundle.putString("userRoleToFragment", userInfo.role)
        mFragment.arguments = mBundle
        mFragmentTransaction.replace(R.id.profile_fragment_container, mFragment) // mFragment indicated via function parameter
        mFragmentTransaction.commit()

        if (fragment is ProfileMainFragment){
            tv_profile_view_profile.alpha = 1.0f
            tv_profile_view_calendar.alpha = 0.6f

        } else {
            tv_profile_view_profile.alpha = 0.6f
            tv_profile_view_calendar.alpha = 1.0f

        }
    }

    private fun checkOwnUser(email: String)  {

        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val ownEmailSharedPref : String? = sharedPreference.getString(Constants.EMAIL, "")
        val ownRoleSharedPref : String? = sharedPreference.getString(Constants.ROLE, Constants.ATHLETE)

        if (ownEmailSharedPref == email){
            // Own profile
            iv_profile_edit_pen.visibility = View.VISIBLE
            if (ownRoleSharedPref == Constants.MANAGER){
                // You are Manager
                ll_profile_view_options.visibility = View.GONE
            }
        } else {
            // Other's profile
            iv_profile_edit_pen.visibility = View.INVISIBLE
        }

    }



}