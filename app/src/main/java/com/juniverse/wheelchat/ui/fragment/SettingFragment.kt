package com.juniverse.wheelchat.ui.fragment

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingFragment
import com.juniverse.wheelchat.databinding.FragmentSettingBinding
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment: DataBindingFragment<FragmentSettingBinding>(){

    companion object{
        const val TAG = "SettingFragment"

    }

    override fun layoutId(): Int = R.layout.fragment_setting

    override fun start() {
        logoutBtn.setOnClickListener {
            var auth = FirebaseAuth.getInstance()
            auth.signOut()

            var intent = Intent(activity,LoginActivity::class.java)
            startActivity(intent)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.finish()




        }

    }

}