package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: Int,
    val title: String,
    val price: Int, // Price in XOF
    val oldPrice: Int,
    val imageUrlString: String,
    val quantity: Int = 1
)

@Entity(tableName = "bought_courses")
data class BoughtCourse(
    @PrimaryKey val courseId: Int,
    val title: String,
    val purchasedAt: Long,
    val transactionId: String
)

@Entity(tableName = "contact_messages")
data class ContactMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val timestamp: Long
)

@Entity(tableName = "quote_requests")
data class QuoteRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectType: String,
    val budget: Int, // budget in XOF
    val description: String,
    val timestamp: Long,
    val status: String = "Envoyé"
)
