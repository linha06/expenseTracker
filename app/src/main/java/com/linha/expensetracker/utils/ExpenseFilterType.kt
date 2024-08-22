package com.linha.expensetracker.utils

/**
 * Used with the filter spinner in the tasks list.
 */
enum class ExpenseFilterType {
    /**
     * TRANSPORTASI = 0
     * MAKANAN = 1
     * MINUMAN = 2
     */
    TRANSPORTASI,
    MINUMAN,
    MAKANAN,

    /**
     * Filters only the completed tasks.
     */
    ALL_CATEGORY
}
