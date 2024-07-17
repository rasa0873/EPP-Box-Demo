package com.example.eppdraft1.main.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.example.eppdraft1.R
import com.example.eppdraft1.main.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        // Initialize auth
        auth = Firebase.auth

        btn_sign_in.setOnClickListener {
            if (isOnline(this)) {
                signInUser()
            } else {
                showCustomNotificationAlertDialog(resources.getString(R.string.connectivity) ,
                    resources.getString(R.string.no_access_to_internet))
            }
        }

        // Check if there is already a user UID
        val currentUID: String = getCurrentUserId()
        Log.i("ETIQUETA","currentUID is: $currentUID")
        if (currentUID.isNotEmpty()) {
            //showCustomSnackBar("Welcome ${getCurrentUserEmail()}", false)
            startActivity(Intent(this, PortalActivity::class.java))
            finish()
        }


    }

    private fun signInUser() {
        val email = et_email_sign_in.text.toString().trim{it <= ' '}
        val passwd = et_password_sign_in.text.toString().trim{it <= ' '}

        if (validateForm(email, passwd)){
            showProgressDialog()
            auth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.i(Constants.TAG, "signInWithEmail:success")

                        saveMailAndPassword(email, passwd)

                        startActivity(Intent(this, PortalActivity::class.java))
                        finish()

                    } else {
                        Log.w(Constants.TAG, "signInWithEmail:failure", task.exception)
                        showCustomSnackBar(resources.getString(R.string.unsuccessful_signin), true)
                    }
                    hideProgressDialog()
                }
        }

    }



    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showCustomSnackBar(resources.getString(R.string.enter_email), true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showCustomSnackBar(resources.getString(R.string.enter_passwd), true)
                false
            } else -> {
                true
            }
        }

    }

    private fun saveMailAndPassword(email: String, pass: String){
        val sharedPreferences = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.LOGGED_EMAIL, email)
        editor.putString(Constants.PASSWORD, pass)
        editor.apply()
    }

}