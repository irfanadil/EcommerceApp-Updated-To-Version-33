package com.ecommerce.testapp

import android.util.Log
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
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginApiResult>()
    val loginApiResult: LiveData<LoginApiResult> = _loginResult

    private val loginEvent = Channel<LoginApiResult>()

    // Receiving channel as a flow
    val loginEventFlow = loginEvent.receiveAsFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            var bool = false
            val userListResult = loginRepository.fetchUserList()
            if (userListResult is Result.Success) {
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
                val result = loginRepository.login(username, password)
                Log.e("result - ", result.toString())
                if (result is Result.Success)
                    //_loginResult.value = LoginResponseResult(success = LoginResponse(token = result.data.token))
                    loginEvent.send(LoginApiResult.Success(result.data))
                else
                    loginEvent.send(LoginApiResult.Error(result.toString()))
                    //_loginResult.value = LoginResponseResult(error = R.string.login_failed)
            }
        }
    }

    fun testLogin(username: String, password: String){
        viewModelScope.launch {
            delay(2000)
            loginEvent.send(LoginApiResult.Success(LoginResponse("SuccessToken")))
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

}