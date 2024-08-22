package com.linha.expensetracker.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.todoapp.utils.DateConverter
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.databinding.ExpenseItemBinding
import com.linha.expensetracker.ui.detail.ExpenseDetailActivity
import com.linha.expensetracker.utils.EXPENSE_ID

//class HomeAdapter(private val onCheckedChange: (Expense, Boolean) -> Unit) : PagedListAdapter<Expense, HomeAdapter.ExpenseViewHolder>(DIFF_CALLBACK) {
class HomeAdapter: PagedListAdapter<Expense, HomeAdapter.ExpenseViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
//        when {
//            expense.isCompleted -> {
//                //DONE
//                holder.cbComplete.isChecked = true
//                holder.tvTitle.state = ExpenseTitleView.DONE
//            }
//            expense.dueDateMillis < System.currentTimeMillis() -> {
//                //OVERDUE
//                holder.cbComplete.isChecked = false
//                holder.tvTitle.state = ExpenseTitleView.OVERDUE
//            }
//            else -> {
//                //NORMAL
//                holder.cbComplete.isChecked = false
//                holder.tvTitle.state = ExpenseTitleView.NORMAL
//            }
//        }
    }

    class ExpenseViewHolder(private val binding: ExpenseItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        private val expenseTitle: TextView = binding.itemTvTitle
        private val expenseTotal: TextView = binding.expenseTotal
//        val cbComplete: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val tvDueDate: TextView = binding.itemTvDate

        lateinit var getExpense: Expense

        fun bind(expense: Expense) {
            getExpense = expense
            expenseTitle.text = expense.title
            expenseTotal.text = expense.expense.toString()
            tvDueDate.text = DateConverter.convertMillisToString(expense.dueDateMillis)
            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, ExpenseDetailActivity::class.java)
                detailIntent.putExtra(EXPENSE_ID, expense.id)
                itemView.context.startActivity(detailIntent)
            }
//            cbComplete.setOnClickListener {
//                onCheckedChange(expense, !expense.isCompleted)
//            }
        }

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem == newItem
            }
        }

    }

}