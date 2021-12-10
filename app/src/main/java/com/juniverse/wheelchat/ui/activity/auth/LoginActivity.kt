package com.juniverse.wheelchat.ui.activity.auth


import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.juniverse.wheelchat.databinding.ActivityLoginBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.ui.activity.home.ProfileActivity

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        initView()

    }

    private fun initView() {

        with(binding) {

            loginBtn.setOnClickListener {

                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login fields can't be empty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    login(email, password)

                }
            }

            signUpTv.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }


        }

    }

    private fun login(email: String, pass: String) {

        auth?.let { login ->
            login.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {

                    if (!it.isSuccessful) {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG)
                            .show()
                    } else {

                        val userRef = Firebase.database.getReference("User/" + auth?.currentUser?.uid)
                        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue(User::class.java)
                                user?.let { userdata ->
                                    if (userdata.name.isNullOrEmpty()) {
                                        ProfileActivity.launch(this@LoginActivity, userdata)
                                    }else {
                                        MainActivity.launch(this@LoginActivity, userdata)
                                    }
                                    finish()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                    }
                }.addOnFailureListener { e ->

                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

                }
        }
    }


}



