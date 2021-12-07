package com.juniverse.wheelchat.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseViewModel() : ViewModel() {
    private var auth: FirebaseAuth? = null

    private val _loggedStatus = MutableLiveData<Boolean>()
    val loggedStatus: LiveData<Boolean> = _loggedStatus

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser :LiveData<FirebaseUser> = _currentUser


    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        auth = FirebaseAuth.getInstance()
        loading.postValue(false)

    }

    fun getLoggedStatus() {
        viewModelScope.launch {
            _loggedStatus.value = auth?.currentUser != null
        }
    }

    fun getCurrentUser(){
        viewModelScope.launch {
            _currentUser.value =auth?.currentUser
        }
    }

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> = _registrationStatus

    fun signUp(email: String, pass: String) {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            auth?.let { auth ->
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            _registrationStatus.postValue(it.exception?.message)
                        } else {
                            _registrationStatus.postValue("Sign Up Success")
                            getLoggedStatus()
                        }
                    }
                    .addOnFailureListener {
                        _registrationStatus.postValue(it.message)
                    }
                loading.postValue(false)
            }
        }
    }

    private val _signInStatus = MutableLiveData<String>()
    val signInStatus: LiveData<String> = _signInStatus

    fun signIn(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            auth?.let { login ->
                login.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (!task.isSuccessful) {
                            _signInStatus.postValue(task.exception?.message)
                        } else {
                            _signInStatus.postValue("Login Successful")
                            getLoggedStatus()
                        }
                    }
                    .addOnFailureListener {
                        _signInStatus.postValue(it.message)
                    }

                loading.postValue(false)
            }
        }
    }

    private val _signOutStatus = MutableLiveData<Result<String>>()
    val signOutStatus: LiveData<Result<String>> = _signOutStatus

    fun signOut() {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var errorCode = -1
            try {
                auth?.let { authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(Result.success("Signout Successful"))
                    loading.postValue(false)
                    getLoggedStatus()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _signOutStatus.postValue(Result.failure(e))
                } else {
                    _signOutStatus.postValue(Result.failure(e))
                }


            }
        }
    }

}