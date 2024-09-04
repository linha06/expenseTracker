package com.linha.expensetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moneys")
data class Expense (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "expense")
    val expense: Int,

    @ColumnInfo(name = "dueDate")
    val dueDateMillis: Long,

    @ColumnInfo(name = "category")
    val category: Int
)

// Transportasi = 0
// Makanan = 1
// Minuman = 2