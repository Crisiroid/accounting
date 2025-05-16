package com.crisiroid.accounting.local

import android.content.Context
import androidx.room.*
import com.crisiroid.accounting.local.data.Category
import com.crisiroid.accounting.local.data.Payment
import com.crisiroid.accounting.local.data.Person
import com.crisiroid.accounting.local.data.Receipt

@Database(entities = [Person::class, Payment::class, Receipt::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paymentDao(): PaymentDao
    abstract fun receiptDao(): ReceiptDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "accounting_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: Payment)

    @Query("SELECT SUM(amount) FROM Payment")
    suspend fun getTotalAmount(): Double?

    @Query("SELECT p.*, c.title AS categoryTitle FROM Payment p LEFT JOIN Category c ON p.categoryId = c.id")
    suspend fun getAllPaymentsWithCategory(): List<PaymentWithCategory>

    @Query("SELECT p.*, c.title AS categoryTitle FROM Payment p LEFT JOIN Category c ON p.categoryId = c.id WHERE p.categoryId = :categoryId")
    suspend fun getPaymentsByCategory(categoryId: Int): List<PaymentWithCategory>

    @Query("DELETE FROM Payment WHERE id = :id")
    suspend fun delete(id: Int)
}

@Dao
interface ReceiptDao {
    @Insert
    suspend fun insert(receipt: Receipt)

    @Query("SELECT SUM(amount) FROM Receipt")
    suspend fun getTotalAmount(): Double?

    @Query("SELECT r.*, c.title AS categoryTitle FROM Receipt r LEFT JOIN Category c ON r.categoryId = c.id")
    suspend fun getAllReceiptsWithCategory(): List<ReceiptWithCategory>

    @Query("SELECT r.*, c.title AS categoryTitle FROM Receipt r LEFT JOIN Category c ON r.categoryId = c.id WHERE r.categoryId = :categoryId")
    suspend fun getReceiptsByCategory(categoryId: Int): List<ReceiptWithCategory>

    @Query("DELETE FROM Receipt WHERE id = :id")
    suspend fun delete(id: Int)
}

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Delete
    suspend fun delete(category: Category)
}

// Helper data classes for joined queries
data class PaymentWithCategory(
    @Embedded val payment: Payment,
    @ColumnInfo(name = "categoryTitle") val categoryTitle: String?
)

data class ReceiptWithCategory(
    @Embedded val receipt: Receipt,
    @ColumnInfo(name = "categoryTitle") val categoryTitle: String?
)