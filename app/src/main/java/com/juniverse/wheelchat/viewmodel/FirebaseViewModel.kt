package com.juniverse.wheelchat.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.juniverse.wheelchat.model.ChatMessage
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.model.WheelChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirebaseViewModel() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseDatabase

    private var _userList = MutableLiveData<ArrayList<User>>()
    val userList: LiveData<ArrayList<User>> = _userList

    private var _currentUser: MutableLiveData<User> = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private var _lastestMessageList: MutableLiveData<HashMap<String, ChatMessage>> =
        MutableLiveData<HashMap<String, ChatMessage>>()
    val lastestMessageList: LiveData<HashMap<String, ChatMessage>> = _lastestMessageList


    private var _wheelChatItem:MutableLiveData<WheelChat> = MutableLiveData<WheelChat>()
    val wheelChatItem:LiveData<WheelChat> = _wheelChatItem


    init {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        getCurrentUser()
    }


    fun performSendMessage(fromId:String,toId:String,text:String){


        val mesFromRef = Firebase.database.getReference("/user-message/$fromId/$toId").push()
        val mesToRef = Firebase.database.getReference("/user-message/$toId/$fromId").push()

        val chatMessage = ChatMessage(mesFromRef.key!!,text,fromId!!,toId!!,System.currentTimeMillis()/1000)

        mesFromRef.setValue(chatMessage)

        mesToRef.setValue(chatMessage)

        val lastMesFromRef = Firebase.database.getReference("/last-message/$fromId/$toId")
        val lastMesToRef = Firebase.database.getReference("/last-message/$toId/$fromId")

        lastMesFromRef.setValue(chatMessage)
        lastMesToRef.setValue(chatMessage)


    }


    fun fetchUsers() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var userListData = ArrayList<User>()
                val ref = db.getReference("User")
                ref.get().addOnCompleteListener {
                    if (it.isSuccessful) {

                        it.result?.children?.forEach {
                            val user = it.getValue(User::class.java)!!
                            if (auth.currentUser?.uid != user.uid) {
                                userListData.add(user)
                            }
                        }

                        _userList.value = userListData
                        Log.i("TEST@!#", userListData.toString())


                    } else {
                        Log.i("Test", it.exception?.message.toString())
                    }
                }
            }
        }


    }

    fun fetchWheelChat(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val ref = db.getReference("wheelchat/"+ auth.currentUser?.uid)
                ref.get().addOnCompleteListener {
                    if(it.isSuccessful){
                        _wheelChatItem.value = it.result!!.getValue(WheelChat::class.java)
                    }else{
                        Log.i("Test", it.exception?.message.toString())
                    }
                }
            }
            }
    }


    fun lastestMessageListener() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val lastmap = HashMap<String, ChatMessage>()

                var ref = db.getReference("last-message/" + auth.currentUser?.uid)
                ref.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                        lastmap[snapshot.key!!] = chatMessage
                        _lastestMessageList.value = lastmap
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                        lastmap[snapshot.key!!] = chatMessage
                        _lastestMessageList.value = lastmap

                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        return
                    }


                })
            }
        }

    }


    fun getUserByUid(uid: String): User? {
        _userList.value?.forEach {
            if (it.uid == uid) {
                return it
            }
        }
        return null
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                db.getReference("User/" + auth.currentUser?.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            _currentUser.value = snapshot.getValue(User::class.java) ?: return


                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })


            }
        }
    }

}