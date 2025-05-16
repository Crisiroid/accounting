package com.crisiroid.accounting.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.crisiroid.accounting.local.AppDatabase
import com.crisiroid.accounting.local.TokenManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val tokenManager = TokenManager(context)

    val token: StateFlow<String?> = tokenManager.tokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val db: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }
}
