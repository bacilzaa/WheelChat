package com.juniverse.wheelchat.ui.activity.auth


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.databinding.ActivityLoginBinding
import com.juniverse.wheelchat.ui.activity.MainActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private val viewModel: FirebaseViewModel by viewModel()

    private val loadingDialog: ProgressDialog by lazy { ProgressDialog(this@LoginActivity) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        initObserver()

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

                startActivity(
                    Intent(this@LoginActivity, RegisterActivity::class.java)
                )
            }


        }

    }

    private fun initObserver(){
        viewModel.apply {
            loading.observe(this@LoginActivity, Observer {
                if(it){
                    loadingDialog.setTitle("Login")
                    loadingDialog.setMessage("Loading...")
                    loadingDialog.show()
                }else{
                    loadingDialog.dismiss()
                }
            })
        }
    }

    private fun login(email: String, pass: String) {
        viewModel.signIn(email, pass)
        viewModel.loggedStatus.observe(this@LoginActivity, Observer {
            if (it) {
                loadingDialog.dismiss()
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                )
                finish()
            }

            viewModel.signInStatus.observe(this@LoginActivity, Observer {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_LONG).show()
            })
        })

    }


}



