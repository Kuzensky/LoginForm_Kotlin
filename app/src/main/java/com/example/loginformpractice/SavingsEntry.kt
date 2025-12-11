package com.example.loginformpractice

data class SavingsEntry(
    val id: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
