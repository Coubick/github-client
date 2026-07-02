package ru.example.gitsource.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveString(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun readString(key: String): String? {
        return dataStore
            .data
            .map { preferences -> preferences[stringPreferencesKey(key)] }
            .firstOrNull()
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun readBoolean(key: String): Boolean? {
        return dataStore
            .data
            .map { preferences -> preferences[booleanPreferencesKey(key)] }
            .firstOrNull()
    }

    suspend fun saveInt(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    suspend fun readInt(key: String): Int? {
        return dataStore
            .data
            .map { preferences -> preferences[intPreferencesKey(key)] }
            .firstOrNull()
    }
}