package com.juniverse.wheelchat.ui.activity.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.juniverse.wheelchat.databinding.ActivityRegisterBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.model.WheelChat
import com.juniverse.wheelchat.ui.activity.edit.ProfileActivity
//import com.juniverse.wheelchat.viewmodel.FirebaseViewModel

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        initView()

    }

    private fun initView() {

        with(binding) {
            signUpBtn.setOnClickListener {
                var email = emailEditText.text.toString()
                var pass = passwordEditText.text.toString()

                if (email.isEmpty() || pass.isEmpty()) {

                    Toast.makeText(this@RegisterActivity, "Signup fields can't be empty", Toast.LENGTH_LONG).show()

                } else {
                    register(email, pass)
                }
            }
            toLoginTv.setOnClickListener {
                super.onBackPressed()
            }
        }

    }

    private fun register(email: String, pass: String) {

        auth?.let { auth ->
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    } else {

                        val currentUser = auth.currentUser

                        val uid = currentUser?.uid.toString()
                        val email = currentUser?.email.toString()

                        val userData = User(uid,email)

                        val db = FirebaseDatabase.getInstance()
                        val userDb = db.getReference("User").child(uid)

                        val wheelChatDb = db.getReference("wheelchat").child(uid)
                        val wheelchat = WheelChat("Hello","How are you","I'm fine","Thank you")

                        wheelChatDb.setValue(wheelchat)
                        userDb.setValue(userData)

                        Toast.makeText(this, "Sign Up Success", Toast.LENGTH_LONG).show()

                        ProfileActivity.launch(this,userData)
                        finish()

                    }
                }
                .addOnFailureListener { e->
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
        }

    }
}