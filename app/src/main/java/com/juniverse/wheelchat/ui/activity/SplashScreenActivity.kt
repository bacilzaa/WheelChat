package com.juniverse.wheelchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel : FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserver()

        viewModel.getLoggedStatus()

    }
    private fun initObserver() {


        viewModel.apply {
            loggedStatus.observe(this@SplashScreenActivity, Observer {

                if(it) {
                    startActivity(
                        Intent(this@SplashScreenActivity, MainActivity::class.java)
                    )
                }else{
                    startActivity(
                        Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    )
                }

                finish()
            })
        }
    }
}