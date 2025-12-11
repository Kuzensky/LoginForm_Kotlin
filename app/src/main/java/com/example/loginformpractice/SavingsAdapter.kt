package com.example.loginformpractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SavingsAdapter(
    private val savings: MutableList<SavingsEntry>,
    private val onDeleteClick: (SavingsEntry) -> Unit
) : RecyclerView.Adapter<SavingsAdapter.SavingsViewHolder>() {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    class SavingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_savings_entry, parent, false)
        return SavingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavingsViewHolder, position: Int) {
        val entry = savings[position]

        holder.txtAmount.text = currencyFormat.format(entry.amount)
        holder.txtDescription.text = if (entry.description.isNotEmpty()) {
            entry.description
        } else {
            "Savings entry"
        }
        holder.txtDate.text = dateFormat.format(Date(entry.timestamp))

        holder.btnDelete.setOnClickListener {
            onDeleteClick(entry)
        }
    }

    override fun getItemCount(): Int = savings.size

    fun updateData(newSavings: List<SavingsEntry>) {
        savings.clear()
        savings.addAll(newSavings)
        notifyDataSetChanged()
    }

    fun removeItem(entry: SavingsEntry) {
        val position = savings.indexOf(entry)
        if (position != -1) {
            savings.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
