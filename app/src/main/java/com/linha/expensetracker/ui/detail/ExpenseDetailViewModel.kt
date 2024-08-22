package com.linha.expensetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(private val expenseRepository: ExpenseRepository): ViewModel() {

    private val _expenseId = MutableLiveData<Int>()

    private val _expense = _expenseId.switchMap { id ->
        expenseRepository.getExpenseById(id)
    }
    val expense: LiveData<Expense> = _expense

    fun updateExpense(id: Int, title: String, expense: Int, date : Long, category : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newExpense = Expense(id = id,title = title, expense = expense, dueDateMillis = date, category = category)
            expenseRepository.updateExpense(newExpense)
        }
    }

    fun setTaskId(taskId: Int) {
        if (taskId == _expenseId.value) {
            return
        }
        _expenseId.value = taskId
    }

    fun deleteTask() {
        viewModelScope.launch {
            _expense.value?.let { expenseRepository.deleteExpense(it) }
        }
    }
}