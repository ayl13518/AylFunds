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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.ScreenAccount
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import com.aylmer.aylfunds.utils.DecimalFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
    onClickList: (accountId: Long) -> Unit
) {
    val state by viewModel.state.collectAsState()

    state.accounts.groupBy { it.accType }

    val decimalFormatter = DecimalFormatter()

    val mainList = state.accounts

    val groupTotal = mainList.fold(0.0) { total, acc ->
        total + acc.balance
    }

    val groupType = mainList.groupBy { acc -> acc.accType }
    val sumType = mainList
        .groupingBy { it.accType }
        .fold(0.0) { total, expTrans -> total + expTrans.balance }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        bottomBar = {
            NavigationBottomBar(
                navController = navController, selected = 2
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
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            //RoundedCornerShape(16.dp)
                        )
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "Total Assets",
                        modifier = Modifier.padding(start = 10.dp),
                        style = typography.titleLarge,
                    )
                    Text(
                        text = decimalFormatter.formatForVisual(
                            groupTotal.toString()
                        ),
                        modifier = Modifier.padding(end = 10.dp),
                        style = typography.titleLarge,
                    )
                }
            }
            groupType.forEach { type, list ->
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                //RoundedCornerShape(16.dp)
                            )
                            .height(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        Text(
                            text = type,
                            modifier = Modifier.padding(start = 10.dp),
                            style = typography.titleLarge,
                        )
                        Text(
                            text = decimalFormatter.formatForVisual(
                                sumType[type].toString()
                            ),
                            modifier = Modifier.padding(end = 10.dp),
                            style = typography.titleLarge,
                        )
                    }
                }
                items(list) { exp ->
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { onClickList(exp.id) }
                            .fillMaxWidth()
                            .height(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = exp.name,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Text(
                            text = decimalFormatter.formatForVisual(
                                exp.balance.toString()
                            ),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
fun AccListScreenPreview() {

    val decimalFormatter = DecimalFormatter()

    val mainList = listOf<accounts>(
        accounts(id = 1, name = "maya1", accType = "Bank", balance = 1000.0),
        accounts(id = 1, name = "maya2", accType = "Bank", balance = 1000.0),
        accounts(id = 1, name = "maya3", accType = "Bank", balance = 1000.0),
        accounts(id = 1, name = "maya4", accType = "Cash", balance = 1000.0),
        accounts(id = 1, name = "maya4", accType = "Investment", balance = 1000.0),
    )

    val groupTotal = mainList.fold(0.0) { total, acc ->
        total + acc.balance
    }

    val groupType = mainList.groupBy { acc -> acc.accType }
    val sumType = mainList
        .groupingBy { it.accType }
        .fold(0.0) { total, expTrans -> total + expTrans.balance }

    NewNavTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
//            bottomBar = {
//                NavigationBottomBar(
//                    //navController= navController
//                    //, selected = 2
//                )
//            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        //navController.navigate(ScreenAccount(0))
                    },
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = paddingValues,
            ) {
                item {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                //RoundedCornerShape(16.dp)
                            )
                            .height(35.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "Total Assets",
                            modifier = Modifier.padding(start = 10.dp),
                            style = typography.titleLarge,
                        )
                        Text(
                            text = decimalFormatter.formatForVisual(
                                groupTotal.toString()
                            ),
                            modifier = Modifier.padding(end = 10.dp),
                            style = typography.titleLarge,
                        )
                    }
                }
                groupType.forEach { type, list ->
                    stickyHeader {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    //RoundedCornerShape(16.dp)
                                )
                                .height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        )
                        {
                            Text(
                                text = type,
                                modifier = Modifier.padding(start = 10.dp),
                                style = typography.titleLarge,
                            )
                            Text(
                                text = decimalFormatter.formatForVisual(
                                    sumType[type].toString()
                                ),
                                modifier = Modifier.padding(end = 10.dp),
                                style = typography.titleLarge,
                            )
                        }
                    }
                    items(list) { exp ->
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                                .height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,

                            ) {
                            Text(
                                text = exp.name,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            Text(
                                text = decimalFormatter.formatForVisual(
                                    exp.balance.toString()
                                ),
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}