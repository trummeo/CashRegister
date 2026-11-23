package com.example.cashregister.DataLayer

import java.util.Date

data class Product (
    val id: Int,
    val name: String,
    val quantity: Int,
    val price: Double
)

data class Purchase (
    val id: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val total: Double,
    val date: Date = Date ()
)