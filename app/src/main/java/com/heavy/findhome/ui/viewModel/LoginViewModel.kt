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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.heavy.findhome.R
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.domain.AuthInteractor
import com.heavy.findhome.ui.view.DashboardActivity
import com.heavy.findhome.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel: ViewModel() {

    private val TAG = "LOGIN"
    private val GOOGLE_SIGN_IN = 100

    private var callbackManager: CallbackManager? = null
    private lateinit var  googleSingInClient:  GoogleSignInClient

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

    //SignIn Facebook
    fun mSignInWithFacebook(activity: Activity) {
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
                                _snackBar.value = result.exception.message
                            }
                            is Result.Canceled -> {
                                Log.d(TAG, "Result.Canceled")
                                _snackBar.value = result.exception?.message
                            }
                        }
                    }
                }

                override fun onError(error: FacebookException?) {
                    Log.e(TAG, "Result.Error - ${error?.message}")
                    _snackBar.value = error?.message
                }

                override fun onCancel() {
                    Log.d(TAG, "Result.Canceled")
                    _snackBar.value = activity.getString(R.string.request_cancelled)
                }
            })
        }
    }

    //Google SignIn
    fun mSignInWithGoogle(activity: Activity) {
        launchDataLoad {
            val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSingInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
            val intent = googleSingInClient.signInIntent
            activity.startActivityForResult(intent, GOOGLE_SIGN_IN)
        }
    }

    private fun mHandleSignInResult (completedTask: Task<GoogleSignInAccount>, activity: Activity) {
        viewModelScope.launch {
            try {
                val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
                account?.let {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    when(val result = obInteractor.mSignInWithCredential(credential)) {
                        is Result.Success -> {
                            Log.d(TAG, "Result.Success - ${result.data?.user?.uid}")
                            result.data?.user?.let {user ->
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
                            _snackBar.value = result.exception.message
                        }
                        is Result.Canceled -> {
                            Log.d(TAG, "Result.Canceled")
                            _snackBar.value =  activity.getString(R.string.request_cancelled)
                        }
                    }
                }
            }
            catch (exception: Exception) {
                Log.d(TAG, "Sign In Failed")
            }
        }
    }

    //Create Object
    fun createUserObject(firebaseUser: FirebaseUser, name: String, profilePicture: String = ""): User {
        val currentUser = User(
            email =  firebaseUser.uid,
            name = name,
            profilePhoto = profilePicture
        )

        return currentUser
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            mHandleSignInResult(task, activity)
        }
    }

    private fun mStartActivityDashboard(activity: Activity) {
        val goDashboard: Intent = Intent(activity, DashboardActivity::class.java)
        activity.startActivity(goDashboard)
        activity.finish()
    }

}