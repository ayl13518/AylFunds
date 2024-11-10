package com.aylmer.aylfunds.accounts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.aylmer.aylfunds.ScreenAccount
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.utils.DecimalFormatter
import kotlin.collections.component1
import kotlin.collections.component2


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
    onClickList: (accountId: Long) -> Unit
){
    val state by viewModel.state.collectAsState()

    val accbyType = state.accounts.groupBy { it.accType }

    val decimalFormatter = DecimalFormatter()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 2
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenAccount(0))
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
                items(expsByDate) { exp ->
                    Row(modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { onClickList(exp.id) }
                        .fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = exp.name,
                            modifier = Modifier.padding(start =16.dp))
                        Text(text = decimalFormatter.formatForVisual(
                            exp.balance.toString()
                        ),
                            modifier = Modifier.padding(end =16.dp))
                    }
                }
            }
        }
    }

}