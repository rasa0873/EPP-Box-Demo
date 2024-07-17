package com.example.eppdraft1.main.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eppdraft1.R
import com.example.eppdraft1.main.firebase.FirestoreClass
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_portal.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_fab.*
import kotlinx.android.synthetic.main.activity_profile_view.*

class ProfileEditActivity : BaseActivity() {

    private lateinit var userInfo: User
    private var mSelectedImageFileLocalUri : Uri? = null
    private var mImageLocalCopy : String = ""
    private var mProfileImageUri = "" // set after new image is uploaded successfully
    private var changesMade : Boolean = false

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1

    }


    // Gallery intent result for image chooser
    private val galleryIntentResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK && it.data!!.data != null){
                mSelectedImageFileLocalUri = it.data!!.data
                userInfo.image = mSelectedImageFileLocalUri.toString()
                mImageLocalCopy = userInfo.image

                try {
                    displayUserData(userInfo)
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this@ProfileEditActivity, R.color.epp_primary)
                    changesMade = true
                } catch (e: Exception){
                    e.printStackTrace()
                    Log.e(Constants.TAG, "Error when displaying image", e)
                    Toast.makeText(this, "Error while displaying the selected image",
                        Toast.LENGTH_LONG).show()
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this, R.color.light_gray3)
                    changesMade = false
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


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

        /* Animated profile image kotlin code
        ViewCompat.setTransitionName(iv_profile_edit, "userPicture")
        supportPostponeEnterTransition()
        iv_profile_edit.post {
            supportStartPostponedEnterTransition()
        }
         */

        fab_activity_profile.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.light_gray3)

        fab_activity_profile.setOnClickListener {
            //showCustomSnackBar(resources.getString(R.string.data_saved), false)
            if (mSelectedImageFileLocalUri != null) {
                // Therefore, there is a new image to upload
                uploadUserImage()
            } else {
                updateUserProfileData()
            }
        }

        iv_profile_edit.setOnClickListener {
            showBottomDialog()
        }
        iv_profile_edit_camera_icon.setOnClickListener {
            showBottomDialog()
        }

        if (intent.hasExtra(Constants.USER_DATA)){
            userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Constants.USER_DATA, User::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<User>(Constants.USER_DATA)!!
            }
        }

        if (this::userInfo.isInitialized) {
            displayUserData(userInfo)
        }

        // initially set shared pref profile_updated to "no"
        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Constants.PROFILE_UPDATED, "no")
        editor.apply()


        text_input_autocomplete_role.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != userInfo.role){
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this@ProfileEditActivity, R.color.epp_primary)
                    changesMade = true
                }
            }
        })

        et_profile_edit_name2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim(){it <= ' '} != userInfo.name) {
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this@ProfileEditActivity, R.color.epp_primary)
                    changesMade = true
                }
            }
        })

        et_profile_edit_id2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim(){it <= ' '} != userInfo.citizenId) {
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this@ProfileEditActivity, R.color.epp_primary)
                    changesMade = true
                }
            }
        })

        et_profile_edit_phone2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim(){it <= ' '} != userInfo.mobile.toString()) {
                    fab_activity_profile.backgroundTintList =
                        ContextCompat.getColorStateList(this@ProfileEditActivity, R.color.epp_primary)
                    changesMade = true
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (changesMade){
                    showUnsavedDataAlertDialog(resources.getString(R.string.unsaved_changes),
                        resources.getString(R.string.leave_without_saving))
                } else {
                    finish()
                }
            }
        })

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_profile_edit_activity)
        toolbar_profile_edit_activity.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbar_profile_edit_activity.setNavigationOnClickListener {
            if (changesMade){
                showUnsavedDataAlertDialog(resources.getString(R.string.unsaved_changes),
                    resources.getString(R.string.leave_without_saving))
            } else {
                finish()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_update_passwd -> {
                val intent = Intent(this, PasswordUpdateActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun checkReadStoragePermissions() : Boolean {

        val result : Boolean = if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
            true

        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
            false
        }
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            } else {
                showCustomNotificationAlertDialog(
                    "READ STORAGE PERMISSION",
                    "Please set the permission from Settings"
                )
            }
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntentResult.launch(galleryIntent)
    }


    fun displayUserData(userInfo: User){
        Glide
            .with(this)
            .load(userInfo.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.person_128)
            .into(iv_profile_edit)

        et_profile_edit_name2.setText(userInfo.name)
        et_profile_edit_name2.setSelection(et_profile_edit_name2.text!!.length)

        //tv_profile_edit_head_email.text = userInfo.email

        et_profile_edit_id2.setText(userInfo.citizenId)
        et_profile_edit_id2.setSelection(et_profile_edit_id2.text!!.length)

        et_profile_edit_phone2.setText(userInfo.mobile.toString())
        et_profile_edit_phone2.setSelection(et_profile_edit_phone2.text!!.length)

        text_input_autocomplete_role.setText(userInfo.role)

        val eppRoles = arrayOf("Manager", "Trainer", "Athlete")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, eppRoles)
        text_input_autocomplete_role.setAdapter(adapter)

    }

    private fun uploadUserImage(){
        showProgressDialog()
        val path = mSelectedImageFileLocalUri!!.path!!.split("/")
        val fileName = path[path.size - 1]

        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            "USER_IMAGE_" + getCurrentUserId() + "_" + fileName + "." +
                    Constants.getFileExtension(this, mSelectedImageFileLocalUri))


        sRef.putFile(mSelectedImageFileLocalUri!!).addOnSuccessListener {
            taskSnapshot ->
            Log.i("ETIQUETA Storage image",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri ->
                Log.i(Constants.TAG, uri.toString())
                mProfileImageUri = uri.toString()
                mSelectedImageFileLocalUri = null
                updateUserProfileData()
            }
        }.addOnFailureListener{
            exception ->
            Toast.makeText(this, "${exception.message}",
                Toast.LENGTH_LONG).show()

            hideProgressDialog()

        }

    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()

        if (mProfileImageUri.isNotEmpty() && mProfileImageUri != userInfo.image){
            userInfo.image = mProfileImageUri
            userHashMap[Constants.IMAGE] = userInfo.image
        } else if (mProfileImageUri == "deleted") {
            mProfileImageUri = ""
            userHashMap[Constants.IMAGE] = userInfo.image
        }

        if (et_profile_edit_name2.text.toString().trim(){it <= ' '} != userInfo.name){
            userInfo.name = et_profile_edit_name2.text.toString().trim(){it <= ' '}
            userHashMap[Constants.NAME] = userInfo.name
        }
        if (et_profile_edit_id2.text.toString().trim(){it <= ' '} != userInfo.citizenId){
            userInfo.citizenId = et_profile_edit_id2.text.toString().trim(){it <= ' '}
            userHashMap[Constants.CITIZEN_ID] = userInfo.citizenId
        }
        if (et_profile_edit_phone2.text.toString().trim(){it <= ' '} != userInfo.mobile.toString()){
            userInfo.mobile = et_profile_edit_phone2.text.toString().toLong()
            userHashMap[Constants.MOBILE] = userInfo.mobile
        }

        if (text_input_autocomplete_role.text.toString() != userInfo.role){
            userInfo.role = text_input_autocomplete_role.text.toString()
            userHashMap[Constants.ROLE] = userInfo.role
        }

        if (userHashMap.isNotEmpty()){
            if (isProgressDialogNull())
                showProgressDialog()

            FirestoreClass().updateUserProfileData(this, userHashMap)
        } else {
            showCustomSnackBar(resources.getString(R.string.nothing_to_update), true)
        }

    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        // update shared preferences as user profile was updated
        val sharedPreference = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Constants.PROFILE_UPDATED, "yes")
        editor.putString(Constants.NAME, userInfo.name)
        editor.putString(Constants.CITIZEN_ID, userInfo.citizenId)
        editor.putLong(Constants.MOBILE, userInfo.mobile)
        editor.putString(Constants.ROLE, userInfo.role)

        if (mImageLocalCopy != "") {
            editor.putString(Constants.IMAGE, mImageLocalCopy)
        } else  {

            editor.putString(Constants.IMAGE, userInfo.image)
        }
        editor.apply()

        finish()
    }

    private fun showUnsavedDataAlertDialog(text1: String, text2: String){
        val cancelGrayButton: Button
        val okButton: Button
        val textView1 : TextView
        val textView2 : TextView
        val customAlertDialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val alert = AlertDialog.Builder(this)
        alert.setView(customAlertDialogView)

        cancelGrayButton = customAlertDialogView.findViewById(R.id.cancel_button_custom_dialog)
        okButton = customAlertDialogView.findViewById(R.id.ok_button_custom_dialog)
        textView1 = customAlertDialogView.findViewById(R.id.tv_custom_alert_dialog_1)
        textView2 = customAlertDialogView.findViewById(R.id.tv_custom_alert_dialog_2)

        textView1.text = text1
        textView2.text = text2

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.show()

        cancelGrayButton.setOnClickListener {
            dialog.dismiss()
        }

        okButton.setOnClickListener {
            finish()
        }
    }

    private fun showBottomDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        val btnCancel = dialog.findViewById<RelativeLayout>(R.id.rl_bottom_dialog_cancel)
        val btnDelete = dialog.findViewById<RelativeLayout>(R.id.rl_bottom_dialog_delete)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_bottom_dialog_gallery)

        btnCancel!!.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete!!.setOnClickListener {
            deleteUserImage()
            dialog.dismiss()
        }

        btnGallery!!.setOnClickListener {
            if (checkReadStoragePermissions())
                showImageChooser()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteUserImage(){
        mImageLocalCopy = ""
        userInfo.image = ""
        mProfileImageUri = "deleted"
        displayUserData(userInfo)
        changesMade = true
    }


}