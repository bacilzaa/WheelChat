package com.juniverse.wheelchat.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseViewModel() : ViewModel() {
        private val LOG_TAG = "FirebaseViewModel"
        private var auth:FirebaseAuth? = null

        private val _loggedStatus = MutableLiveData<Boolean>()
        val loggedStatus : LiveData<Boolean> = _loggedStatus

        var loading:MutableLiveData<Boolean> = MutableLiveData()


        init {
            auth = FirebaseAuth.getInstance()
            loading.postValue(false)
        }

        fun getLoggedStatus(){
            viewModelScope.launch {
                _loggedStatus.value = auth?.currentUser != null
            }
        }


        private val _registrationStatus = MutableLiveData<Result<String>>()
        val registrationStatus:LiveData<Result<String>> = _registrationStatus

        fun signUp(email:String, pass:String){
            loading.postValue(true)
            viewModelScope.launch(Dispatchers.IO){
                var errorCode = -1
                try {
                    auth?.let { authentication ->
                        authentication.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener{task ->
                                if(!task.isSuccessful){

                                }else{

                                }
                                loading.postValue(false)
                            }

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    loading.postValue(false)
                    if(errorCode != -1){
                        _registrationStatus.postValue(Result.failure(e))
                    }else{
                        _registrationStatus.postValue(Result.failure(e))
                    }
                }

            }
        }

    private val _signInStatus = MutableLiveData<Result<String>>()
    val signInStatus: LiveData<Result<String>> = _signInStatus
    fun signIn(email:String, password:String){
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO){
            var  errorCode = -1
            try{
                auth?.let{ login->
                    login.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task ->

                            if(!task.isSuccessful){
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(Result.success("Login Failed with ${task.exception}"))
                            }else{
                                _signInStatus.postValue(Result.success("Login Successful"))

                            }
                            loading.postValue(false)
                        }

                }

            }catch (e:Exception){
                e.printStackTrace()
                loading.postValue(false)
                if(errorCode != -1){
                    _registrationStatus.postValue(Result.failure(e))
                }else{
                    _registrationStatus.postValue(Result.failure(e))
                }


            }
        }
    }

    fun resetSignInLiveData(){
        _signInStatus.value =  Result.success("Reset")
    }
    private val _signOutStatus = MutableLiveData<Result<String>>()
    val signOutStatus: LiveData<Result<String>> = _signOutStatus
    fun signOut(){
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO){
            var  errorCode = -1
            try{
                auth?.let {authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(Result.success("Signout Successful"))
                    loading.postValue(false)
                }

            }catch (e:Exception){
                e.printStackTrace()
                loading.postValue(false)
                if(errorCode != -1){
                    _signOutStatus.postValue(Result.failure(e))
                }else{
                    _signOutStatus.postValue(Result.failure(e))
                }


            }
        }
    }

    }