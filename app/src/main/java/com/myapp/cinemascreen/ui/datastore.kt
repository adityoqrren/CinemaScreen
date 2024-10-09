package com.myapp.cinemascreen.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.cinemaDataStore: DataStore<Preferences> by preferencesDataStore(
    // just my preference of naming including the package name
    name = "com.example.myappname.user_preferences"
)

object PreferencesKey{
    val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
    val EMAIL_LOGIN = stringPreferencesKey("EMAIL_LOGIN")
    val USERNAME_LOGIN = stringPreferencesKey("USERNAME_LOGIN")
}

@Module
@InstallIn(SingletonComponent::class)
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context){

    //read preference
    val isLogin: Flow<Boolean> = context.cinemaDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[PreferencesKey.IS_LOGIN] ?: false
        }

    val emailLogin: Flow<String> = context.cinemaDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[PreferencesKey.EMAIL_LOGIN] ?: ""
        }

    val usernameLogin: Flow<String> = context.cinemaDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[PreferencesKey.USERNAME_LOGIN] ?: ""
        }

    //write preference
    suspend fun saveUserData(isLogin: Boolean, email: String, username: String){
        context.cinemaDataStore.edit { settings ->
            settings[PreferencesKey.IS_LOGIN] = isLogin
            settings[PreferencesKey.EMAIL_LOGIN] = email
            settings[PreferencesKey.USERNAME_LOGIN] = username
        }
    }
}
