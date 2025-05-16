package com.crisiroid.accounting.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crisiroid.accounting.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    // State for username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5FF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.balloon),
            contentDescription = "Balloon",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Login",
            fontSize = 28.sp,
            color = Color(0xFF3F51B5),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Username field with label
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
                value = username, // Bind to state
                onValueChange = { username = it }, // Update state on change
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

        // Password field with label
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
                value = password, // Bind to state
                onValueChange = { password = it }, // Update state on change
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
            onClick = { /* Handle Login action with username and password */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Register", color = Color(0xFF3F51B5), fontSize = 16.sp)
    }
}