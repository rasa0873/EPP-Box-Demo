package com.example.eppdraft1.main.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.eppdraft1.R

class TrainersProfileActivity : AppCompatActivity() {

    private lateinit var toolbarTrainers : Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainers_profile)

        setupActionBar()
        val imageViewInstagramCarrera : ImageView = findViewById(R.id.iv_ernesto_ig_link)
        imageViewInstagramCarrera.setOnClickListener {
            val profileIGUrl = "https://www.instagram.com/protpersonal/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profileIGUrl))
            startActivity(intent)
        }
    }

    private fun setupActionBar(){
        toolbarTrainers = findViewById(R.id.toolbar_trainers_activity)
        setSupportActionBar(toolbarTrainers)
        toolbarTrainers.setNavigationIcon(R.drawable.white_arrow_back_ios_24)
        toolbarTrainers.setNavigationOnClickListener {
            finish()
        }

    }
}