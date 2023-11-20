package com.ecommerce.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var root:View
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        root = inflater.inflate(R.layout.fragment_login, container, false)

        val username = root.findViewById<EditText>(R.id.username)
        val password = root.findViewById<EditText>(R.id.password)
        val login = root.findViewById<Button>(R.id.login)
        val loading = root.findViewById<ProgressBar>(R.id.loading)

        viewLifecycleOwner.lifecycleScope.launch {
           viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // do your work here
                loginViewModel.loginEventFlow.collect(){

                    when(it){
                        is LoginApiResult.Success ->{
                            findNavController().navigate(R.id.item_listing_fragment, null)
                        }
                        is LoginApiResult.Failure->{
                            it.message?.let { it1 -> showLoginFailed(it1) }
                        }
                        else -> {}
                    }


                }
            }
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.testLogin("mor_2314", "83r5^_")
        }
        return root
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}
