package com.crisiroid.accounting.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crisiroid.accounting.local.TokenManager
import com.crisiroid.accounting.api.ApiService
import com.crisiroid.accounting.api.RegisterRequest
import com.crisiroid.accounting.api.RetrofitClient
import kotlinx.coroutines.launch
import java.util.UUID
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    tokenManager: TokenManager = TokenManager(LocalContext.current),
    apiService: ApiService = RetrofitClient.apiService,
    navController: NavHostController
) {
    var fullName by remember { mutableStateOf("") }
    var ssn by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5FF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Register",
            fontSize = 28.sp,
            color = Color(0xFF3F51B5),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Full Name Field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Full Name",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                shape = CircleShape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SSN Field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Social Security Number",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = ssn,
                onValueChange = { ssn = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                shape = CircleShape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Username",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                shape = CircleShape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Password",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                shape = CircleShape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                if (fullName.isNotBlank() && ssn.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            val response = apiService.register(
                                RegisterRequest(fullName, ssn, username, password)
                            )
                            if (response.code() == 200) {
                                val token = UUID.randomUUID().toString()
                                tokenManager.saveToken(token)
                                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate(Routes.Login.route)
                            } else {
                                Toast.makeText(context, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Register", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Login",
            color = Color(0xFF3F51B5),
            fontSize = 16.sp,
            modifier = Modifier
                .clickable {
                    navController.navigate(Routes.Login.route)
                })
    }
}