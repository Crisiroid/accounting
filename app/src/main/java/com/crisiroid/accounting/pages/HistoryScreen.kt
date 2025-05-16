package com.crisiroid.accounting.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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
import com.crisiroid.accounting.local.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

// Data class to unify Payment and Receipt for display
data class Transaction(
    val id: Int,
    val type: String, // "Income" or "Spend"
    val amount: Double,
    val date: String,
    val description: String,
    val categoryId: Int,
    val categoryTitle: String
)

@Composable
fun HistoryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()

    // State variables
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var categories by remember { mutableStateOf(listOf<Category>()) }
    var selectedType by remember { mutableStateOf("All") }
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }

    // Number formatter for amounts
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    // Load transactions and categories
    LaunchedEffect(selectedType, selectedCategory) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                // Fetch categories
                val categoryList = database.categoryDao().getAllCategories()
                // Fetch transactions based on filters
                val paymentList = when {
                    selectedType == "Income" -> emptyList()
                    selectedCategory != null -> database.paymentDao().getPaymentsByCategory(selectedCategory!!.id)
                    else -> database.paymentDao().getAllPaymentsWithCategory()
                }
                val receiptList = when {
                    selectedType == "Spend" -> emptyList()
                    selectedCategory != null -> database.receiptDao().getReceiptsByCategory(selectedCategory!!.id)
                    else -> database.receiptDao().getAllReceiptsWithCategory()
                }
                // Combine and convert to Transaction
                val combinedList = (paymentList.map {
                    Transaction(
                        id = it.payment.id,
                        type = "Spend",
                        amount = it.payment.amount,
                        date = it.payment.date,
                        description = it.payment.description,
                        categoryId = it.payment.categoryId,
                        categoryTitle = it.categoryTitle ?: "Unknown"
                    )
                } + receiptList.map {
                    Transaction(
                        id = it.receipt.id,
                        type = "Income",
                        amount = it.receipt.amount,
                        date = it.receipt.date,
                        description = it.receipt.description,
                        categoryId = it.receipt.categoryId,
                        categoryTitle = it.categoryTitle ?: "Unknown"
                    )
                }).sortedByDescending { it.date } // Sort by date (newest first)
                withContext(Dispatchers.Main) {
                    categories = categoryList
                    transactions = combinedList
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
        // Header with back button
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
                text = "Transaction History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Type Filter Dropdown
        Box {
            Button(
                onClick = { typeExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Type: $selectedType",
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
                listOf("All", "Income", "Spend").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            typeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Filter Dropdown
        Box {
            Button(
                onClick = { categoryExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category: ${selectedCategory?.title ?: "All"}",
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
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        selectedCategory = null
                        categoryExpanded = false
                    }
                )
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

        // Transaction List
        if (transactions.isEmpty()) {
            Text(
                text = "No transactions found.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(transactions) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Amount: $${numberFormat.format(transaction.amount)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Type: ${transaction.type}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Category: ${transaction.categoryTitle}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Date: ${transaction.date}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                if (transaction.description.isNotBlank()) {
                                    Text(
                                        text = "Description: ${transaction.description}",
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            if (transaction.type == "Income") {
                                                database.receiptDao().delete(transaction.id)
                                            } else {
                                                database.paymentDao().delete(transaction.id)
                                            }
                                            // Refresh transactions
                                            val paymentList = when {
                                                selectedType == "Income" -> emptyList()
                                                selectedCategory != null -> database.paymentDao().getPaymentsByCategory(selectedCategory!!.id)
                                                else -> database.paymentDao().getAllPaymentsWithCategory()
                                            }
                                            val receiptList = when {
                                                selectedType == "Spend" -> emptyList()
                                                selectedCategory != null -> database.receiptDao().getReceiptsByCategory(selectedCategory!!.id)
                                                else -> database.receiptDao().getAllReceiptsWithCategory()
                                            }
                                            val combinedList = (paymentList.map {
                                                Transaction(
                                                    id = it.payment.id,
                                                    type = "Spend",
                                                    amount = it.payment.amount,
                                                    date = it.payment.date,
                                                    description = it.payment.description,
                                                    categoryId = it.payment.categoryId,
                                                    categoryTitle = it.categoryTitle ?: "Unknown"
                                                )
                                            } + receiptList.map {
                                                Transaction(
                                                    id = it.receipt.id,
                                                    type = "Income",
                                                    amount = it.receipt.amount,
                                                    date = it.receipt.date,
                                                    description = it.receipt.description,
                                                    categoryId = it.receipt.categoryId,
                                                    categoryTitle = it.categoryTitle ?: "Unknown"
                                                )
                                            }).sortedByDescending { it.date }
                                            withContext(Dispatchers.Main) {
                                                transactions = combinedList
                                            }
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Transaction",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}