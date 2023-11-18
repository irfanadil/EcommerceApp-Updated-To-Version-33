package com.ecommerce.testapp

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepo: LoginRepository) : ViewModel() {

    //private val _loginResult = MutableLiveData<LoginApiResult>()
    //val loginApiResult: LiveData<LoginApiResult> = _loginResult

    private val loginEvent = Channel<LoginApiResult>()
    // Receiving channel as a flow
    val loginEventFlow = loginEvent.receiveAsFlow()



    fun testLogin(username: String, password: String){
        viewModelScope.launch {
            delay(2000)
            viewModelScope.launch {
                val response = loginRepo.login(username, password)
                if(response is LoginApiResult.Success){
                    loginEvent.send(LoginApiResult.Success(response.data))
                }
                else{
                    loginEvent.send(LoginApiResult.Failure(message = "Login call failed..."))
                }
            }

        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    /*
    fun login(username: String, password: String) {
        viewModelScope.launch {
            var bool = false
            val userListResult = loginRepo.fetchUserList()
            if (userListResult is ApiResult.Success) {
                val userList = userListResult.data.userList
                if (userList != null) {
                    for (user in userList) {
                        if (user.userName == username && user.password == password) {
                            bool = true
                            break
                        }
                    }
                }
            }
            if (bool) {
                val result = loginRepo.login(username, password)
                Log.e("result - ", result.toString())
                if (result is ApiResult.Success)
                //_loginResult.value = LoginResponseResult(success = LoginResponse(token = result.data.token))
                    loginEvent.send(ApiResult.Success(result.data))
                else
                    loginEvent.send(ApiResult.Error(result.toString()))
                //_loginResult.value = LoginResponseResult(error = R.string.login_failed)
            }
        }
    }

     */


}