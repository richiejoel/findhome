package com.heavy.findhome.ui.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.R
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.domain.AuthInteractor
import com.heavy.findhome.ui.view.DashboardActivity
import com.heavy.findhome.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel: ViewModel() {

    //val user = MutableLiveData<User>()

    private val TAG = "LOGIN"
    private var callbackManager: CallbackManager? = null

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

    fun signInWithFacebook(activity: Activity) {
        launchDataLoad {
            callbackManager = CallbackManager.Factory.create()

            LoginManager.getInstance()
                .logInWithReadPermissions(
                    activity,
                    Arrays.asList("email", "public_profile"))

            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
                    viewModelScope.launch {
                        when(val result = obInteractor.mSignInWithCredential(credential)) {
                            is Result.Success -> {
                                Log.d(TAG, "Result.Success - ${result.data?.user?.uid}")
                                result.data?.user?.let { user ->
                                    val _user = user.displayName?.let {
                                        createUserObject(
                                            user,
                                            it
                                        )
                                    }
                                    _user?.let {
                                        obInteractor.mAddUserFirestore(_user)
                                    }
                                }
                                _snackBar.value = activity.getString(R.string.login_successful)
                                mStartActivityDashboard(activity = activity)
                            }
                            is Result.Error -> {
                                Log.e(TAG, "Result.Error - ${result.exception.message}")
                                //_toast.value = result.exception.message
                            }
                            is Result.Canceled -> {
                                Log.d(TAG, "Result.Canceled")
                                //_toast.value = activity.applicationContext.getString(R.string.request_canceled)
                            }
                        }
                    }
                }

                override fun onError(error: FacebookException?) {
                    Log.e(TAG, "Result.Error - ${error?.message}")
                    //_toast.value = error?.message
                }

                override fun onCancel() {
                    Log.d(TAG, "Result.Canceled")
                    //_toast.value = activity.applicationContext.getString(R.string.request_canceled)
                }
            })
        }
    }


    fun createUserObject(firebaseUser: FirebaseUser, name: String, profilePicture: String = ""): User {
        val currentUser = User(
            email =  firebaseUser.uid,
            name = name,
            profilePhoto = profilePicture
        )

        return currentUser
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        callbackManager?.onActivityResult(
            requestCode,
            resultCode,
            data)
    }

    private fun mStartActivityDashboard(activity: Activity) {
        val goDashboard: Intent = Intent(activity, DashboardActivity::class.java)
        activity.startActivity(goDashboard)
        activity.finish()
    }

}