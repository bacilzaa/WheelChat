package com.juniverse.wheelchat.ui.activity.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.juniverse.wheelchat.databinding.ActivityWheelChatBinding
import com.juniverse.wheelchat.model.WheelChat
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WheelChatActivity : AppCompatActivity() {

    private val binding: ActivityWheelChatBinding by lazy {
        ActivityWheelChatBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: FirebaseViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "WheelChat"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initView()
        initObserver()

        viewModel.fetchWheelChat()

    }

    private fun initView() {
        with(binding) {
            updateWheelChatBtn.setOnClickListener {
                val ref = Firebase.database.getReference("wheelchat")
                    .child(viewModel.currentUser.value!!.uid)
                val wheelChat = WheelChat(
                    topEditText.text.toString(),
                    rightEditText.text.toString(),
                    bottomEditText.text.toString(),
                    leftEditText.text.toString()
                )

                ref.setValue(wheelChat).addOnSuccessListener {
                    Toast.makeText(this@WheelChatActivity,"Update WheelChat Success",Toast.LENGTH_LONG).show()
                    super.onBackPressed()
                }

            }
        }
    }

    private fun initObserver() {
        viewModel.wheelChatItem.observe(this, Observer {
            if(it != null) {
                with(binding) {
                    if (it.top.isNotEmpty()) {
                        topEditText.setText(it.top)
                    }
                    if (it.right.isNotEmpty()) {
                        rightEditText.setText(it.right)
                    }
                    if (it.right.isNotEmpty()) {
                        bottomEditText.setText(it.bottom)
                    }
                    if (it.left.isNotEmpty()) {
                        leftEditText.setText(it.left)
                    }
                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}