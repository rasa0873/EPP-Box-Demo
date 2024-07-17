package com.example.eppdraft1.main.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.eppdraft1.R
import kotlinx.android.synthetic.main.activity_passwd_update_result.*
import kotlinx.android.synthetic.main.activity_password_update.*

class PasswordUpdateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_update)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        toolbar_passwd_edit_activity.visibility = View.VISIBLE
        ll_update_passwd_heading.visibility = View.VISIBLE
        cv_update_passwd.visibility = View.VISIBLE
        ll_update_pass_result.visibility = View.GONE

        btn_update_passwd.setOnClickListener {
            passwdUpdate()
        }

        btn_update_passwd_result.setOnClickListener {
            finish()
        }


    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_passwd_edit_activity)
        toolbar_passwd_edit_activity.setNavigationIcon(R.drawable.black_arrow_back_ios_24)
        toolbar_passwd_edit_activity.setNavigationOnClickListener {
            finish()
        }
    }

    private fun passwdUpdate(){
        val passwdText1 = et_passwd_update1.text.toString()
        val passwdText2 = et_passwd_update2.text.toString()
        if (validateForm(passwdText1, passwdText2)){
            showProgressDialog()
            updatePassword(this, passwdText1)
        }
    }


    private fun validateForm(passwd1: String, passwd2: String): Boolean {
        if (passwd1.length == passwd2.length){
            if (passwd1.length >= 6){
                return true
            } else {
                showCustomSnackBar(resources.getString(R.string.passwd_length), true)
            }
        } else {
            showCustomSnackBar(resources.getString(R.string.verify_passwd), true)
        }
        return false
    }

    fun passwordUpdated(){
        hideProgressDialog()

        toolbar_passwd_edit_activity.visibility = View.GONE
        ll_update_passwd_heading.visibility = View.GONE
        cv_update_passwd.visibility = View.GONE

        ll_update_pass_result.visibility = View.VISIBLE
    }


}