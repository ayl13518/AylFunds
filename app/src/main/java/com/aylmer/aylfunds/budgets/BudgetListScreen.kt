package com.aylmer.aylfunds.budgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.ScreenBudget
import com.aylmer.aylfunds.ScreenSchedule
import com.aylmer.aylfunds.ScreenSetting
import com.aylmer.aylfunds.designsys.component.AylTab
import com.aylmer.aylfunds.designsys.component.AylTabRow
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.designsys.theme.Green40
import com.aylmer.aylfunds.designsys.theme.Green80
import com.aylmer.aylfunds.designsys.theme.Red40
import com.aylmer.aylfunds.models.BudgetType
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import com.aylmer.aylfunds.utils.DecimalFormatter
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.random.Random


data class RollingBalance(
    val budgetName: String,
    val amount: Double,
    val balance: Double,
    val progress: Float = (amount/balance).toFloat(),
    val textOut: String = ""
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: BudgetViewModel = hiltViewModel(),
    onClickList: (budgetId: Long) -> Unit
){
    val decimalFormatter = DecimalFormatter()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val transMonthList by viewModel.transMonthList.collectAsStateWithLifecycle()
    var budgetTypes = BudgetType.entries.map { it.name }

    val accbyType = state.budgets
        //.sortedBy { it.type }
        .sortedByDescending { it.balance }
        .sortedBy { it.type }
        .groupBy { it.type }


    val allBudget = state.budgets

    val sumByBudget = transMonthList
        .groupingBy { it.budName }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount  }

    val progress = allBudget.map { budget ->
        RollingBalance(
            budgetName = budget.name,
            amount = sumByBudget[budget.name] ?: 0.0,
            balance = budget.balance,
            textOut = decimalFormatter.formatForVisual( (sumByBudget[budget.name] ?: 0.0).toString() )
                    + " of " + decimalFormatter.formatForVisual( budget.balance.toString())
        )
    }

    val sumBudgetType =allBudget
        .groupingBy { it.type }
        .fold(0.0) { acc, expTrans -> acc + expTrans.balance  }

    val sumTransType = transMonthList
        .groupingBy { it.tranType }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount  }



    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            AylTopBar(
                titleRes = "Budget",
                navigationIcon = Icons.Rounded.Dehaze,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = { navController.navigate(ScreenSetting) },
                actionIcon = Icons.Default.AddBox,
                actionIconContentDescription = "Action icon",
                actionIcon2 = Icons.Default.AccessTime,
                actionIconContentDescription2 = "Schedule",
                onActionClick2 = {navController.navigate(ScreenSchedule)}
            )
        },
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 3
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ScreenBudget(0))},
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column()
        {
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .offset(y = paddingValues.calculateTopPadding()),
                contentAlignment = Alignment.BottomEnd) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    , horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    , verticalAlignment = Alignment.CenterVertically)
                {
                    AylTabRow(selectedTabIndex = 0
                        , modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .width(200.dp)
                    )
                    {
                        budgetTypes.forEachIndexed { index, title ->
                             AylTab(
                                selected = index == 0,
                                onClick = {  },
                                text = { Text(text = title) },
                            )
                        }
                    }

                    Spacer(Modifier.width(50.dp))

                    Box(modifier = Modifier
                        .height(40.dp)
                        .clickable(onClick = {
                            //currentMonth--
                            //viewModel.onSwipe(currentMonth-1)
                        })
                        .align(Alignment.CenterVertically)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    ){
                        Text(text = " < ",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.align(Alignment.Center)
                            )
                    }
                    Text(text = "Hello")
                    Box(modifier = Modifier
                        .height(40.dp)
                        //.clickable(onClick = {viewModel.onSwipe(currentMonth+1)})
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    ){
                        Text(text = " > ",
                            //color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(paddingValues)
                    .fillMaxSize(),
                contentPadding = paddingValues
            ) {
                accbyType.forEach { (initialDate, expsByDate) ->
                    stickyHeader {
                        Spacer(Modifier.height(30.dp))
                        Text(
                            text = initialDate,
                            textAlign = TextAlign.Center,
                            style = typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .height(30.dp)
                        )
                        Text(
                            text = decimalFormatter.formatForVisual((sumTransType[initialDate]?: "0").toString()) +
                                    " of " +
                                    decimalFormatter.formatForVisual((sumBudgetType[initialDate]?: "0").toString()),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(end = 16.dp)
                        )
                        CustomProgressBar( progress = (sumTransType[initialDate]?: 0f).toFloat()  / (sumBudgetType[initialDate]?: 0f).toFloat(),
                            modifier = Modifier
                                .fillMaxWidth(.95f)
                                .offset(x = 10.dp),
                            progressColor= if(initialDate == TransactionType.Income.name) Green80 else Red40,
                        )
                    }
                    items(expsByDate) { item ->
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable { onClickList(item.budgetid) }
                                .fillMaxWidth()
                                .height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            )
                            Text(
                                text = progress.find { it.budgetName == item.name }?.textOut ?: "",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        val randomColor = Color( red = Random.nextFloat(),
                            green = Random.nextFloat(),
                            blue = Random.nextFloat(),
                            alpha = 1f )
                        CustomProgressBar( progress = progress.find { it.budgetName == item.name }?.progress ?: 0f,
                                modifier = Modifier
                                    .fillMaxWidth(.95f)
                                    .offset(x = 10.dp),
                                progressColor=randomColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color =MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer
)
{
    Box(
        modifier = modifier
            //.clip(RoundedCornerShape(16.dp))
            .background(backgroundColor,RoundedCornerShape(16.dp))
            .height(20.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer,RoundedCornerShape(16.dp))
            .shadow(8.dp,RoundedCornerShape(16.dp))
            //.padding(start = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(progressColor)
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .shadow(8.dp,RoundedCornerShape(16.dp))
                //.offset(x = 10.dp)
        )
    }
}



@ThemePreviews
@Composable
fun BudgetTabScreenPreview() {
    NewNavTheme {
        val titles = listOf("Expenses", "Income")
        AylTabRow(selectedTabIndex = 0
            , modifier = Modifier
                .clip(RoundedCornerShape(30.dp)) // Apply rounded corners
                .width(200.dp))
        {
            titles.forEachIndexed { index, title ->
                AylTab(
                    selected = index == 0,
                    onClick = { },
                    text = { Text(text = title) },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun BudgetProgressPreview() {
    NewNavTheme {
        var randomColor = Color( red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f )
        CustomProgressBar( progress = .8f,
            modifier = Modifier.fillMaxWidth()
            , progressColor =randomColor
        )
    }
}