package com.linha.expensetracker.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.linha.expensetracker.utils.ExpenseFilterType
import com.linha.expensetracker.utils.FilterUtils

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: ExpenseRepository? = null

        fun getInstance(context: Context): ExpenseRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = ExpenseDatabase.getInstance(context)
                    instance = ExpenseRepository(database.expenseDao())
                }
                return instance as ExpenseRepository
            }

        }
    }

    fun getExpenses(filter: ExpenseFilterType): LiveData<PagedList<Expense>> {
        val query = FilterUtils.getFilteredQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(expenseDao.getExpense(query), config).build()
    }

    fun getExpenseById(expenseId: Int): LiveData<Expense> {
        return expenseDao.getExpenseById(expenseId)
    }

    fun getNearestActiveExpense(): Expense {
        return expenseDao.getNearestExpense()
    }

    suspend fun insertExpense(newExpense: Expense): Long{
        return expenseDao.insertExpense(newExpense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.id)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.id, expense.title, expense.expense, expense.dueDateMillis, expense.category)
    }
}