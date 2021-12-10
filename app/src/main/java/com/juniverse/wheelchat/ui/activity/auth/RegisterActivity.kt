package com.juniverse.wheelchat.ui.activity.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.juniverse.wheelchat.databinding.ActivityRegisterBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.home.ProfileActivity
//import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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