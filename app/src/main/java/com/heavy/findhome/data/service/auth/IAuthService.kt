package com.heavy.findhome.data.service.auth

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.utils.Result


interface IAuthService {

    suspend fun mSignUpUserWithEmail(user: User, context: Context): Result<FirebaseUser?>
    suspend fun mSignInUserWithEmail(user: User): Result<FirebaseUser?>
    suspend fun mGetUserFromFirestore(userId: String): Result<User>?
    suspend fun mAddUserFirestore(user:User): Result<Void?>
    suspend fun mCheckUserLoggedIn(): FirebaseUser?
    suspend fun mLogOutUser(provider: String?)
    suspend fun mSignInWithCredential(authCredential: AuthCredential): Result<AuthResult?>

}