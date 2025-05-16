package com.crisiroid.accounting.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun CategoriesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()

    // State for text field and categories
    var categoryTitle by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf(listOf<Category>()) }

    // Load categories when screen is created
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val categoryList = database.categoryDao().getAllCategories()
                withContext(Dispatchers.Main) {
                    categories = categoryList
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
                text = "Manage Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        // Title


        // Category Title Text Field
        Text(
            text = "Category Title",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BasicTextField(
            value = categoryTitle,
            onValueChange = {
                categoryTitle = it
                errorMessage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Category Button
        Button(
            onClick = {
                if (categoryTitle.isBlank()) {
                    errorMessage = "Please enter a category title"
                    return@Button
                }
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        database.categoryDao().insert(
                            Category(title = categoryTitle.trim())
                        )
                        val updatedCategories = database.categoryDao().getAllCategories()
                        withContext(Dispatchers.Main) {
                            categories = updatedCategories
                            categoryTitle = "" // Clear text field
                        }
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
                text = "Create Category",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category List
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    database.categoryDao().delete(category)
                                    val updatedCategories = database.categoryDao().getAllCategories()
                                    withContext(Dispatchers.Main) {
                                        categories = updatedCategories
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Category",
                            tint = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = category.title,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 1.dp)
            }
        }
    }
}