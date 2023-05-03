package com.azuredragon.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.azuredragon.app.R
import com.azuredragon.app.databinding.ActivityMainBinding
import com.azuredragon.core.ui.gone
import com.azuredragon.core.ui.show

class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                com.azuredragon.home.presentation.R.id.homeFragment -> {
                    mainBinding.bottomNavigationView.show()
                }
                else -> {
                    mainBinding.bottomNavigationView.gone()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}