package com.example.newnav.budgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.ScreenAccount
import com.example.newnav.ScreenBudget
import com.example.newnav.navigation.NavigationBottomBar
import com.example.newnav.viewmodels.AccountViewModel
import com.example.newnav.viewmodels.BudgetViewModel
import kotlin.collections.component1
import kotlin.collections.component2


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BudgetListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: BudgetViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    val accbyType = state.budgets.groupBy { it.type }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 3
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenBudget)
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            accbyType.forEach { (initialDate, expsByDate) ->
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Text(text = initialDate,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                }
                items(expsByDate) { item ->
                    Row(modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = item.name,
                            modifier = Modifier.padding(start =16.dp))
                        Text(text = item.balance.toString(),
                            modifier = Modifier.padding(end =16.dp))
                    }
                }
            }
        }
    }

}