package com.heavy.findhome.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.R
import com.heavy.findhome.databinding.ActivitySplashScreenBinding
import com.heavy.findhome.ui.viewModel.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val splashScreenViewModel:SplashScreenViewModel by viewModels()
    private var currentFirebaseUser: FirebaseUser? = null

    private val obHandler:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide action bar
        supportActionBar?.hide()
    }

    private fun mChangeColorStatusBar(){
        window?.statusBarColor = ContextCompat.getColor(applicationContext, R.color.colorWhite)
    }

    private val mRunnable = Runnable {
        currentFirebaseUser = splashScreenViewModel.mCheckUserLoggedIn()
        if(!isFinishing){
            if(currentFirebaseUser == null){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                currentFirebaseUser?.let {
                    Log.i("AUTH", it.uid)
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        obHandler.postDelayed(mRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        obHandler.removeCallbacks(mRunnable)
    }
}