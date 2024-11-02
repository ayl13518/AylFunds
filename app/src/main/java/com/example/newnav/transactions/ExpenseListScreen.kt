package com.example.newnav.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.ScreenTran
import com.example.newnav.navigation.NavigationBottomBar
import com.example.newnav.ui.theme.expenseColor
import com.example.newnav.ui.theme.incomeColor
import com.example.newnav.viewmodels.ExpTransViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpTransViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    val transbyDate = state.expTrans.groupBy { it.dateTrans  }
    val sumByDate = state.expTrans
        .groupingBy { it.dateTrans  }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 1
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenTran)
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            transbyDate.forEach { (initialDate, expsByDate) ->
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        , horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = initialDate,
                            style = typography.titleLarge,
                        )
                        Text(text = sumByDate[initialDate].toString())
                    }
                }
                items(expsByDate) { exp ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        , horizontalArrangement = Arrangement.SpaceBetween)
                    {
                        Text(text = exp.note)
                        Text(text = exp.tranType)
                        if (exp.tranType == "Income")
                            Text(text = exp.amount.toString(),
                                color = incomeColor,
                            )
                        else
                            Text(text = exp.amount.toString(),
                                color = expenseColor,
                            )

                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ExpListScreenPreview(){
    ExpListScreen( )
}