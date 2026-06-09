package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    val cartItems: Flow<List<CartItem>> = appDao.getCartItems()
    val boughtCourses: Flow<List<BoughtCourse>> = appDao.getBoughtCourses()
    val contactMessages: Flow<List<ContactMessage>> = appDao.getContactMessages()
    val quoteRequests: Flow<List<QuoteRequest>> = appDao.getQuoteRequests()

    suspend fun insertCartItem(cartItem: CartItem) = appDao.insertCartItem(cartItem)
    suspend fun deleteCartItem(cartItem: CartItem) = appDao.deleteCartItem(cartItem)
    suspend fun deleteCartItemByCourseId(courseId: Int) = appDao.deleteCartItemByCourseId(courseId)
    suspend fun clearCart() = appDao.clearCart()

    suspend fun insertBoughtCourse(boughtCourse: BoughtCourse) = appDao.insertBoughtCourse(boughtCourse)
    suspend fun isCourseBought(courseId: Int): Boolean = appDao.isCourseBought(courseId)

    suspend fun insertContactMessage(message: ContactMessage) = appDao.insertContactMessage(message)
    suspend fun insertQuoteRequest(quoteRequest: QuoteRequest) = appDao.insertQuoteRequest(quoteRequest)
}
