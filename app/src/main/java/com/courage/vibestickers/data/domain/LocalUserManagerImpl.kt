package com.courage.vibestickers.data.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.courage.vibestickers.utils.Constants
import com.courage.vibestickers.utils.Constants.APP_ENTRY
import com.courage.vibestickers.utils.Constants.USER_SETTING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
): LocalUserManager {
    override suspend fun saveAppEntry() {

        // context.datastore üzerinden veri yazma işlemleri
        context.dataStore.edit { settings ->
            settings[PreferencesKey.APP_ENTRY] = true
        }
    }
    override val readAppEntry: Flow<Boolean>
        get() = context.dataStore.data.map {
            preferences ->
            preferences[booleanPreferencesKey(APP_ENTRY)] ?: false
        }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTING)

private object PreferencesKey {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
}