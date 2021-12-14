package com.juniverse.wheelchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.ui.activity.edit.ProfileActivity
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
            viewModel.currentUser.observe(this, Observer { user ->
                Log.i("ViewModel", user.toString())
                if (user.name.isNullOrEmpty()) {
                    ProfileActivity.launch(this@SplashScreenActivity, user)
                    finish()
                } else {
                    MainActivity.launch(this@SplashScreenActivity, user)
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