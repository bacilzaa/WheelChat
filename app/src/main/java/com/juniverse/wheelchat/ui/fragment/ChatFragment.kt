package com.juniverse.wheelchat.ui.fragment


import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingFragment
import com.juniverse.wheelchat.databinding.FragmentChatBinding

class MessageFragment: DataBindingFragment<FragmentChatBinding>(){

    companion object{
        const val TAG = "ChatFragment"
    }

    override fun layoutId(): Int = R.layout.fragment_chat

    override fun start() {
    }

    private fun initView(){

    }

}