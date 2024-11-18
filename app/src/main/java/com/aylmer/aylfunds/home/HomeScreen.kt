package com.aylmer.aylfunds.home


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aylmer.aylfunds.ScreenAddTran
import com.aylmer.aylfunds.ScreenSchedule
import com.aylmer.aylfunds.ScreenSetting
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.designsys.theme.Green80
import com.aylmer.aylfunds.designsys.theme.Red40
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import com.aylmer.aylfunds.utils.DecimalFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val decimalFormatter = DecimalFormatter()

    val mainList = state.accounts
    val groupTotal = mainList.fold(0.0) { total, acc ->
        total + acc.balance
    }

    val budList = state.budgets
    val budTotal = budList.groupingBy { it.type }.fold(0.0) { total, bud ->
        total + bud.balance
    }

    val transList = state.expTrans
    val transTotal = transList.groupingBy { it.tranType }.fold(0.0) { total, trans ->
        total + trans.amount
    }

    val budIncome = ((budTotal[TransactionType.Income.name] ?: 0.0).toDouble() /
            ((budTotal[TransactionType.Income.name] ?: 0.0).toDouble() +
                    (budTotal[TransactionType.Expense.name] ?: 0.0).toDouble())).toFloat()

    val budExpense = ((budTotal[TransactionType.Expense.name] ?: 0.0).toDouble() /
            ((budTotal[TransactionType.Income.name] ?: 0.0).toDouble() +
                    (budTotal[TransactionType.Expense.name] ?: 0.0).toDouble())).toFloat()

    val transIncome = ((transTotal[TransactionType.Income.name]
        ?: 0.0).toDouble() / (budTotal[TransactionType.Income.name] ?: 0.0).toDouble()).toFloat()
    val transExpense = ((transTotal[TransactionType.Expense.name]
        ?: 0.0).toDouble() / (budTotal[TransactionType.Expense.name] ?: 0.0).toDouble()).toFloat()

    Scaffold(
        topBar = {
            AylTopBar(
                titleRes = "Welcome",
                navigationIcon = Icons.Rounded.Dehaze,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = { navController.navigate(ScreenSetting) },
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Action icon",
                actionIcon2 = Icons.Default.AccessTime,
                actionIconContentDescription2 = "Schedule",
                onActionClick2 = { navController.navigate(ScreenSchedule) }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                navController = navController, selected = 0
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenAddTran(0))
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Total assets
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
//          // BarChart
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    //.background(
//                    //MaterialTheme.colorScheme.primaryContainer,
//                    //RoundedCornerShape(16.dp)
//                    //)
//                    .height(150.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                CustomBarChart(
//                    progress = transExpense,
//                    progressMax = budExpense,
//                    progressColor = Red40,
//                    progressMaxColor = com.aylmer.aylfunds.designsys.theme.Red30,
//                )
//                CustomBarChart(
//                    progress = transIncome,
//                    progressMax = budIncome,
//                    progressColor = Green80,
//                    progressMaxColor = com.aylmer.aylfunds.designsys.theme.Green40,
//                )
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    //.background(
//                    //MaterialTheme.colorScheme.primaryContainer,
//                    //RoundedCornerShape(16.dp)
//                    //)
//                    .height(30.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                Text(
//                    text = decimalFormatter.formatForVisual(
//                        budTotal[TransactionType.Expense.name].toString()
//                    ),
//                    modifier = Modifier.padding(end = 10.dp),
//                    style = typography.titleMedium,
//                )
//                Text(
//                    text = decimalFormatter.formatForVisual(
//                        budTotal[TransactionType.Income.name].toString()
//                    ),
//                    modifier = Modifier.padding(end = 10.dp),
//                    style = typography.titleMedium,
//                )
//            }

            // graph2 progress bar
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,

                ) {
                Text(text = "Category", Modifier.weight(.25f))
                Text(text = "Actual", Modifier.weight(.35f))
                Text(
                    text = "Budget", Modifier.weight(.4f), textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = " ", Modifier.weight(.25f))
                Text(text = decimalFormatter.formatForVisual(
                    transTotal[TransactionType.Expense.name].toString()),
                    Modifier.weight(.35f))
                Text(
                    text =  decimalFormatter.formatForVisual(
                        budTotal[TransactionType.Expense.name].toString()),
                    Modifier.weight(.4f),
                    textAlign = TextAlign.End)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "Expense",
                    modifier = Modifier.weight(.25f)
                )
                CustomProgressBar(
                    progress = transExpense,
                    progressColor = Red40,
                    modifier = Modifier.weight(.75f)
                )
            }
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = " ", Modifier.weight(.25f))
                Text(text = decimalFormatter.formatForVisual(
                    transTotal[TransactionType.Income.name].toString()),
                    Modifier.weight(.35f))
                Text(
                    text =  decimalFormatter.formatForVisual(
                        budTotal[TransactionType.Income.name].toString()),
                    Modifier.weight(.4f),
                    textAlign = TextAlign.End)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "Income",
                    modifier = Modifier.weight(.25f)
                )
                CustomProgressBar(
                    progress = transIncome,
                    progressColor = Green80,
                    modifier = Modifier.weight(.75f)
                )

            }
        }
    }
}

