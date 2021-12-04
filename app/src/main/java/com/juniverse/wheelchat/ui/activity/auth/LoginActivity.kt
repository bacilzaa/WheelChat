package com.juniverse.wheelchat.ui.activity.auth


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.juniverse.wheelchat.databinding.ActivityLoginBinding
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    private val viewModel : FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

    }

    private fun initView(){

        with(binding){

            loginBtn.setOnClickListener{

                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(this@LoginActivity,"Login fields can't be empty",Toast.LENGTH_LONG).show()
                }else{
                    login(email, password)
                }
            }

            signUpTv.setOnClickListener{

                startActivity(
                    Intent(this@LoginActivity,RegisterActivity::class.java)
                )


            }
        }

    }

    private fun login(email:String, pass:String){
        viewModel.signIn(email, pass)
    }





}



