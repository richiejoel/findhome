package com.heavy.findhome.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.heavy.findhome.R
import com.heavy.findhome.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val obHandler:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash_screen)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide action bar
        supportActionBar?.hide()
    }

    private fun mChangeColorStatusBar(){
        window?.statusBarColor = ContextCompat.getColor(applicationContext, R.color.colorWhite)
    }

    private val mRunnable = Runnable {
        if(!isFinishing){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
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