@Composable
fun CustomBarChart(
    modifier: Modifier = Modifier,
    progress: Float,
    progressMax: Float = 1f,
    progressColor: Color = Green80,
    progressMaxColor: Color = com.aylmer.aylfunds.designsys.theme.Green40,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val theWidth = 50.dp
    val theHeight = 100.dp

    Canvas(
        modifier = Modifier
            .width(theWidth)
            .height(theHeight)
            .border(1.dp, color = backgroundColor)
    )
    {
        val canvasHeight = size.height
        val progressHeight = size.height * if (progress > 1f) 1f else progress
        val progressMax = size.height * progressMax

        drawRect(
            color = progressMaxColor,
            size = Size(theWidth.toPx(), progressMax),
            topLeft = Offset(x = 0.dp.toPx(), canvasHeight - progressMax)
        )

        drawRect(
            color = progressColor,
            size = Size(theWidth.toPx(), progressHeight),
            topLeft = Offset(x = 0.dp.toPx(), canvasHeight - progressHeight)
        )
    }
}

@ThemePreviews
@Composable
fun BarChartPreview() {
    NewNavTheme {
        //BarChart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    //RoundedCornerShape(16.dp)
                )
                .height(150.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        )
        {
            CustomBarChart(
                progress = .7f, progressMax = .8f,
                progressColor = Red40,
                progressMaxColor = com.aylmer.aylfunds.designsys.theme.Red30,
            )
            CustomBarChart(
                progress = .5f, progressMax = .7f,
                progressColor = Green80,
                progressMaxColor = com.aylmer.aylfunds.designsys.theme.Green40,
            )
        }
    }
}


@Composable
fun CustomProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    Box(
        modifier = modifier
            //.clip(RoundedCornerShape(16.dp))
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .height(20.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer, RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))

        //.padding(start = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(progressColor)
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .shadow(8.dp, RoundedCornerShape(16.dp))
            //.offset(x = 10.dp)
        )
    }
}

@ThemePreviews
@Composable
fun ProgressPreview() {
    NewNavTheme {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,

                ) {
                Text(text = "Category", Modifier.weight(.25f))
                Text(text = "Actual", Modifier.weight(.35f))
                Text(
                    text = "Budget", Modifier.weight(.4f), textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = " ", Modifier.weight(.25f))
                Text(text = "$100", Modifier.weight(.35f))
                Text(
                    text = "10,000", Modifier.weight(.4f), textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "Expense",
                    modifier = Modifier.weight(.25f)
                )
                CustomProgressBar(
                    progress = .2F,
                    progressColor = Red40,
                    modifier = Modifier.weight(.75f)
                )
            }
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = " ", Modifier.weight(.25f))
                Text(text = "$100", Modifier.weight(.35f))
                Text(
                    text = "10,000", Modifier.weight(.4f),
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "Income",
                    modifier = Modifier.weight(.25f)
                )
                CustomProgressBar(
                    progress = .2F,
                    progressColor = Green80,
                    modifier = Modifier.weight(.75f)
                )
            }
        }
    }
}