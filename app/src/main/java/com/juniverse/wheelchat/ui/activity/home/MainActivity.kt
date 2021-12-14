package com.juniverse.wheelchat.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.databinding.ActivityMainBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.fragment.ChatFragment
import com.juniverse.wheelchat.ui.fragment.HomeFragment
import com.juniverse.wheelchat.ui.fragment.SettingFragment
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: FirebaseViewModel by viewModel()

    companion object {

        const val CURRENT_USER_KEY = "CURRENT_USER_KEY"

        fun launch(context: Context, user: User) {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(CURRENT_USER_KEY, user)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        initView()

        viewModel.fetchUsers()
        viewModel.lastestMessageListener()

    }

    private fun initView() {

        viewModel.apply {
            currentUser.observe(this@MainActivity, Observer { user->

                val homeFragment = HomeFragment()
                val chatFragment = ChatFragment()
                val settingFragment = SettingFragment()

                setCurrentFragment(homeFragment)
                setHomeFragment(user)
                Picasso.get().load(user.profile_img).into(binding.toolBar.iconTb)


                bottomNavigationView.setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.homeNav -> {
                            setCurrentFragment(homeFragment)
                            setHomeFragment(user)
                        }
                        R.id.chatNav -> {
                            setCurrentFragment(chatFragment)
                            setChatFragment()
                        }
                        R.id.settingNav -> {
                            setCurrentFragment(settingFragment)
                            setSettingFragment()
                        }

                    }
                    true
                }

            })
        }
    }

    private fun setHomeFragment(user: User){
        binding.toolBar.titleTb.text = user.name
        binding.toolBar.iconTb.visibility = View.VISIBLE
    }

    private fun setChatFragment(){
        binding.toolBar.titleTb.text = "Chat"
        binding.toolBar.iconTb.visibility = View.GONE
    }

    private fun setSettingFragment(){
        binding.toolBar.titleTb.text = "Setting"
        binding.toolBar.iconTb.visibility = View.GONE
    }




    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragment.id, fragment)
            commit()
        }
}