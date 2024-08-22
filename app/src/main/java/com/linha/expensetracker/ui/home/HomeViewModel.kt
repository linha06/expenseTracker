package com.linha.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.data.ExpenseRepository
import com.linha.expensetracker.utils.Event
import com.linha.expensetracker.utils.ExpenseFilterType
import kotlinx.coroutines.launch

class HomeViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    private val _filter = MutableLiveData<ExpenseFilterType>()

    val expense: LiveData<PagedList<Expense>> = _filter.switchMap {
        expenseRepository.getExpenses(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    init {
        _filter.value = ExpenseFilterType.ALL_CATEGORY
    }

    fun filter(filterType: ExpenseFilterType) {
        _filter.value = filterType
    }

//    fun completeExpense(expense: Expense, completed: Boolean) = viewModelScope.launch {
//        expenseRepository.completeExpense(expense, completed)
//        if (completed) {
//            _snackbarText.value = Event(R.string.task_marked_complete)
//        } else {
//            _snackbarText.value = Event(R.string.task_marked_active)
//        }
//    }

    fun deleteTask(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expense)
        }
    }
}