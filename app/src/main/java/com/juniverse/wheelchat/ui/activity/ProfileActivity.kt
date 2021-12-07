package com.juniverse.wheelchat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.databinding.ActivityProfileBinding
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val binding : ActivityProfileBinding by lazy { ActivityProfileBinding.inflate(layoutInflater)}

    private val viewModel : FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initObsever()
        viewModel.getCurrentUser()
    }

    private fun initObsever(){
        viewModel.apply {

            currentUser.observe(this@ProfileActivity, Observer {

            })


        }
    }




}