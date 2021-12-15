package com.juniverse.wheelchat.ui.fragment

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingFragment
import com.juniverse.wheelchat.databinding.FragmentSettingBinding
import com.juniverse.wheelchat.ui.activity.auth.LoginActivity
import com.juniverse.wheelchat.ui.activity.edit.ProfileActivity
import com.juniverse.wheelchat.ui.activity.edit.WheelChatActivity
import com.juniverse.wheelchat.ui.adapter.SettingItem
import com.juniverse.wheelchat.ui.adapter.SettingListAdapter
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingFragment : DataBindingFragment<FragmentSettingBinding>() {

    companion object {
        const val TAG = "SettingFragment"

    }

    private val adapter = SettingListAdapter(mutableListOf())

    private val viewModel: FirebaseViewModel by sharedViewModel()

    override fun layoutId(): Int = R.layout.fragment_setting

    override fun start() {

        viewBinding.apply {
            adapter.clear()
            adapter.setOnClickListener {
                when (it) {
                    0 -> {
                        startActivity(Intent(activity,ProfileActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(activity,WheelChatActivity::class.java))
                    }
                    2 -> {
                        var intent = Intent(activity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)

                    }
                }
            }

            recyclerView.adapter = adapter

            val menu = listOf<SettingItem>(
                SettingItem("Edit Profile", R.drawable.ic_baseline_account_box_24),
                SettingItem("Edit WheelChat", R.drawable.ic_wheelchat_icon),
                SettingItem("Logout", R.drawable.ic_baseline_login_24)
            )

            adapter.setData(menu)


        }


//        logoutBtn.setOnClickListener {

//
//        }

    }

}