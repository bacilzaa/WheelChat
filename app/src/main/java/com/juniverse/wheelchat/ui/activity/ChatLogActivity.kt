package com.juniverse.wheelchat.ui.activity

import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.juniverse.wheelchat.databinding.ActivityChatLogBinding
import com.juniverse.wheelchat.model.ChatFromItem
import com.juniverse.wheelchat.model.ChatMessage
import com.juniverse.wheelchat.model.ChatToItem
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.home.MainActivity.Companion.CURRENT_USER_KEY
import com.juniverse.wheelchat.ui.adapter.ChatLogAdapter
import com.juniverse.wheelchat.ui.fragment.ChatFragment
import com.juniverse.wheelchat.ui.fragment.WheelChatDialogFragment
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel

class ChatLogActivity : AppCompatActivity() {

    private val binding : ActivityChatLogBinding by lazy{ ActivityChatLogBinding.inflate(layoutInflater)}

    private val viewModel:FirebaseViewModel by viewModel()

    private val adapter = ChatLogAdapter(mutableListOf())

    private var user:User? = null

    private var currentUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getParcelableExtra(ChatFragment.USER_KEY)
        currentUser = intent.getParcelableExtra(CURRENT_USER_KEY)

        supportActionBar?.title = user?.name

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)

        listenForMessages()

        initView()

        viewModel.fetchWheelChat()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initView(){

        with(binding){

            chatLogRecycleview.adapter = adapter

            chatLogRecycleview.scrollToPosition(adapter.itemCount - 1)

            val dialogWheelChat = WheelChatDialogFragment.newInstance(currentUser!!,user!!)

            sendBtn.setOnClickListener{
                if(!binding.chatEditText.text.isNullOrEmpty()) {
                    Log.i("Test", "Attemp to send message...")

                    viewModel.performSendMessage(currentUser!!.uid,user!!.uid,binding.chatEditText.text.toString())

                }else{
                    dialogWheelChat.show(supportFragmentManager,"wheel_chat")
                }

                with(binding){
                    chatEditText.text?.clear()
                    chatLogRecycleview.scrollToPosition(adapter.itemCount - 1)
                }
            }

        }
    }

    private fun performSendMessage(){
        val fromId = currentUser?.uid
        val toId = user?.uid

        val text = binding.chatEditText.text.toString()

        val mesFromRef = Firebase.database.getReference("/user-message/$fromId/$toId").push()
        val mesToRef = Firebase.database.getReference("/user-message/$toId/$fromId").push()

        val chatMessage = ChatMessage(mesFromRef.key!!,text,fromId!!,toId!!,System.currentTimeMillis()/1000)

        mesFromRef.setValue(chatMessage).addOnSuccessListener {

        }

        mesToRef.setValue(chatMessage)

        val lastMesFromRef = Firebase.database.getReference("/last-message/$fromId/$toId")
        val lastMesToRef = Firebase.database.getReference("/last-message/$toId/$fromId")

        lastMesFromRef.setValue(chatMessage)
        lastMesToRef.setValue(chatMessage)


    }

    private fun listenForMessages(){
        val fromId = currentUser?.uid
        val toId = user?.uid

        val mesRef = Firebase.database.getReference("/user-message/$fromId/$toId")

        mesRef.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage: ChatMessage? = snapshot.getValue(ChatMessage::class.java)
                chatMessage?.text?.let { Log.i("Test", it) }

                if(chatMessage == null)return

                if(chatMessage.fromId == fromId) {
                    adapter.add(ChatToItem(chatMessage))
                }else{
                    adapter.add(ChatFromItem(chatMessage,user!!))
                }

                    binding.chatLogRecycleview.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}