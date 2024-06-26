package com.dynamicdal.dictionary.dataStore

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.dynamicdal.dictionary.presentation.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore
@Inject
constructor(
    app: BaseApplication
){
    private val datastore: DataStore<Preferences> = app.createDataStore(
        name = "settings"
    )

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        observeDataStore()
    }

    val showSplashScreen = mutableStateOf(true)
    private val onboardingComplete = mutableStateOf(false)
    private val isDark = mutableStateOf(true)
    private val userName = mutableStateOf("")

    fun getOnboardingCompleteValue(): MutableState<Boolean> {
        return onboardingComplete
    }
    fun getIsDarkValue(): MutableState<Boolean> {
        return isDark
    }
    fun getUserName(): MutableState<String> {
        return userName
    }

    fun toggleTheme(){
        scope.launch {
            datastore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY]?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    fun setUserName(name: String){
        scope.launch {
            datastore.edit { preferences ->
                preferences[USER_NAME_KEY] = name
            }
        }
    }

    fun setOnboardingComplete(){
        scope.launch {
            datastore.edit { preferences ->
                preferences[ONBOARDING_KEY] = true
            }
        }
    }

    private fun observeDataStore(){

        datastore.data.onEach { preferences ->

            showSplashScreen.value = true // just to be double sure

            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark.value = isDarkTheme
            }

            preferences[ONBOARDING_KEY]?.let { isOnboardingComplete ->
                onboardingComplete.value = isOnboardingComplete
            }

            preferences[USER_NAME_KEY]?.let { name ->
                userName.value = name
            }

            showSplashScreen.value = false

        }.launchIn(scope)


    }

    companion object{
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_key")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }
}