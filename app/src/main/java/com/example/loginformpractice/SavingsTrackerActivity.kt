package com.example.loginformpractice

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.NumberFormat
import java.util.*

class SavingsTrackerActivity : AppCompatActivity() {

    private lateinit var btnBack: LinearLayout
    private lateinit var editSavingsAmount: EditText
    private lateinit var editSavingsDescription: EditText
    private lateinit var btnSave: LinearLayout
    private lateinit var txtTotalSavings: TextView
    private lateinit var recyclerViewSavings: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var savingsAdapter: SavingsAdapter

    private val savingsList = mutableListOf<SavingsEntry>()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_savings_tracker)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadSavingsData()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        editSavingsAmount = findViewById(R.id.editSavingsAmount)
        editSavingsDescription = findViewById(R.id.editSavingsDescription)
        btnSave = findViewById(R.id.btnSave)
        txtTotalSavings = findViewById(R.id.txtTotalSavings)
        recyclerViewSavings = findViewById(R.id.recyclerViewSavings)
    }

    private fun setupRecyclerView() {
        savingsAdapter = SavingsAdapter(savingsList) { entry ->
            showDeleteConfirmation(entry)
        }
        recyclerViewSavings.layoutManager = LinearLayoutManager(this)
        recyclerViewSavings.adapter = savingsAdapter
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            saveSavingsEntry()
        }
    }

    private fun saveSavingsEntry() {
        val amountStr = editSavingsAmount.text.toString().trim()
        val description = editSavingsDescription.text.toString().trim()

        if (amountStr.isEmpty()) {
            editSavingsAmount.error = "Please enter an amount"
            editSavingsAmount.requestFocus()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            editSavingsAmount.error = "Please enter a valid amount"
            editSavingsAmount.requestFocus()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new document reference to get the ID
        val docRef = firestore.collection("savings")
            .document(currentUser.uid)
            .collection("entries")
            .document()

        val savingsEntry = SavingsEntry(
            id = docRef.id,
            amount = amount,
            description = description,
            timestamp = System.currentTimeMillis()
        )

        // Save to Firestore
        docRef.set(savingsEntry)
            .addOnSuccessListener {
                Toast.makeText(this, "Savings added successfully", Toast.LENGTH_SHORT).show()
                editSavingsAmount.text.clear()
                editSavingsDescription.text.clear()
                loadSavingsData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add savings: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadSavingsData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        // Load savings entries from Firestore
        firestore.collection("savings")
            .document(currentUser.uid)
            .collection("entries")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                savingsList.clear()
                for (document in documents) {
                    val entry = document.toObject(SavingsEntry::class.java)
                    savingsList.add(entry)
                }
                savingsAdapter.updateData(savingsList)
                updateTotalSavings()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load savings: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTotalSavings() {
        val total = savingsList.sumOf { it.amount }
        txtTotalSavings.text = currencyFormat.format(total)
    }

    private fun showDeleteConfirmation(entry: SavingsEntry) {
        AlertDialog.Builder(this)
            .setTitle("Delete Savings Entry")
            .setMessage("Are you sure you want to delete this savings entry of ${currencyFormat.format(entry.amount)}?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteSavingsEntry(entry)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteSavingsEntry(entry: SavingsEntry) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        // Delete from Firestore
        firestore.collection("savings")
            .document(currentUser.uid)
            .collection("entries")
            .document(entry.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Savings entry deleted", Toast.LENGTH_SHORT).show()
                savingsAdapter.removeItem(entry)
                updateTotalSavings()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
