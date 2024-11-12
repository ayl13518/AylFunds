package com.aylmer.aylfunds.scheduling

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help

import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.aylmer.aylfunds.ScreenAddSchedule

import com.aylmer.aylfunds.navigation.AylTopBar

import com.aylmer.aylfunds.ui.theme.expenseColor
import com.aylmer.aylfunds.ui.theme.incomeColor

import com.aylmer.aylfunds.utils.DecimalFormatter
import com.aylmer.aylfunds.utils.getDayofWeek

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: SchedulingViewModel = hiltViewModel(),
    onClickList: (tranId: Long, tranType: String) -> Unit,
) {

    val allSchedule = viewModel.allSchedule.collectAsStateWithLifecycle()

    val scheduleByDate = allSchedule.value
        .groupBy { it.dateTrans }
        .mapValues { (_, value) -> value.sortedBy { it.dateTrans } }.toSortedMap(reverseOrder())

    val decimalFormatter = DecimalFormatter()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            AylTopBar(
                titleRes = "Schedule",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigate Back",
                onNavigationClick = { navController.popBackStack() },
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Help",
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenAddSchedule(0))
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
    { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(1f),
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            scheduleByDate.forEach { (initialDate, schedule) ->
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.95f)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            ), horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = getDayofWeek(initialDate) + " " + initialDate,
                            modifier = Modifier.padding(start = 10.dp),
                            style = typography.titleLarge,
                        )
//                        Text(
//                            text = decimalFormatter.formatForVisual(sumByDate[initialDate].toString()),
//                            modifier = Modifier.padding(end = 10.dp),
//                            style = typography.titleLarge,
//                        )
                    }
                }

                items(schedule) { schedule ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClickList(schedule.id, schedule.tranType) }
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(
                                text = schedule.note,
                                modifier = Modifier.padding(start = 10.dp),
                            )

                            if (schedule.tranType == "Income")
                                Text(
                                    text = decimalFormatter.formatForVisual(schedule.amount.toString()),
                                    modifier = Modifier.padding(end = 10.dp),
                                    color = incomeColor,
                                )
//                            else if (schedule.tranType == "Transfer" && schedule.accNameTo != "")
//                                Text(
//                                    text = decimalFormatter.formatForVisual(schedule.amount.toString()),
//                                    modifier = Modifier.padding(end = 10.dp),
//                                    color = transferColor,
//                                )
//                                else if (exp.tranType == "Transfer")
//                                    Text(
//                                        text = decimalFormatter.formatForVisual(exp.amount.toString()),
//                                        modifier = Modifier.padding(end = 10.dp),
//                                        color = transferColor,
//                                    )
                            else
                                Text(
                                    text = decimalFormatter.formatForVisual(schedule.amount.toString()),
                                    modifier = Modifier.padding(end = 10.dp),
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
                            Text(
                                text = schedule.budName,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .weight(.5f),
                            )
                            Text(
                                text = schedule.accName,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                                modifier = Modifier.weight(.2f),
                            )
                            Text(
                                text = schedule.period,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .weight(.2f),
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(
                            Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .height(5.dp)
                        )
                    }
                }
            }
        }
    }
}