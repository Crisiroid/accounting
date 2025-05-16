package com.crisiroid.accounting.pages

import Routes
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.crisiroid.accounting.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()
    var incomeTotal by remember { mutableStateOf(0.0) }
    var spendTotal by remember { mutableStateOf(0.0) }
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val incomeSum = database.receiptDao().getTotalAmount() ?: 0.0
                val spendSum = database.paymentDao().getTotalAmount() ?: 0.0
                withContext(Dispatchers.Main) {
                    incomeTotal = incomeSum
                    spendTotal = spendSum
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome Name", fontSize = 24.sp)
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Income",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "$${numberFormat.format(incomeTotal)}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Costs",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "$${numberFormat.format(spendTotal)}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(0.8f))
        Column(modifier = Modifier.padding(horizontal = 26.dp)) {
            Button(
                onClick = { navController.navigate(Routes.Transaction.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Add", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Routes.Categories.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCA28))
            ) {
                Text("Categories", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Routes.History.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("History", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
            ) {
                Text("Exit", color = Color.White)
            }
        }
    }
}