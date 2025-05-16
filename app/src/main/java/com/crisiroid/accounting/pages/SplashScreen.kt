package com.crisiroid.accounting.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crisiroid.accounting.viewmodels.SplashViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onLoginRequired: () -> Unit,
    onHome: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val token by viewModel.token.collectAsState()

    LaunchedEffect(token) {
        delay(2000)
        if (token.isNullOrEmpty()) {
            onLoginRequired()
        } else {
            onHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Accounting Application")
    }
}
