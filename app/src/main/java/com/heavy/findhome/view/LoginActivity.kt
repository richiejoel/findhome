package com.heavy.findhome.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.heavy.findhome.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        //Hide action bar
        supportActionBar?.hide()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.btnLogin.id -> mLoadDashboard()
            else -> {
                Log.i("ERROR", "Error not controlled")
            }
        }
    }

    private fun mLoadDashboard() {
        val goDashboard:Intent = Intent(this, DashboardActivity::class.java)
        startActivity(goDashboard)
        finish()
    }
}