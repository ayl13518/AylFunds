package com.aylmer.aylfunds.transactions

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.ScreenAddTran
import com.aylmer.aylfunds.ScreenSetting
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.TransferTransactions
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import com.aylmer.aylfunds.ui.theme.expenseColor
import com.aylmer.aylfunds.ui.theme.incomeColor
import com.aylmer.aylfunds.ui.theme.transferColor
import com.aylmer.aylfunds.utils.DecimalFormatter
import com.aylmer.aylfunds.utils.getDayofWeek
import java.time.Month


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpTransViewModel = hiltViewModel(),
    onClickList: (tranId: Long, tranType: String) -> Unit,
    modifier: Modifier = Modifier,
){

    val state by viewModel.state.collectAsState()
    val transMonthList by viewModel.transMonthList.collectAsState(emptyList())
    val transferMonthList by viewModel.transferMonthList.collectAsStateWithLifecycle()
    val accountBalance by viewModel.accountBalance.collectAsStateWithLifecycle()
    val currentMonth = state.selectedMonth

//    val transbyDate = transMonthList
//        .groupBy { it.dateTrans }
//        .mapValues { (_, value) -> value.sortedBy { it.dateTrans }.reversed() }.toSortedMap(reverseOrder())
//
//    val sumByDate = transMonthList
//        .groupingBy { it.dateTrans }
//        .fold(0.0) { acc, expTrans -> acc + expTrans.amount }
//
//    val rollTransList=rollList(transMonthList,accountBalance)

    val rollTransList=roll2(transMonthList,transferMonthList,accountBalance)

    val sumByDate = rollTransList
        .groupingBy { it.dateTrans }
        .fold(0.0) { acc, expTrans -> acc + expTrans.amount }

    val transByDate = rollTransList
        .groupBy { it.dateTrans }
        .mapValues { (_, value) -> value.sortedBy { it.dateTrans } }.toSortedMap(reverseOrder())

    val decimalFormatter = DecimalFormatter()

    val monthName= Month.of(currentMonth+1).toString().take(3)


    Scaffold(
        modifier = modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
                AylTopBar(
                    titleRes = "",
                    navigationIcon = Icons.Rounded.Dehaze,
                    navigationIconContentDescription = "Navigation icon",
                    onNavigationClick = {navController.navigate(ScreenSetting)},
                    actionIcon = Icons.AutoMirrored.Default.Help,
                    actionIconContentDescription = "Action icon",
                )

                Box(modifier = Modifier
                    .padding(top = 90.dp)
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
                                viewModel.onSwipe(currentMonth - 1)
                            })
                            .background(MaterialTheme.colorScheme.primary),
                            ){
                            Text(text = " < ",color = MaterialTheme.colorScheme.onPrimary)     }
                        Text(text = monthName)
                        Box(modifier = Modifier
                            .clickable(onClick = { viewModel.onSwipe(currentMonth + 1) })
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
                    navController.navigate(ScreenAddTran(0))
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
                            viewModel.onSwipe(currentMonth + 1)
                        } else if (dragAmount > 300) {
                            change.consume()
                            viewModel.onSwipe(currentMonth - 1)
                        }
                    }
                }
                .fillMaxWidth(1f),
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            transByDate.forEach { (initialDate, expsByDate) ->
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
                            Text(
                                text = decimalFormatter.formatForVisual(sumByDate[initialDate].toString()),
                                modifier = Modifier.padding(end = 10.dp),
                                style = typography.titleLarge,
                            )
                        }
                    }
                items(expsByDate) { exp ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClickList(exp.id,exp.tranType) }
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(
                                    text = exp.note,
                                    modifier = Modifier.padding(start = 10.dp),
                                )

                                if (exp.tranType == "Income")
                                    Text(
                                        text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                        modifier = Modifier.padding(end = 10.dp),
                                        color = incomeColor,
                                    )
                                else if (exp.tranType == "Transfer" && exp.accNameTo != "")
                                    Text(
                                        text = decimalFormatter.formatForVisual(exp.amount.toString()),
                                        modifier = Modifier.padding(end = 10.dp),
                                        color = transferColor,
                                    )
