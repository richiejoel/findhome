package com.heavy.findhome.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heavy.findhome.domain.AuthInteractor
import kotlinx.coroutines.launch

class DashboardViewModel: ViewModel() {

    val obInteractor: AuthInteractor = AuthInteractor()

    fun mLogOutUser(){
        viewModelScope.launch {
            obInteractor.mLogOutUser()
        }
    }
}