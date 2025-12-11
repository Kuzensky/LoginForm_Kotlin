package com.example.loginformpractice

data class FinanceData(
    val monthlyIncome: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun getRemainingBalance(): Double {
        return monthlyIncome - monthlyExpenses
    }
}
