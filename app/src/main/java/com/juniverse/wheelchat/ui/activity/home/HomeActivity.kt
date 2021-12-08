package com.juniverse.wheelchat.ui.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.databinding.ActivityMainBinding
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        viewModel.getCurrentUser()

    }

    private fun initView() {

        with(binding) {

            viewModel.apply {
                currentUser.observe(this@MainActivity, Observer {
                    textView.text = it?.email
                })

                logoutBtn.setOnClickListener {

                    signOut()
                    loggedStatus.observe(this@MainActivity, Observer {
                        if (!it) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                    })
                }
            }

        }

    }
}