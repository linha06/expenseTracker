package com.linha.expensetracker.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linha.expensetracker.data.ExpenseRepository
import com.linha.expensetracker.ui.add.AddExpenseViewModel
import com.linha.expensetracker.ui.detail.ExpenseDetailViewModel
import com.linha.expensetracker.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val taskRepository: ExpenseRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    ExpenseRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(ExpenseDetailViewModel::class.java) -> {
                ExpenseDetailViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(AddExpenseViewModel::class.java) -> {
                AddExpenseViewModel(taskRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}