package com.example.loginformpractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CareerTipsAdapter(
    private val tips: List<CareerTip>
) : RecyclerView.Adapter<CareerTipsAdapter.CareerTipViewHolder>() {

    private val tipIcons = listOf(
        "🎯", "📚", "🤝", "💬", "⏰",
        "🎓", "💼", "🚀", "🧠", "📊",
        "💡", "🔧", "⚖️", "👥", "🔄",
        "💰", "🧩", "🤲", "📝", "🎉"
    )

    class CareerTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTipNumber: TextView = itemView.findViewById(R.id.txtTipNumber)
        val txtTipIcon: TextView = itemView.findViewById(R.id.txtTipIcon)
        val txtTipTitle: TextView = itemView.findViewById(R.id.txtTipTitle)
        val txtTipDescription: TextView = itemView.findViewById(R.id.txtTipDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareerTipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_career_tip, parent, false)
        return CareerTipViewHolder(view)
    }

    override fun onBindViewHolder(holder: CareerTipViewHolder, position: Int) {
        val tip = tips[position]

        holder.txtTipNumber.text = tip.id.toString()
        holder.txtTipIcon.text = tipIcons.getOrElse(position) { "💡" }
        holder.txtTipTitle.text = tip.title
        holder.txtTipDescription.text = tip.description
    }

    override fun getItemCount(): Int = tips.size
}
