package ru.example.gitsource.data.auth

import ru.example.gitsource.data.local.LocalDataStore
import javax.inject.Inject

internal class TokenManager @Inject constructor(private val localDataStore: LocalDataStore) {
    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
    }

    suspend fun saveToken(token: String){
        localDataStore.saveString(
            key = KEY_ACCESS_TOKEN,
            value = token
        )
    }

    suspend fun removeToken(){
        localDataStore.removeStringKey(KEY_ACCESS_TOKEN)
    }

    suspend fun getToken() : String? {
        return localDataStore.readString(KEY_ACCESS_TOKEN)
    }
}