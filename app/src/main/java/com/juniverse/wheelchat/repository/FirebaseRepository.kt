//package com.juniverse.wheelchat.repository
//
//import android.util.Log
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//import com.juniverse.wheelchat.base.UseCaseResult
//import com.juniverse.wheelchat.helper.handleUseCaseException
//import com.juniverse.wheelchat.model.User
//import org.koin.core.component.KoinComponent
//
//interface FirebaseRepository {
//    suspend fun getUser(): UseCaseResult<User>
//    suspend fun getUserList(): UseCaseResult<List<User>>
//}
//
//class FirebaseRepositoryImpl(
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
//    private val userRef: DatabaseReference = Firebase.database.getReference("User/" + auth.currentUser?.uid)
//) : FirebaseRepository, KoinComponent {
//
//
//    override suspend fun getUser(): UseCaseResult<User> {
//        var user = User()
//
//        try{
//            userRef.addListenerForSingleValueEvent(object:ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    user = snapshot.getValue(User::class.java)!!
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }catch ()
//    }
//
//    override suspend fun getUserList(): UseCaseResult<List<User>> {
//        TODO("Not yet implemented")
//    }
//
//}