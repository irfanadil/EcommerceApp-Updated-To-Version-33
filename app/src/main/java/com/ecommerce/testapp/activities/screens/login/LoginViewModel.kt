package com.ecommerce.testapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepo: LoginRepo) : ViewModel() {

    private val loginEvent = Channel<LoginApiResult>()
    // Receiving channel as a flow
    val loginEventFlow = loginEvent.receiveAsFlow()

    // I need to add job because viewmodelScope.launch was not working for the second time...
    // https://stackoverflow.com/questions/63776069/cancel-viewmodelscope-and-re-use-it-later-on
    // https://stackoverflow.com/questions/66959931/viewmodelscope-launch-does-not-work-for-second-time
    // https://stackoverflow.com/questions/59145183/cant-use-livedata-or-viewmodelscope-launch
    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    fun testLogin(username: String, password: String){
        viewModelScope.launch(job) {
            val response = loginRepo.login(username, password)
            if(response is LoginApiResult.Success)
                loginEvent.send(LoginApiResult.Success(response.data))
            else
                loginEvent.send(LoginApiResult.Failure(message = "Login call failed..."))
        }
    }


    private var logoutChannel: Channel<Boolean> = Channel()
    val logoutEvent = logoutChannel.receiveAsFlow()

    fun logoutUser()
    {
        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    loginRepo.logout()
                }
                Log.e("LogOut C - ", "logoutCompleted.")
                logoutChannel.send(true)
            } catch (_: Exception) { }
        }
    }

}