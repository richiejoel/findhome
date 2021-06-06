package com.heavy.findhome.data.repository

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.data.service.auth.AuthService
import com.heavy.findhome.utils.Result
import com.heavy.findhome.utils.extension.await

class AuthRepository {

    private val obService = AuthService()

    suspend fun mSignUpUserWithEmail(user: User, context: Context):Result<FirebaseUser?>{
        return obService.mSignUpUserWithEmail(user, context)
    }

    suspend fun mAddUserFirestore(user: User): Result<Void?>{
        return obService.mAddUserFirestore(user)
    }

    suspend fun mSignInUserWithEmail(user: User):Result<FirebaseUser?>{
        return obService.mSignInUserWithEmail(user)
    }

    suspend fun mCheckUserLoggedIn(): FirebaseUser? {
        return obService.mCheckUserLoggedIn()
    }

    suspend fun mGetUserFromFirestore(userId: String): Result<User>? {
        return obService.mGetUserFromFirestore(userId)
    }

    suspend fun mLogOutUser(provider: String?){
        obService.mLogOutUser(provider)
    }

    suspend fun mSignInWithCredential(authCredential: AuthCredential): Result<AuthResult?> {
        return obService.mSignInWithCredential(authCredential)
    }

}