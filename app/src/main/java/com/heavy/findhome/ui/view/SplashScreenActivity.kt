package com.heavy.findhome.ui.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.R
import com.heavy.findhome.databinding.ActivitySplashScreenBinding
import com.heavy.findhome.ui.viewModel.LoginViewModel
import com.heavy.findhome.ui.viewModel.SplashScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val splashScreenViewModel:SplashScreenViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private var currentFirebaseUser: FirebaseUser? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    private val obHandler:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Hide action bar
        supportActionBar?.hide()
        printKeyHash()

        coroutineScope.launch {
            delay(3_000)
            currentFirebaseUser = splashScreenViewModel.mCheckUserLoggedIn()
            if(currentFirebaseUser == null){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                currentFirebaseUser?.let {
                    Log.i("JOEL", it.email)
                    loginViewModel.mGetUserFromFirestore(it.email!!, this@SplashScreenActivity)
                    startActivity(DashboardActivity())
                }
            }

        }

    }

    private fun startActivity(activity: Activity)
    {
        val intentDashboard = Intent(this@SplashScreenActivity, activity::class.java)
        intentDashboard.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentDashboard)
        //finish()
    }

    private fun mChangeColorStatusBar(){
        window?.statusBarColor = ContextCompat.getColor(applicationContext, R.color.colorWhite)
    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo("com.heavy.findhome", PackageManager.GET_SIGNATURES)
            for(signature in info.signatures) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())
                Log.i("printKeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT))
            }
        } catch (exception: PackageManager.NameNotFoundException) {
            Log.e("ERROR", exception.toString() )
        }
        catch (exception: NoSuchAlgorithmException) {
            Log.e("ERROR", exception.toString() )
        }
    }

    /*private val mRunnable = Runnable {
        currentFirebaseUser = splashScreenViewModel.mCheckUserLoggedIn()
        if(!isFinishing){
            if(currentFirebaseUser == null){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                currentFirebaseUser?.let {
                    Log.i("AUTH", it.uid)
                    loginViewModel.mGetUserFromFirestore(it.email!!, this@SplashScreenActivity)
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                    finish()
                }
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        //obHandler.postDelayed(mRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        //obHandler.removeCallbacks(mRunnable)
    }
}