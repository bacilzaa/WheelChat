package com.juniverse.wheelchat.ui.activity.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.juniverse.wheelchat.databinding.ActivityProfileBinding
import com.juniverse.wheelchat.helper.bitmapToFile
import com.juniverse.wheelchat.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        const val CURRENT_USER_KEY = "CURRENT_USER_KEY"

        fun launch(context: Context, user: User) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(CURRENT_USER_KEY, user)
            }
            context.startActivity(intent)
        }
    }

    private var imageUri: Uri? = null

    private var auth: FirebaseAuth? = null

    private var db: DatabaseReference? = null

    init {
        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference.child("User").child(auth?.currentUser?.uid.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(binding.root)

        intent.extras?.getParcelable<User>(CURRENT_USER_KEY)?.let {
            initView(it)
        }
    }


    private fun initView(currentUser: User) {

        with(binding) {

            Log.i("Test", "ProfileActivity :" + currentUser.toString())

            if (!currentUser?.name.isNullOrEmpty()) {
                usernameEditText.setText(currentUser?.name)
                profileCloseBtn.visibility = View.VISIBLE

                if (imageUri == null) {
                    Picasso.get().load(currentUser?.profile_img)
                        .into(profileImageBtn)
                }
            }

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
            imageUri = data?.data
        } else {
            imageUri = bitmapToFile(this@ProfileActivity, data?.extras?.get("data") as Bitmap)
        }
        Log.i("Test", imageUri.toString())
        binding.profileImageBtn.setImageURI(imageUri)
    }


    private fun updateProfile(username: String) {

        var uid = auth?.currentUser?.uid.toString()


        val refStorage = Firebase.storage.reference.child("User/Profile/" + uid)
        imageUri?.let {
            refStorage.putFile(it)
                .addOnSuccessListener { task ->

                    refStorage.downloadUrl.addOnSuccessListener {
                        var profile_img = it.toString()

                        db?.child("name")?.setValue(username)
                        db?.child("profile_img")?.setValue(profile_img)

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