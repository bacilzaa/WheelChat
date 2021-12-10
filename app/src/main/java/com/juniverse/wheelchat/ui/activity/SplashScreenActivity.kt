package com.juniverse.wheelchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.ui.activity.home.ProfileActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: FirebaseViewModel by viewModel()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            viewModel.currentUser.observe(this, Observer {
                Log.i("ViewModel", it.toString())
                if (it.name.isNullOrEmpty()) {
                    ProfileActivity.launch(this@SplashScreenActivity, it)
                    finish()
                } else {
                    MainActivity.launch(this@SplashScreenActivity, it)
                    finish()
                }
            })
        } else {
            Log.i("Test", "StartLoginActivity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}