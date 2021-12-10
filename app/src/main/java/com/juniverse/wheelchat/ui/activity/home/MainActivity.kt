package com.juniverse.wheelchat.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.databinding.ActivityMainBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.fragment.ChatFragment
import com.juniverse.wheelchat.ui.fragment.HomeFragment
import com.juniverse.wheelchat.ui.fragment.SettingFragment
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: FirebaseViewModel by viewModel()

    companion object{

        const val CURRENT_USER_KEY = "CURRENT_USER_KEY"

        fun launch(context: Context,user:User){
            val intent = Intent(context,MainActivity::class.java).apply {
                putExtra(CURRENT_USER_KEY,user)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

         intent.extras?.getParcelable<User>(CURRENT_USER_KEY).let { currentUser->

             if(currentUser == null) return
             val homeFragment = HomeFragment()
             val chatFragment = ChatFragment()
             val settingFragment = SettingFragment()

             setCurrentFragment(homeFragment)
             supportActionBar?.title = currentUser?.name


             bottomNavigationView.setOnItemSelectedListener {
                 when (it.itemId) {
                     R.id.homeNav -> {
                         setCurrentFragment(homeFragment)
                         supportActionBar?.title = currentUser?.name
                     }
                     R.id.chatNav -> {
                         setCurrentFragment(chatFragment!!)
                         supportActionBar?.title = "Chat"
                     }
                     R.id.settingNav -> {
                         setCurrentFragment(settingFragment)
                         supportActionBar?.title = "Setting"
                     }

                 }
                 true
             }


         }

         viewModel.fetchUsers()
         viewModel.lastestMessageListener()

    }




    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragment.id, fragment)
            commit()
        }
}