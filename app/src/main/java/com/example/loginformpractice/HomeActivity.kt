package com.example.loginformpractice

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var txtWelcomeUser: TextView
    private lateinit var btnSavingsTracker: LinearLayout
    private lateinit var btnIncomeExpenses: LinearLayout
    private lateinit var btnCareerTips: LinearLayout
    private lateinit var btnLogout: LinearLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initializeViews()
        setupClickListeners()
        loadUserData()
    }

    private fun initializeViews() {
        txtWelcomeUser = findViewById(R.id.txtWelcomeUser)
        btnSavingsTracker = findViewById(R.id.btnSavingsTracker)
        btnIncomeExpenses = findViewById(R.id.btnIncomeExpenses)
        btnCareerTips = findViewById(R.id.btnCareerTips)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupClickListeners() {
        btnSavingsTracker.setOnClickListener {
            navigateToSavingsTracker()
        }

        btnIncomeExpenses.setOnClickListener {
            navigateToIncomeExpenses()
        }

        btnCareerTips.setOnClickListener {
            navigateToCareerTips()
        }

        btnLogout.setOnClickListener {
            handleLogout()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            redirectToLogin()
            return
        }

        firestore.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        // Capitalize first letter of each word
                        val capitalizedName = it.fullName.split(" ")
                            .joinToString(" ") { word ->
                                word.lowercase().replaceFirstChar { char -> char.uppercase() }
                            }
                        txtWelcomeUser.text = capitalizedName
                    }
                } else {
                    txtWelcomeUser.text = "User"
                }
            }
            .addOnFailureListener {
                txtWelcomeUser.text = "User"
            }
    }

    private fun navigateToSavingsTracker() {
        val intent = Intent(this, SavingsTrackerActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToIncomeExpenses() {
        val intent = Intent(this, IncomeExpensesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCareerTips() {
        val intent = Intent(this, CareerTipsActivity::class.java)
        startActivity(intent)
    }

    private fun handleLogout() {
        auth.signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
