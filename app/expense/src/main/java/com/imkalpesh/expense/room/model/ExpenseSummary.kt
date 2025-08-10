package com.imkalpesh.expense.room.model

data class ExpenseSummary(
    val type: String,
    val date: String,
    val total_amount: Double
)