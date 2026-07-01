package ru.example.gitsource.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveString(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    fun readString(key: String): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    fun readBoolean(key: String): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)]
        }
    }

    suspend fun saveInt(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    fun readInt(key: String): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)]
        }
    }
}