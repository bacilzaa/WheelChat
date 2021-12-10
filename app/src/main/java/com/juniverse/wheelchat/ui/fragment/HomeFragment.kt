package com.juniverse.wheelchat.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.database.*
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingFragment
import com.juniverse.wheelchat.databinding.FragmentHomeBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.ChatLogActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity.Companion.CURRENT_USER_KEY
import com.juniverse.wheelchat.ui.adapter.UserListAdapter
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: DataBindingFragment<FragmentHomeBinding>(){

    private val adapter = UserListAdapter(mutableListOf())

    private val viewModel:FirebaseViewModel by sharedViewModel()



    companion object{
        const val TAG = "HomeFragment"
        const val USER_KEY = "USER_KEY"
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun start() {

        viewBinding.recyclerView.adapter = adapter

        viewModel.userList.observe(this, Observer{
            Log.i("viewModelshared", it.toString())
            adapter.setData(it)
        })


        adapter.setOnClickListener {

            Log.i("Test", it.toString())

            val intent = Intent(activity, ChatLogActivity::class.java)
            intent.putExtra(ChatFragment.USER_KEY, it)
            viewModel.currentUser.observe(this, Observer { currentUser->
                intent.putExtra(CURRENT_USER_KEY,currentUser)
            })
            startActivity(intent)
        }
    }




}
