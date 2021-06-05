package com.heavy.findhome.data.service.auth

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.core.FirebaseHelper
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.utils.Constants.USER_COLLECTION
import com.heavy.findhome.utils.extension.await
import com.heavy.findhome.utils.Result

class AuthService : IAuthService {

    private val firestore = FirebaseHelper.getFirestoreConnection()
    private val firebaseAuth = FirebaseHelper.getFirebaseAuth()
    private val userCollection = firestore.collection(USER_COLLECTION)

    private val TAG ="AUTH"

    //Add user Firestore
    override suspend fun mAddUserFirestore(user: User): Result<Void?> {
        return try {
            userCollection.document(user.email)
                .set(
                    hashMapOf(
                        "username" to user.username,
                        "email" to user.email,
                        "name" to user.name,
                        "lastname" to user.lastname,
                        "provider" to user.provider
                    )
                ).await()
        } catch (e: Exception) {
            Log.i("Error", "Error no controlado")
            Result.Error(e)
        }
    }

    //Sign Up Firebase Auth
    override suspend fun mSignUpUserWithEmail(user: User, context: Context): Result<FirebaseUser?> {
        try {
            val resultSignUp = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            return when (resultSignUp){
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultSignUp.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultSignUp.exception}")
                    Result.Error(resultSignUp.exception)
                }
                is Result.Canceled ->  {
                    Log.e(TAG, "${resultSignUp.exception}")
                    Result.Canceled(resultSignUp.exception)
                }
            }

        } catch (exception: Exception){
            return Result.Error(exception)
        }
    }

    //Sign In Firebase Auth
    override suspend fun mSignInUserWithEmail(user:User): Result<FirebaseUser?> {
        try{
            val resultSignIn = firebaseAuth.signInWithEmailAndPassword(user.email, user.password).await()
            return when(resultSignIn){
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultSignIn.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultSignIn.exception}")
                    Result.Error(resultSignIn.exception)
                }
                is Result.Canceled -> {
                    Log.e(TAG, "${resultSignIn.exception}")
                    Result.Canceled(resultSignIn.exception)
                }
            }
        } catch(exception: Exception){
            return Result.Error(exception)
        }

    }

    override suspend fun mCheckUserLoggedIn(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun mGetUserFromFirestore(userId: String): Result<User>? {
        try {
            val resultUser = userCollection.document(userId).get().await()
            return when(resultUser){
                is Result.Success -> {
                    val user = resultUser.data.toObject(User::class.java)!!
                    Result.Success(user)
                }
                is Result.Error -> {
                    Result.Error(resultUser.exception)
                }
                is Result.Canceled -> {
                    Result.Canceled(resultUser.exception)
                }
            }

        } catch (exception: Exception){
            return Result.Error(exception)
        }
    }

    override suspend fun mLogOutUser(){
        firebaseAuth.signOut()
    }

}