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

class LoginViewModel: ViewModel() {

    //val user = MutableLiveData<User>()

    private val TAG = "LOGIN"

    private val _currentUser = MutableLiveData<User>(User())
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    val obInteractor: AuthInteractor = AuthInteractor()

    fun mSignInUserWithEmailAndPassword(user: User, activity: Activity){
        launchDataLoad {
            viewModelScope.launch {
                val obResultSignIn = obInteractor.mSignInWithEmailAndPassword(user)
                when (obResultSignIn) {
                    is Result.Success -> {
                        obResultSignIn.data?.let {
                            _snackBar.value = activity.getString(R.string.login_successful)
                            mGetUserFromFirestore(it.email!!, activity)
                        }

                    }
                    is Result.Canceled -> {
                        _snackBar.value = activity.getString(R.string.request_cancelled)
                    }
                    is Result.Error -> {
                        _snackBar.value = obResultSignIn.exception.message
                    }
                }
            }
        }
    }

    suspend fun mGetUserFromFirestore(userId:String, activity: Activity){
        val user = obInteractor.mGetUserFromFirestore(userId)
        when (user){
            is Result.Success -> {
                val _user = user.data
                Log.d("Goku2", "${user.data}")
                _currentUser.value = _user
                mStartActivityDashboard(activity = activity)
            }
            is Result.Error -> {
                _snackBar.value = user.exception.message
            }
            is Result.Canceled -> {
                _snackBar.value = activity.getString(R.string.request_cancelled)
            }
        }
    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                block()
            }
            catch (error: Throwable) {
                _snackBar.value = error.message
            }
            finally {
                Log.i("","")
            }
        }
    }

    private fun mStartActivityDashboard(activity: Activity) {
        val goDashboard: Intent = Intent(activity, DashboardActivity::class.java)
        activity.startActivity(goDashboard)
        activity.finish()
    }

}