package com.juniverse.wheelchat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    private val binding : ActivityChatLogBinding by lazy{ ActivityChatLogBinding.inflate(layoutInflater)}

    private val CURRENT_USER_KEY = "CURRENT_USER_KEY"

    private val adapter = ChatLogAdapter(mutableListOf())

    private var user:User? = null

    private var currentUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getParcelableExtra<User>(ChatFragment.USER_KEY)
        currentUser = intent.getParcelableExtra<User>(CURRENT_USER_KEY)

        supportActionBar?.title = user?.name

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)

        listenForMessages()

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initView(){

        with(binding){

            chatLogRecycleview.adapter = adapter

            chatLogRecycleview.scrollToPosition(adapter.itemCount - 1)

            sendBtn.setOnClickListener{
                Log.i("Test","Attemp to send message...")
                performSendMessage()
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
            with(binding){
                chatEditText.text?.clear()
                chatLogRecycleview.scrollToPosition(adapter.itemCount - 1)
            }
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