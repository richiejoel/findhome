package com.heavy.findhome.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.heavy.findhome.databinding.ActivityLoginBinding
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.ui.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel:LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.txtLoginAccount.setOnClickListener(this)
        binding.sesionFacebook.setOnClickListener(this)
        binding.sesionGoogle.setOnClickListener(this)

        //Hide action bar
        supportActionBar?.hide()
        //project-828857439006
        loginViewModel.snackBar.observe(this, Observer {
            it?.let {
                Snackbar.make(binding.root, it!!, Snackbar.LENGTH_LONG).show()
                loginViewModel.onSnackBarShown()
            }
        })

    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.btnLogin.id -> mLoginWithUserAndPassword()
            binding.txtLoginAccount.id -> mStartRegisterActivity()
            binding.sesionFacebook.id -> mLoginWithFacebook()
            binding.sesionGoogle.id -> mLoginWithGoogle()
            else -> {
                Log.i("ERROR", "Error not controlled")
            }
        }
    }

    private fun mLoginWithUserAndPassword(){
        val user:User = User()
        user.email = binding.edtEmail.text.toString()
        user.password = binding.edtPassword.text.toString()
        if(mValidateEmail() && mValidatePassword()){
            loginViewModel.mSignInUserWithEmailAndPassword(user, this)
        }

    }

    private fun mLoginWithFacebook(){
        loginViewModel.mSignInWithFacebook(this)
    }

    private fun mLoginWithGoogle(){
        loginViewModel.mSignInWithGoogle(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.onActivityResult(requestCode, resultCode, data, this)
    }

    private fun mStartRegisterActivity(){
        val intentRegister:Intent = Intent(this, RegisterActivity::class.java)
        startActivity(intentRegister)
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