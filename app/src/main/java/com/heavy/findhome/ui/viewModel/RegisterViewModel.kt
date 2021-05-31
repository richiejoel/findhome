package com.heavy.findhome.ui.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heavy.findhome.R
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.domain.AuthInteractor
import com.heavy.findhome.ui.view.DashboardActivity
import com.heavy.findhome.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {

    private val _currentUser = MutableLiveData<User>(User())
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    val obInteractor: AuthInteractor = AuthInteractor()

    fun mCreateUserWithEmailAndPassword(user: User, activity: Activity){
        launchDataLoad {
            viewModelScope.launch {
                val obResultSignUp =
                    obInteractor.mCreateUserWithEmailAndPassword(user, activity.applicationContext)
                when (obResultSignUp) {
                    is Result.Success -> {
                        _snackBar.value = activity.getString(R.string.login_successful)
                        _currentUser.value = user
                        mStartActivityDashboard(activity)
                    }
                    is Result.Canceled -> {
                        _snackBar.value = activity.getString(R.string.request_cancelled)
                    }
                    is Result.Error -> {
                        _snackBar.value = obResultSignUp.exception.message
                    }
                }
            }
        }
    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            }
            catch (error: Throwable) {
                _snackBar.value = error.message
            }
            finally {
                _spinner.value = false
            }
        }
    }

    private fun mStartActivityDashboard(activity: Activity) {
        val goDashboard: Intent = Intent(activity, DashboardActivity::class.java)
        activity.startActivity(goDashboard)
        activity.finish()
    }
}