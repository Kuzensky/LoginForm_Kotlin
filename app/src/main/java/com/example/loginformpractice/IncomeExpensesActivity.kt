package com.example.loginformpractice

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*

class IncomeExpensesActivity : AppCompatActivity() {

    private lateinit var btnBack: LinearLayout
    private lateinit var editMonthlyIncome: EditText
    private lateinit var editMonthlyExpenses: EditText
    private lateinit var btnUpdateFinances: Button
    private lateinit var txtIncomeValue: TextView
    private lateinit var txtExpensesValue: TextView
    private lateinit var txtRemainingBalance: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_income_expenses)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initializeViews()
        setupClickListeners()
        loadFinanceData()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        editMonthlyIncome = findViewById(R.id.editMonthlyIncome)
        editMonthlyExpenses = findViewById(R.id.editMonthlyExpenses)
        btnUpdateFinances = findViewById(R.id.btnUpdateFinances)
        txtIncomeValue = findViewById(R.id.txtIncomeValue)
        txtExpensesValue = findViewById(R.id.txtExpensesValue)
        txtRemainingBalance = findViewById(R.id.txtRemainingBalance)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnUpdateFinances.setOnClickListener {
            updateFinanceData()
        }
    }

    private fun updateFinanceData() {
        val incomeStr = editMonthlyIncome.text.toString().trim()
        val expensesStr = editMonthlyExpenses.text.toString().trim()

        if (incomeStr.isEmpty()) {
            editMonthlyIncome.error = "Please enter monthly income"
            editMonthlyIncome.requestFocus()
            return
        }

        if (expensesStr.isEmpty()) {
            editMonthlyExpenses.error = "Please enter monthly expenses"
            editMonthlyExpenses.requestFocus()
            return
        }

        val income = incomeStr.toDoubleOrNull()
        val expenses = expensesStr.toDoubleOrNull()

        if (income == null || income < 0) {
            editMonthlyIncome.error = "Please enter a valid amount"
            editMonthlyIncome.requestFocus()
            return
        }

        if (expenses == null || expenses < 0) {
            editMonthlyExpenses.error = "Please enter a valid amount"
            editMonthlyExpenses.requestFocus()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val financeData = FinanceData(
            monthlyIncome = income,
            monthlyExpenses = expenses,
            lastUpdated = System.currentTimeMillis()
        )

        firestore.collection("finance")
            .document(currentUser.uid)
            .collection("data")
            .document("current")
            .set(financeData)
            .addOnSuccessListener {
                Toast.makeText(this, "Finances updated successfully", Toast.LENGTH_SHORT).show()
                displayFinanceData(financeData)
                editMonthlyIncome.text.clear()
                editMonthlyExpenses.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadFinanceData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("finance")
            .document(currentUser.uid)
            .collection("data")
            .document("current")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val financeData = document.toObject(FinanceData::class.java)
                    financeData?.let {
                        displayFinanceData(it)
                    }
                } else {
                    displayFinanceData(FinanceData())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load data: ${e.message}", Toast.LENGTH_SHORT).show()
                displayFinanceData(FinanceData())
            }
    }

    private fun displayFinanceData(financeData: FinanceData) {
        txtIncomeValue.text = currencyFormat.format(financeData.monthlyIncome)
        txtExpensesValue.text = currencyFormat.format(financeData.monthlyExpenses)

        val balance = financeData.getRemainingBalance()
        txtRemainingBalance.text = currencyFormat.format(balance)

        if (balance >= 0) {
            txtRemainingBalance.setTextColor(Color.parseColor("#26A69A"))
        } else {
            txtRemainingBalance.setTextColor(Color.parseColor("#F44336"))
        }
    }
}