//                                else if (exp.tranType == "Transfer")
//                                    Text(
//                                        text = decimalFormatter.formatForVisual(exp.amount.toString()),
//                                        modifier = Modifier.padding(end = 10.dp),
//                                        color = transferColor,
//                                    )
                                else
                                    Text(
                                        text = decimalFormatter.formatForVisual(exp.amount.toString()),
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
                                    text = exp.budName,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = typography.bodyMedium,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(.5f),
                                )
                                Text(
                                    text = exp.accName,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = typography.bodyMedium,
                                    modifier = Modifier.weight(.2f),
                                )
                                Text(
                                    text = decimalFormatter.formatForVisual(
                                        //rollTransList.find { it.id == exp.id }!!.balance.toString()
                                        exp.balance.toString()
                                    ),
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

data class RollingList(
    val accName: String,
    val amount: Double,
    val balance: Double,
    val dateTrans: String,
    val id: Long,

    val tranType: String="",
    val budName: String="",
    val note: String="",
    val accNameTo: String="",
)

data class BalanceList(
    val accName: String,
    val balance: Double,
    val rolling: Double,
)

fun roll2(expList: List<ExpTrans>,
          transferList: List<TransferTransactions>,
          accountList: List<accounts>) : List<RollingList> {

    var rollList = mutableListOf<RollingList>()
    var balanceList = mutableListOf<BalanceList>()

    expList.forEach { exp ->
        var roll = RollingList(
            accName = exp.accName,
            amount = exp.amount,
            balance = 0.0,
            dateTrans = exp.dateTrans,
            id = exp.id,
            tranType = exp.tranType,
            budName = exp.budName,
            note = exp.note,
        )
        rollList.add(roll)
    }

    transferList.forEach { tran ->
        var roll = RollingList(
            accName = tran.accName,
            amount = tran.amount,
            balance = 0.0,
            dateTrans = tran.dateTrans,
            id = tran.id,
            tranType = tran.tranType,
            budName = "",
            note = tran.note,
        )
        var roll2 = RollingList(
            accName = tran.accNameTo,
            amount = tran.amount,
            balance = 0.0,
            dateTrans = tran.dateTrans,
            id = tran.id,
            tranType = tran.tranType,
            budName = "",
            note = tran.note,
            accNameTo = tran.accNameTo,
        )

        rollList.add(roll)
        rollList.add(roll2)
    }

    accountList.forEach { exp ->
        balanceList.add(BalanceList(exp.name, exp.balance,0.0))
    }

    var currentBalance: Double = 0.0
    var tmpbal: Double = 0.0
    var tmpRoll: Double = 0.0
    var rollList2 = mutableListOf<RollingList>()

    rollList.sortedBy { it.dateTrans }
        .reversed()
        .forEach{ tran ->

            if ( tran.accNameTo=="" && balanceList.any { it.accName == tran.accName }) {

                tmpbal = balanceList.find { it.accName == tran.accName }!!.balance
                tmpRoll = balanceList.find { it.accName == tran.accName }!!.rolling
                balanceList.removeIf { it.accName == tran.accName }

                if (tran.tranType == TransactionType.Income.name)
                    balanceList.add(BalanceList(tran.accName, tmpbal + tmpRoll,  -tran.amount))
                else
                    balanceList.add(BalanceList(tran.accName, tmpbal + tmpRoll,  tran.amount))

                currentBalance = balanceList.find { it.accName == tran.accName }!!.balance
            }
            else if (tran.tranType == TransactionType.Transfer.name && balanceList.any { it.accName == tran.accNameTo }) {
                tmpbal = balanceList.find { it.accName == tran.accNameTo }!!.balance
                tmpRoll = balanceList.find { it.accName == tran.accNameTo }!!.rolling
                balanceList.removeIf { it.accName == tran.accNameTo }
                balanceList.add(BalanceList(tran.accNameTo, tmpbal + tmpRoll, + tran.amount))

                currentBalance = balanceList.find { it.accName == tran.accNameTo }!!.balance
            }
            else {
                balanceList.add(BalanceList(tran.accName, tran.amount,0.00))
                currentBalance=tran.amount
            }

            var roll = RollingList(
                accName = tran.accName,
                amount = tran.amount,
                balance = currentBalance,
                dateTrans = tran.dateTrans,
                id = tran.id,

                tranType = tran.tranType,
                budName = tran.budName,
                note = tran.note,
                accNameTo = tran.accNameTo,
            )

            rollList2.add(roll)

        }

    return rollList2
}

fun rollList(expList: List<ExpTrans>,
             accountList: List<accounts>) : List<RollingList> {
    val rollList = mutableListOf<RollingList>()

    var currentBalance: Double = 0.0
    var tmpbal: Double = 0.0
    var tmpRoll: Double = 0.0
    var balanceList = mutableListOf<BalanceList>()

    accountList.forEach { exp ->
        balanceList.add(BalanceList(exp.name, exp.balance,0.0))
    }

    expList.sortedBy { it.dateTrans }
        .reversed()
        .forEach { exp ->

        if (balanceList.any { it.accName == exp.accName }) {

            tmpbal = balanceList.find { it.accName == exp.accName }!!.balance
            tmpRoll = balanceList.find { it.accName == exp.accName }!!.rolling
            balanceList.removeIf { it.accName == exp.accName }

            if (exp.tranType == "Income")
                balanceList.add(BalanceList(exp.accName, tmpbal + tmpRoll, + exp.amount))
            else
                balanceList.add(BalanceList(exp.accName, tmpbal + tmpRoll, - exp.amount))

            currentBalance = balanceList.find { it.accName == exp.accName }!!.balance
        }
        else {
            balanceList.add(BalanceList(exp.accName, exp.amount,0.00))
            currentBalance=exp.amount
        }

        var roll = RollingList(
            accName = exp.accName,
            amount = exp.amount,
            balance = currentBalance,
            dateTrans = exp.dateTrans,
            id = exp.id,
        )

        rollList.add(roll)
    }

    return rollList
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
                    .padding(top = 50.dp)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = paddingValues,
            ){
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Row(modifier = Modifier
                        .fillMaxWidth(.95f)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(16.dp)
                        )
                        ,horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = getDayofWeek("2024-11-10") + " " + "2024-01-10",
                            modifier = Modifier.padding(start = 10.dp),
                            style = typography.titleLarge,
                        )
                        Text(text = "1,000.00"
                            , modifier = Modifier.padding(end = 10.dp),
                            )
                    }
                }
                items(3) {
                    Column(modifier = Modifier
                        .fillMaxWidth(.95f)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = "exp.note", Modifier.offset(10.dp))
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
                                style = typography.bodyMedium,
                                modifier = Modifier.weight(.3f))
                            Text(text = "exp.accName",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                                modifier = Modifier.weight(.3f),
                                )
                            Text(
                                text = "1,000.00",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = typography.bodyMedium,
                                modifier = Modifier.weight(.2f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Spacer(
                        Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .height(10.dp))
                }
            }
        }
    }
}