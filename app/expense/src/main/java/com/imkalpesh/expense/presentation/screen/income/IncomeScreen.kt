@file:OptIn(ExperimentalMaterial3Api::class)

package com.imkalpesh.expense.presentation.screen.income

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.imkalpesh.expense.R
import com.imkalpesh.expense.base.AddExpenseNavigationEvent
import com.imkalpesh.expense.base.NavigationEvent
import com.imkalpesh.expense.presentation.component.ExpenseTextView
import com.imkalpesh.expense.room.model.ExpenseEntity
import com.imkalpesh.expense.ui.theme.BluePrimary
import com.imkalpesh.expense.ui.theme.BluePrimaryLight
import com.imkalpesh.expense.ui.theme.InterFontFamily
import com.imkalpesh.expense.ui.theme.Typography
import com.imkalpesh.expense.utils.Utils

@Composable
fun IncomeScreen(
    navController: NavController,
    isIncome: Boolean,
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val menuExpanded = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateBack -> navController.popBackStack()
                AddExpenseNavigationEvent.MenuOpenedClicked -> {
                    menuExpanded.value = true
                }

                else -> {}
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, card, topBar) = createRefs()
            Row(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(BluePrimary)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            viewModel.onEvent(AddExpenseUiEvent.OnBackPressed)
                        })
                ExpenseTextView(
                    text = "Add ${if (isIncome) "Income" else "Expense"}",
                    style = Typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { ExpenseTextView(text = "Profile") },
                            onClick = {
                                menuExpanded.value = false
                                // Navigate to profile screen
                                // navController.navigate("profile_route")
                            }
                        )
                        DropdownMenuItem(
                            text = { ExpenseTextView(text = "Settings") },
                            onClick = {
                                menuExpanded.value = false
                                // Navigate to settings screen
                                // navController.navigate("settings_route")
                            }
                        )
                    }
                }

            }
            DataForm(modifier = Modifier.constrainAs(card) {
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onAddExpenseClick = {
                viewModel.onEvent(AddExpenseUiEvent.OnAddExpenseClicked(it))
            }, isIncome)
        }
    }
}

@Composable
fun DataForm(
    modifier: Modifier,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    isIncome: Boolean
) {

    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableLongStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val type = remember {
        mutableStateOf(if (isIncome) "Income" else "Expense")
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TitleComponent(title = "name")
        ExpenseDropDown(
            if (isIncome) listOf(
                "Salary",
                "Freelance",
                "Investments",
                "Rental Income",
                "Other Income"
            ) else listOf(
                "Grocery",
                "Rent",
                "Shopping",
                "Dining Out",
                "Subscriptions",
                "Travel",
                "Other Expenses"
            ),
            onItemSelected = {
                name.value = it
            })
        Spacer(modifier = Modifier.size(24.dp))
        TitleComponent("amount")
        OutlinedTextField(
            value = amount.value,
            onValueChange = { newValue ->
                amount.value = newValue.filter { it.isDigit() || it == '.' }
            }, textStyle = TextStyle(color = Color.Black),
            visualTransformation = { text ->
                val out = "₹" + text.text
                val currencyOffsetTranslator = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        return offset + 1
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        return if (offset > 0) offset - 1 else 0
                    }
                }

                TransformedText(AnnotatedString(out), currencyOffsetTranslator)
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { ExpenseTextView(text = "Enter amount") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
                focusedTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.size(24.dp))
        TitleComponent("date")
        OutlinedTextField(value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(
            date.longValue
        ),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
            ),
            placeholder = { ExpenseTextView(text = "Select date") })
        Spacer(modifier = Modifier.size(24.dp))
        val context = LocalContext.current

        Button(
            colors = ButtonColors(
                BluePrimary,
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.background
            ),
            onClick = {
                when {
                    name.value.isBlank() -> {
                        Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                    }

                    amount.value.isBlank() -> {
                        Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show()
                    }

                    amount.value.toDoubleOrNull() == null || amount.value.toDouble() <= 0 -> {
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT)
                            .show()
                    }

                    type.value.isBlank() -> {
                        Toast.makeText(context, "Please select a type", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        val model = ExpenseEntity(
                            id = null,
                            title = name.value.trim(),
                            amount = amount.value.toDouble(),
                            date = Utils.formatDateToHumanReadableForm(date.longValue),
                            type = type.value
                        )
                        onAddExpenseClick(model)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            ExpenseTextView(
                text = "Add ${if (isIncome) "Income" else "Expense"}",
                fontSize = 14.sp,
                color = Color.White
            )
        }

    }
    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(onDateSelected = {
            date.longValue = it
            dateDialogVisibility.value = false
        }, onDismiss = {
            dateDialogVisibility.value = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TitleComponent(title: String) {
    ExpenseTextView(
        text = title.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = BluePrimaryLight
    )
    Spacer(modifier = Modifier.size(10.dp))
}

@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        OutlinedTextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            textStyle = TextStyle(fontFamily = InterFontFamily),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            shape = RoundedCornerShape(8.dp),
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { }) {
            listOfItems.forEach {
                DropdownMenuItem(text = { ExpenseTextView(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpense() {
    IncomeScreen(rememberNavController(), true)
}

