package com.ecommerce.testapp.activities


import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.ecommerce.testapp.AppConstant
import com.ecommerce.testapp.LoginViewModel
import com.ecommerce.testapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController:NavController

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        navController = host.navController

        val inflater = host.navController.navInflater
        val graph = inflater.inflate(R.navigation.mobile_navigation)


        if(sharedPreferences.getBoolean(AppConstant.IS_LOGGED_IN, false))
            graph.setStartDestination(R.id.item_listing_fragment)

        navController.graph = graph

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            Log.d("NavigationActivity", "Navigated to $dest")
        }

        observeLogout()
    }

    private val loginViewModel: LoginViewModel by viewModels()

    private fun observeLogout()
    {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                loginViewModel.logoutEvent.collect() {
                    Log.e("Now observing- ", "observeLogout")
                    if (it) {
                        navController.navigate(R.id.login_dest, null)
                    } else
                        Toast.makeText(this@MainActivity, "User can not logout...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration
    ) {
        // TODO STEP 9.6 - Have NavigationUI handle what your ActionBar displays
//        // This allows NavigationUI to decide what label to show in the action bar
//        // By using appBarConfig, it will also determine whether to
//        // show the up arrow or drawer menu icon
//        setupActionBarWithNavController(navController, appBarConfig)
        // TODO END STEP 9.6
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        //val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        //if (navigationView == null) {
           // menuInflater.inflate(R.menu.overflow_menu, menu)
            //return true
        //}
        return retValue
    }


    override fun onSupportNavigateUp(): Boolean {
        // TODO STEP 9.7 - Have NavigationUI handle up behavior in the ActionBar
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
   }

}


