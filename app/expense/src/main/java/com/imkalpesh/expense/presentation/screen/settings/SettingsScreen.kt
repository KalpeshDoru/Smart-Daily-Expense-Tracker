package com.imkalpesh.expense.presentation.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.imkalpesh.expense.R
import com.imkalpesh.expense.presentation.component.ExpenseTextView

@Composable
fun SettingsScreen(
    navController: NavController, viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.navigateUp() },
            )

            ExpenseTextView(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }) {
        val isDarkMode by viewModel.isDarkMode.collectAsState()
        Column(modifier = Modifier.padding(it)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Dark Mode",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(checked = isDarkMode,
                    onCheckedChange = { viewModel.onEvent(SettingsUiEvent.OnDarkModeToggled(it)) })
            }
        }
    }
}

