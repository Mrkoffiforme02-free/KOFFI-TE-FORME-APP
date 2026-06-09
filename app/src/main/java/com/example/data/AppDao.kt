package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Cart operations
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE courseId = :courseId")
    suspend fun deleteCartItemByCourseId(courseId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Bought courses operations
    @Query("SELECT * FROM bought_courses")
    fun getBoughtCourses(): Flow<List<BoughtCourse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoughtCourse(boughtCourse: BoughtCourse)

    @Query("SELECT EXISTS(SELECT * FROM bought_courses WHERE courseId = :courseId)")
    suspend fun isCourseBought(courseId: Int): Boolean

    // Contact Operations
    @Query("SELECT * FROM contact_messages ORDER BY timestamp DESC")
    fun getContactMessages(): Flow<List<ContactMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactMessage(message: ContactMessage)

    // Quote Operations
    @Query("SELECT * FROM quote_requests ORDER BY timestamp DESC")
    fun getQuoteRequests(): Flow<List<QuoteRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuoteRequest(quoteRequest: QuoteRequest)
}
