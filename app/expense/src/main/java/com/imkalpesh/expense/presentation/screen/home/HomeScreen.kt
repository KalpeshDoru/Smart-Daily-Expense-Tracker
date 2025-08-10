package com.imkalpesh.expense.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.imkalpesh.expense.R
import com.imkalpesh.expense.base.HomeNavigationEvent
import com.imkalpesh.expense.base.NavigationEvent
import com.imkalpesh.expense.presentation.component.ExpenseTextView
import com.imkalpesh.expense.room.model.ExpenseEntity
import com.imkalpesh.expense.ui.theme.BluePrimary
import com.imkalpesh.expense.ui.theme.BluePrimaryLight
import com.imkalpesh.expense.ui.theme.GreenLight
import com.imkalpesh.expense.ui.theme.RedLight
import com.imkalpesh.expense.ui.theme.Typography
import com.imkalpesh.expense.utils.Utils


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateBack -> navController.popBackStack()
                HomeNavigationEvent.NavigateToSeeAll -> {
                    navController.navigate("/all_transactions")
                }

                HomeNavigationEvent.NavigateToAddIncome -> {
                    navController.navigate("/add_income")
                }

                HomeNavigationEvent.NavigateToAddExpense -> {
                    navController.navigate("/add_exp")
                }

                else -> {}
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        val state = viewModel.expenses.collectAsState(initial = emptyList())
        val expense = viewModel.getTotalExpense(state.value)
        val income = viewModel.getTotalIncome(state.value)
        val balance = viewModel.getBalance(state.value)

        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .background(BluePrimary)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ExpenseTextView(
                modifier = Modifier.padding(16.dp),
                text = "Hi, Greetings",
                style = Typography.headlineSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            CardItem(
                modifier = Modifier.fillMaxWidth(),
                balance = balance,
                income = income,
                expense = expense
            )
        }
        Column(
            modifier = Modifier
                .weight(8f)
                .fillMaxWidth()
        ) {
            TransactionList(
                modifier = Modifier.fillMaxWidth(),
                list = state.value,
                onSeeAllClicked = {
                    viewModel.onEvent(HomeUiEvent.OnSeeAllClicked)
                }
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        MultiFloatingActionButton(modifier = Modifier, {
            viewModel.onEvent(HomeUiEvent.OnAddExpenseClicked)
        }, {
            viewModel.onEvent(HomeUiEvent.OnAddIncomeClicked)
        })
    }
}

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier, onAddExpenseClicked: () -> Unit, onAddIncomeClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = expanded) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = BluePrimary, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddIncomeClicked.invoke()
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_income),
                            contentDescription = "Add Income",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = BluePrimary, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddExpenseClicked.invoke()
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_expense),
                            contentDescription = "Add Expense",
                            tint = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = BluePrimary)
                    .clickable {
                        expanded = !expanded
                    }, contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_addbutton),
                    contentDescription = "small floating action button",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier, balance: String, income: String, expense: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
        ) {
            Column {
                ExpenseTextView(
                    text = "Total Balance", style = Typography.titleLarge, color = Color.White
                )
                ExpenseTextView(
                    text = balance, style = Typography.headlineLarge, color = Color.White,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CardRowItem(
                        modifier = Modifier.align(Alignment.CenterStart),
                        title = "Income",
                        amount = income,
                        imaget = R.drawable.ic_income
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    CardRowItem(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        title = "Expense",
                        amount = expense,
                        imaget = R.drawable.ic_expense
                    )
                }
            }
        }
    }
}


@Composable
fun TransactionList(
    modifier: Modifier,
    list: List<ExpenseEntity>,
    title: String = "Recent Transactions",
    onSeeAllClicked: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Column {
                Box(modifier = modifier.fillMaxWidth()) {
                    ExpenseTextView(
                        text = title,
                        style = Typography.titleLarge,
                    )
                    if (title == "Recent Transactions") {
                        ExpenseTextView(text = "See all",
                            style = Typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    onSeeAllClicked.invoke()
                                })
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
        items(items = list, key = { item -> item.id ?: 0 }) { item ->
            val icon = Utils.getItemIcon(item)
            val amount = if (item.type == "Income") item.amount else item.amount * -1

            TransactionItem(
                title = item.title,
                amount = Utils.formatCurrency(amount),
                icon = icon,
                date = Utils.formatStringDateToMonthDayYear(item.date),
                color = if (item.type == "Income") GreenLight else RedLight,
                Modifier
            )
        }
    }
}

@Composable
fun TransactionItem(
    title: String, amount: String, icon: Int, date: String, color: Color, modifier: Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(
                    text = title,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(4.dp))
                ExpenseTextView(
                    text = date,
                    fontWeight = FontWeight.Normal,
                    style = Typography.titleSmall,
                    color = BluePrimaryLight
                )
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, imaget: Int) {
    Column(modifier = modifier) {
        Row {

            Image(
                painter = painterResource(id = imaget),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, style = Typography.bodyLarge, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = amount, style = Typography.titleLarge, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}