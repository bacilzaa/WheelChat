package com.juniverse.wheelchat.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.juniverse.wheelchat.databinding.ActivityProfileBinding
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private var imageUri: Uri? = null

    private val viewModel: FirebaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initObsever()
        initView()

        viewModel.getCurrentUser()
    }

    private fun initObsever() {
        viewModel.apply {

            currentUser.observe(this@ProfileActivity, Observer {

            })

        }
    }

    private fun initView() {

        with(binding) {

            profileImageBtn.setOnClickListener {
                choosePicture()
            }

            updateBtn.setOnClickListener {
                var username = usernameEditText.text.toString()
                if(username.isEmpty()){
                    Toast.makeText(this@ProfileActivity,"Please enter your display name",Toast.LENGTH_SHORT).show()
                }else{
                    update(username)

                    startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                    finish()
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
        }else{

            imageUri = bitmapToFile(data?.extras?.get("data") as Bitmap)
        }

        binding.profileImageBtn.setImageURI(imageUri)
    }

    private fun bitmapToFile(bitmap:Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    private fun update(username:String) {

        viewModel.apply {

            var pd = ProgressDialog(this@ProfileActivity)
            pd.setTitle("Uploading Image...")
            pd.show()

            var uid = currentUser.value?.uid.toString()


            val refStorage =
                Firebase.storage.reference.child("User/Profile/" + uid)
            imageUri?.let {
                refStorage.putFile(it)
                    .addOnSuccessListener { task ->
                        pd.dismiss()
                        Toast.makeText(this@ProfileActivity, "Uploaded Success", Toast.LENGTH_LONG)
                            .show()

                        var profile_img = task.uploadSessionUri.toString()

                        userData.value?.profile_img = profile_img
                        userData.value?.name = username

                        updateProfile()

                    }
                    .addOnFailureListener {
                        pd.dismiss()
                        Toast.makeText(
                            this@ProfileActivity,
                            "Uploaded Failed :${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnProgressListener { task ->
                        var progressPrecent = (100.00 * task.bytesTransferred / task.totalByteCount)
                        pd.setMessage("Progress : $progressPrecent %")
                    }
            }
        }
    }


}