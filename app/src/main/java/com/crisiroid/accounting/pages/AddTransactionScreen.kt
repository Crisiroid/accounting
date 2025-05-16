package com.crisiroid.accounting.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.crisiroid.accounting.local.AppDatabase
import com.crisiroid.accounting.local.data.Category
import com.crisiroid.accounting.local.data.Payment
import com.crisiroid.accounting.local.data.Receipt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTransactionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Income") }
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var categories by remember { mutableStateOf(listOf<Category>()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val categoryList = database.categoryDao().getAllCategories()
                withContext(Dispatchers.Main) {
                    categories = categoryList
                    if (categoryList.isNotEmpty()) {
                        selectedCategory = categoryList.first()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5FA))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(Routes.Home.route) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Add Transaction",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Amount",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        BasicTextField(
            value = amount,
            onValueChange = {
                if (it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                    amount = it
                    errorMessage = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            Button(
                onClick = { typeExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedType,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White
                    )
                }
            }
            DropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .width(200.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Income") },
                    onClick = {
                        selectedType = "Income"
                        typeExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Spend") },
                    onClick = {
                        selectedType = "Spend"
                        typeExpanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            Button(
                onClick = { if (categories.isNotEmpty()) categoryExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                enabled = categories.isNotEmpty()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory?.title ?: "No Categories",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White
                    )
                }
            }
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .width(200.dp)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.title) },
                        onClick = {
                            selectedCategory = category
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description (Optional)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (categories.isEmpty()) {
            Text(
                text = "No categories available. Please create a category first.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (amount.isEmpty() || amount.toDoubleOrNull() == null) {
                    errorMessage = "Please enter a valid amount"
                    return@Button
                }
                if (selectedCategory == null) {
                    errorMessage = "Please select a category"
                    return@Button
                }

                val amountValue = amount.toDouble()
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())

                CoroutineScope(Dispatchers.IO).launch {
                    if (selectedType == "Income") {
                        database.receiptDao().insert(
                            Receipt(
                                amount = amountValue,
                                date = currentDate,
                                description = description.trim(),
                                categoryId = selectedCategory!!.id
                            )
                        )
                    } else {
                        database.paymentDao().insert(
                            Payment(
                                amount = amountValue,
                                date = currentDate,
                                description = description.trim(),
                                categoryId = selectedCategory!!.id
                            )
                        )
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("Transaction")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text(
                text = "Add",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}