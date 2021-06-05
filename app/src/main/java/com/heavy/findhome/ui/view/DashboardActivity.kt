package com.heavy.findhome.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.heavy.findhome.R
import com.heavy.findhome.databinding.ActivityDashboardBinding
import com.heavy.findhome.ui.viewModel.LoginViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //Hide action bar
        supportActionBar?.hide()

        loginViewModel.currentUser.observe(this, Observer { user ->
            //binding.txtUsername.text = it?.email
            var este = user.name
            Log.i("Gola", "That -> " + este)
            user?.let {
                var hola = it?.email
                Log.i("Gola", "This -> $hola " + hola)
            }
        })

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_ubication, R.id.navigation_chat
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}