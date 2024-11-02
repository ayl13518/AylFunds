package com.example.newnav.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.ScreenTran
import com.example.newnav.navigation.NavigationBottomBar
import com.example.newnav.ui.theme.expenseColor
import com.example.newnav.ui.theme.incomeColor
import com.example.newnav.ui.theme.transferColor
import com.example.newnav.utils.DecimalFormatter
import com.example.newnav.viewmodels.ExpTransViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpTransViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    val transbyDate = state.expTrans
        .groupBy { it.dateTrans }
        .mapValues { (_, value) -> value.sortedBy { it.dateTrans } }.toSortedMap()

    val sumByDate = state.expTrans
        .groupingBy { it.dateTrans }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount }

    val decimalFormatter = DecimalFormatter()


    Scaffold(
        modifier = Modifier
            .padding(top=50.dp)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
                Box(modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd) {
                    Text(text = state.typeList[state.selectedType].toString())
                }
        },
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
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount > -200) {
                            change.consume()
                            viewModel.onTranTypeUpdate("Transfer")
                        }
                        else if (dragAmount < 200) {
                            change.consume()
                            viewModel.onTranTypeUpdate("Expense")
                        }
                    }
                }
                .fillMaxWidth(),
            contentPadding = paddingValues,

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
                        Text(text = decimalFormatter.formatForVisual( sumByDate[initialDate].toString()))
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
                            Text(text = decimalFormatter.formatForVisual( exp.amount.toString()),
                                color = incomeColor,
                                )
                        else if (exp.tranType == "Transfer")
                            Text(text = decimalFormatter.formatForVisual( exp.amount.toString()),
                                color = transferColor,
                            )
                        else
                            Text(text = decimalFormatter.formatForVisual( exp.amount.toString()),
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