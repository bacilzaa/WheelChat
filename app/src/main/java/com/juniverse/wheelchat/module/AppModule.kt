package com.juniverse.wheelchat.module


import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.dsl.module

val appModule = module {
 factory { FirebaseViewModel() }
}
