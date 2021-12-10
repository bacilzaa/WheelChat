package com.juniverse.wheelchat.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingFragment
import com.juniverse.wheelchat.databinding.FragmentChatBinding
import com.juniverse.wheelchat.model.LastestMessageItem
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.ChatLogActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity.Companion.CURRENT_USER_KEY
import com.juniverse.wheelchat.ui.adapter.LastestMessageListAdapter
import com.juniverse.wheelchat.ui.adapter.UserListAdapter
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatFragment : DataBindingFragment<FragmentChatBinding>() {

    private val adapter = LastestMessageListAdapter(mutableListOf())

    private val viewModel: FirebaseViewModel by sharedViewModel()

    companion object {
        const val USER_KEY = "USER_KEY"
        const val USER_LIST_KEY = "USER_LIST_KEY"

        fun newInstance(userList:ArrayList<User>,user: User): ChatFragment {
            val fragment = ChatFragment()

            val bundle = Bundle().apply {
                putParcelable(CURRENT_USER_KEY, user)
                putParcelableArrayList(USER_LIST_KEY,userList)
            }

            fragment.arguments = bundle

            return fragment
        }

    }

    override fun layoutId(): Int = R.layout.fragment_chat

    override fun start() {
        initView()
    }

    private fun initView() {

        viewBinding.recyclerView.adapter = adapter

        viewModel.lastestMessageList.observe(this, Observer {
            adapter.clear()
            it.forEach{ map->
                val user = viewModel.getUserByUid(map.key)
                adapter.add(LastestMessageItem(map.value,user!!))
            }
        })


        adapter.setOnClickListener {

            Log.i("Test", it.toString())

            val intent = Intent(activity, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, it)
            viewModel.currentUser.observe(this, Observer { currentUser->
                intent.putExtra(CURRENT_USER_KEY,currentUser)
            })
            startActivity(intent)
        }
    }


}