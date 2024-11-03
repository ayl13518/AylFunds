package com.example.newnav.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.ScreenTran
import com.example.newnav.designsys.component.ThemePreviews
import com.example.newnav.navigation.NavigationBottomBar
import com.example.newnav.ui.theme.NewNavTheme
import com.example.newnav.ui.theme.expenseColor
import com.example.newnav.ui.theme.incomeColor
import com.example.newnav.ui.theme.transferColor
import com.example.newnav.utils.DecimalFormatter
import com.example.newnav.viewmodels.ExpTransViewModel
import java.time.Month


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpTransViewModel = hiltViewModel()
){

    val state by viewModel.state.collectAsState()
    val transMonthList by viewModel.transMonthList.collectAsState(emptyList())
    val currentMonth = state.selectedMonth

    val transbyDate = transMonthList
        .groupBy { it.dateTrans }
        .mapValues { (_, value) -> value.sortedBy { it.dateTrans } }.toSortedMap(reverseOrder())

    val sumByDate = transMonthList
        .groupingBy { it.dateTrans }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount }

    val decimalFormatter = DecimalFormatter()

    val monthName= Month.of(currentMonth+1).toString().take(3)


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
                Box(modifier = Modifier
                        .padding(top=50.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        , horizontalArrangement = Arrangement.Absolute.Right)
                    {
                        Box(modifier = Modifier
                            .clickable(onClick = {
                                //currentMonth--
                                viewModel.onSwipe(currentMonth-1)
                            })
                            .background(MaterialTheme.colorScheme.primary),
                            ){
                            Text(text = " < ",color = MaterialTheme.colorScheme.onPrimary)     }
                        Text(text = monthName)
                        Box(modifier = Modifier
                            .clickable(onClick = {viewModel.onSwipe(currentMonth+1)})
                            .background(MaterialTheme.colorScheme.primary),
                        ){
                            Text(text = " > ",color = MaterialTheme.colorScheme.onPrimary)     }
                    }
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
                        if (dragAmount < -300) {
                            change.consume()
                            viewModel.onSwipe(currentMonth+1)
                        }
                        else if (dragAmount > 300) {
                            change.consume()
                            viewModel.onSwipe(currentMonth-1)
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
                    Column{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = exp.note)
                            //Text(text = exp.tranType)
                            if (exp.tranType == "Income")
                                Text(
                                    text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                    color = incomeColor,
                                )
                            else if (exp.tranType == "Transfer")
                                Text(
                                    text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                    color = transferColor,
                                )
                            else
                                Text(
                                    text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                    color = expenseColor,
                                )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = exp.budName,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium, )
                            Text(text = exp.accName,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium, )
                            Text(
                                text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium, )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
fun ExpListScreenPreview(){
    NewNavTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            topBar = {
                Box(modifier = Modifier
                    .padding(top=50.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        , horizontalArrangement = Arrangement.Absolute.Right)
                    {
                        Box(modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary),
                        ){
                            Text(text = " < ",color = MaterialTheme.colorScheme.onPrimary)     }
                        Text(text = "MON")
                        Box(modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary),
                        ){
                            Text(text = " > ",color = MaterialTheme.colorScheme.onPrimary)     }
                    }
                }
            },

        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = paddingValues,
            ){
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        ,horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = "2024-01-10",
                            style = typography.titleLarge,
                        )
                        Text(text = "1,000.00")
                    }
                }
                item {
                    Column{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = "exp.note")
                            //Text(text = exp.tranType)
                            Text(
                                text = "1,000.00",
                                color = incomeColor,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = "exp.budName",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium, )
                            Text(text = "exp.accName",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium, )
                            Text(
                                text = "1,000.00",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}