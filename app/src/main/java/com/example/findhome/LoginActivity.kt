package com.example.findhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.findhome.databinding.ActivityLoginBinding

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
            binding.btnLogin.id -> loadDashboard()
            else -> {
                Log.i("ERROR", "Error not controlled")
            }
        }
    }

    private fun loadDashboard() {
        val goDashboard:Intent = Intent(this, DashboardActivity::class.java)
        startActivity(goDashboard)
        finish()
    }
}