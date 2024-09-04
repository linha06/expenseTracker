package com.linha.expensetracker.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface ExpenseDao {

    @RawQuery(observedEntities = [Expense::class])
    fun getExpense(query: SupportSQLiteQuery): DataSource.Factory<Int, Expense>

    @Query("SELECT * FROM moneys WHERE id = :expenseId")
    fun getExpenseById(expenseId: Int): LiveData<Expense>

    @Query("SELECT * FROM moneys ORDER BY dueDate ASC LIMIT 1")
    fun getNearestExpense(): Expense

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expenses: Expense)

    @Query("DELETE FROM moneys WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: Int)

    @Query("UPDATE moneys SET category = :kategori, title = :title, expense = :expense, dueDate = :dueDate  WHERE id = :expenseId")
    suspend fun updateExpense(expenseId: Int, title: String, expense: Int, dueDate: Long, kategori: Int)

}