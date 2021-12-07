package com.juniverse.wheelchat.ui.activity.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.databinding.ActivityRegisterBinding
import com.juniverse.wheelchat.ui.activity.MainActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(
            layoutInflater
        )
    }

    private val loadingDialog: ProgressDialog by lazy { ProgressDialog(this@RegisterActivity) }

    private val viewModel: FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        initObserver()
    }

    private fun initView() {

        with(binding) {
            signUpBtn.setOnClickListener {
                var email = emailEditText.text.toString()
                var pass = passwordEditText.text.toString()

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Signup fields can't be empty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    register(email, pass)
                }
            }
        }


    }

    private fun initObserver(){
        viewModel.apply {
            loadingDialog.setTitle("SignUp")
            loadingDialog.setMessage("Loading...")

            loggedStatus.observe(this@RegisterActivity, Observer {
                if(it){
                    loadingDialog.show()
                }else{
                    loadingDialog.dismiss()
                }
            })
        }
    }

    private fun register(email: String, pass: String) {

        viewModel.apply {
            signUp(email, pass)
            loggedStatus.observe(this@RegisterActivity, Observer {
                if (it) {
                    loadingDialog.dismiss()

                    startActivity(
                        Intent(this@RegisterActivity, MainActivity::class.java)
                    )
                    finish()
                }

                registrationStatus.observe(this@RegisterActivity, Observer {
                    Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_LONG).show()
                })
            })
        }

    }
}