package com.juniverse.wheelchat.ui.activity.edit

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.juniverse.wheelchat.databinding.ActivityProfileBinding
import com.juniverse.wheelchat.helper.bitmapToFile
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.google.firebase.database.ktx.database

class ProfileActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: FirebaseViewModel by viewModel()

    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(binding.root)


        initView()

    }


    private fun initView() {

        with(binding) {

            viewModel.currentUser.observe(this@ProfileActivity, Observer { user ->
                if (user != null) {
                    if (!user.name.isEmpty()) {
                        usernameEditText.setText(user.name)
                        profileCloseBtn.visibility = View.VISIBLE

                        if (imageUri == null) {
                            Picasso.get().load(user.profile_img)
                                .into(profileImageBtn)
                        }
                    }
                }
            })


            profileCloseBtn.setOnClickListener {
                finish()
            }

            profileImageBtn.setOnClickListener {
                choosePicture()
            }

            updateBtn.setOnClickListener {
                var username = usernameEditText.text.toString()
                if (username.isEmpty()) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Please enter your display name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    updateProfile(username)
                }
            }

        }

    }

    private fun choosePicture() {

        val builder = AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Choose a method")

        val method = arrayOf("Camera", "Gallery")
        builder.setItems(method) { dialog, which ->
            when (which) {
                0 -> {
                    startActivityForResult(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        REQUEST_IMAGE_CAPTURE
                    )
                }
                1 -> {
                    startActivityForResult(
                        Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT),
                        REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }

        val dialog = builder.create()
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data?.data != null) {
            imageUri = data.data
        } else {
            imageUri = bitmapToFile(this@ProfileActivity, data?.extras?.get("data") as Bitmap)
        }
        Log.i("Test", imageUri.toString())
        binding.profileImageBtn.setImageURI(imageUri)
    }


    private fun updateProfile(username: String) {

        var uid = viewModel.currentUser.value!!.uid

        val refStorage = Firebase.storage.reference.child("profile/"+uid)
        imageUri?.let {
            refStorage.putFile(it)
                .addOnSuccessListener { task ->

                    refStorage.downloadUrl.addOnSuccessListener {
                        var profile_img = it.toString()
                        val db = Firebase.database.getReference("User/" + uid)

                        db.child("name").setValue(username)
                        db.child("profile_img").setValue(profile_img)

                        startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                        finish()

                        Toast.makeText(this@ProfileActivity, "Uploaded Success", Toast.LENGTH_LONG)
                            .show()
                    }

                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this@ProfileActivity,
                        "Uploaded Failed :${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

}