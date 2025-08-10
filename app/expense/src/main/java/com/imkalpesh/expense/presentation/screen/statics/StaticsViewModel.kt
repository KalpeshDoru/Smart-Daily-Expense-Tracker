package com.imkalpesh.expense.presentation.screen.statics

import com.github.mikephil.charting.data.Entry
import com.imkalpesh.expense.base.BaseViewModel
import com.imkalpesh.expense.base.UiEvent
import com.imkalpesh.expense.room.dao.ExpenseDao
import com.imkalpesh.expense.room.model.ExpenseSummary
import com.imkalpesh.expense.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(val dao: ExpenseDao) : BaseViewModel() {
    val entries = dao.getAllExpenseByDate()
    val topEntries = dao.getTopExpenses()
    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in entries) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }

    override fun onEvent(event: UiEvent) {
    }
}

