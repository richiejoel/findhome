package com.heavy.findhome.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.domain.AuthInteractor
import kotlinx.coroutines.launch

class SplashScreenViewModel: ViewModel() {

    val obInteractor: AuthInteractor = AuthInteractor()

    fun mCheckUserLoggedIn(): FirebaseUser?
    {
        var _firebaseUser: FirebaseUser? = null
        viewModelScope.launch {
            _firebaseUser = obInteractor.mCheckUserLoggedIn()
        }
        return _firebaseUser
    }
}