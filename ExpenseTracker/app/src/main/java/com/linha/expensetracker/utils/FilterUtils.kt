package com.linha.expensetracker.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {

    fun getFilteredQuery(filter: ExpenseFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM moneys ")
        when (filter) {
            ExpenseFilterType.TRANSPORTASI -> {
                simpleQuery.append("WHERE category = 0")
            }
            ExpenseFilterType.MAKANAN -> {
                simpleQuery.append("WHERE category = 1")
            }
            ExpenseFilterType.MINUMAN -> {
                simpleQuery.append("WHERE category = 2")
            }
            else -> {
                ExpenseFilterType.ALL_CATEGORY
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}