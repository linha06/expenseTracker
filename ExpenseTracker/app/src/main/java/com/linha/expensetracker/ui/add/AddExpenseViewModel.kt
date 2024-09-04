package com.linha.expensetracker.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val expenseRepository: ExpenseRepository): ViewModel() {
    fun addExpense(title: String, expense: Int, date : Long, category : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newExpense = Expense(title = title, expense = expense, dueDateMillis = date, category = category)
            expenseRepository.insertExpense(newExpense)
        }
    }
}