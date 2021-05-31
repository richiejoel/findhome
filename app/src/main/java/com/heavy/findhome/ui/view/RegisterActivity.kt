package com.heavy.findhome.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.databinding.ActivityRegisterBinding
import com.heavy.findhome.ui.viewModel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding:ActivityRegisterBinding

    private val registerViewModel:RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener(this)
        binding.txtRegisterLogin.setOnClickListener(this)
        //Hide action bar
        supportActionBar?.hide()

        registerViewModel.snackBar.observe(this, Observer {
            it?.let {
                Snackbar.make(binding.root, it!!, Snackbar.LENGTH_LONG).show()
                registerViewModel.onSnackBarShown()
            }
        })

        registerViewModel.spinner.observe(this, Observer {
            it?.let { show ->
                binding.spinnerRegister.visibility = if(show) View.VISIBLE else View.GONE
            }
        })

    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.btnRegister.id -> mSignUpWithUserAndPassword()
            binding.txtRegisterLogin.id -> mStartLoginActivity()
            else -> {
                Log.i("ERROR", "Error not controlled")
            }
        }
    }

    private fun mSignUpWithUserAndPassword(){
        val user: User = User()
        user.email = binding.edtEmail.text.toString()
        user.password = binding.edtPassword.text.toString()
        user.lastname = "Gracia"
        user.name = "Michel"
        user.username = "michie00"
        user.provider = "EMAIL"
        user.profilePhoto = "path/"
        if(mValidateEmail() && mValidatePassword()){
            registerViewModel.mCreateUserWithEmailAndPassword(user, this)
        }
    }

    private fun mStartLoginActivity(){
        val intentLogin:Intent = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }

    private fun mValidateName(): Boolean {
        val name = binding.edtEmail.text.toString().trim()

        return if(name.length < 6) {
            binding.edtEmail.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun mValidateEmail(): Boolean {
        val email = binding.edtEmail.text.toString().trim()

        return if(!email.contains("@") && !email.contains(".")) {
            binding.edtEmail.error = "Enter a valid email"
            false
        }
        else if (email.length < 6) {
            binding.edtEmail.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun mValidatePassword(): Boolean {
        val password = binding.edtPassword.text.toString().trim()

        return if(password.length < 6) {
            binding.edtPassword.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

}