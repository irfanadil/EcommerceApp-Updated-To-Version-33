package com.ecommerce.testapp

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // do your work here
                loginViewModel.loginEventFlow.collect(){
                    when(it){
                        is LoginApiResult.Success->{
                            val intent = Intent(this@LoginActivity, ProductActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is LoginApiResult.Error->{
                            showLoginFailed(it.message)
                        }
                        else -> {}
                    }
                }
            }
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.testLogin(username.text.toString(), password.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoginResponse) {
         //val displayName = model.token
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}


