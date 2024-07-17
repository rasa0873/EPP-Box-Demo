package com.example.eppdraft1.main.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.example.eppdraft1.R
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_create_profile_fab.*
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val eppRoles = arrayOf(Constants.MANAGER, Constants.TRAINER, Constants.ATHLETE)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, eppRoles)
        autocomplete_roles.setAdapter(adapter)

        fab_create_profile.setOnClickListener {
            registerUser()
        }

    }

    fun userRegisteredSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()

    }

    private fun registerUser(){
        val name = et_name_create_user.text.toString().trim(){it <= ' '}
        val citizenId = et_citizenId_create_user.text.toString().trim(){it <= ' '}
        val passwd = et_password_create_user.text.toString().trim(){it <= ' '}
        val email = et_email_create_user.text.toString().trim(){it <= ' '}
        val mobile = et_phone_create_user.text.toString().trim(){it <= ' '}
        val mobileLong : Long = if (mobile.isNotEmpty()){
            mobile.toLong()
        } else {
            0
        }
        val role = autocomplete_roles.text.toString().trim(){it <= ' '}

        if (validateForm(name, citizenId, passwd, email, role)){
            showProgressDialog()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, passwd).addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, citizenId, registeredEmail, role, mobileLong)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        hideProgressDialog()
                        val exceptionError = task.exception!!.message.toString()
                        showCustomSnackBar(resources.getString(R.string.error_while_regist_user, exceptionError), true)
                    }
                }
        }

    }

    private fun validateForm(name: String, citizenId: String, passwd: String,
                             email: String, role: String) : Boolean {
        return when{
            TextUtils.isEmpty(name) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(citizenId) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(passwd) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(email) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            }
            TextUtils.isEmpty(role) -> {
                showCustomSnackBar(resources.getString(R.string.please_fill_out), true)
                false
            } else -> {
                true
            }
        }
    }


}