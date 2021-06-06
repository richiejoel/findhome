package com.heavy.findhome.domain


import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.heavy.findhome.data.model.entity.User
import com.heavy.findhome.data.repository.AuthRepository
import com.heavy.findhome.utils.Result

class AuthInteractor {

    private val obRepository = AuthRepository()

    suspend fun mCreateUserWithEmailAndPassword(user: User, context: Context): Result<FirebaseUser?> {
        val obResult = obRepository.mSignUpUserWithEmail(user, context)
        return when(obResult){
            is Result.Success -> {
               obResult.data?.let { firebaseUser ->
                    obRepository.mAddUserFirestore(user)
                }
               Result.Success(obResult.data)
            }
            is Result.Canceled -> {
                Result.Canceled(obResult.exception)
            }
            is Result.Error -> {
                Result.Error(obResult.exception)
            }
        }
    }

    suspend fun mSignInWithEmailAndPassword(user: User): Result<FirebaseUser?> {
        return obRepository.mSignInUserWithEmail(user)
    }

    suspend fun mCheckUserLoggedIn(): FirebaseUser? {
        return obRepository.mCheckUserLoggedIn()
    }

    suspend fun mGetUserFromFirestore(userId: String): Result<User>? {
        return obRepository.mGetUserFromFirestore(userId)
    }

    suspend fun mLogOutUser(provider: String?){
        obRepository.mLogOutUser(provider)
    }

    suspend fun mSignInWithCredential(authCredential: AuthCredential): Result<AuthResult?> {
         return obRepository.mSignInWithCredential(authCredential)
    }

    suspend fun mAddUserFirestore(user: User): Result<Void?>{
        return  obRepository.mAddUserFirestore(user)
    }

}