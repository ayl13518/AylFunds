package com.aylmer.aylfunds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState


import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import kotlinx.serialization.Serializable
import com.aylmer.aylfunds.accounts.AccListScreen
import com.aylmer.aylfunds.accounts.AccountTran
import com.aylmer.aylfunds.budgets.BudgetListScreen
import com.aylmer.aylfunds.budgets.BudgetTran
import com.aylmer.aylfunds.models.TransactionType

import com.aylmer.aylfunds.models.UserData
import com.aylmer.aylfunds.preference.SettingsScreen
import com.aylmer.aylfunds.transactions.AddTranScreen
import com.aylmer.aylfunds.transactions.ExpListScreen
import com.aylmer.aylfunds.preference.SettingsViewModel
import com.aylmer.aylfunds.scheduling.AddSchedule
import com.aylmer.aylfunds.scheduling.ScheduleScreen
import com.aylmer.aylfunds.workers.DailyInterest


import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import kotlin.reflect.typeOf


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val interestWorker = PeriodicWorkRequestBuilder<DailyInterest>(
//            //1, TimeUnit.DAYS,
//            15, TimeUnit.MINUTES,
//            15, TimeUnit.MINUTES
//        ).build()
//
//        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("interest2",ExistingPeriodicWorkPolicy.UPDATE,interestWorker)
        val interestWorker = OneTimeWorkRequestBuilder<DailyInterest>().build()
        WorkManager.getInstance(applicationContext).enqueue(interestWorker)

       // var uiState: UserData by mutableStateOf(UserData())
        val userData= viewModel.state


        enableEdgeToEdge()
        setContent {
            //val darkTheme = isSystemInDarkTheme()
            val darkTheme = shouldUseDarkTheme(userData.collectAsState().value)
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            NewNavTheme(
                darkTheme = darkTheme,
                dynamicColor = true
            ) {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenHome
                ) {
                    composable<ScreenHome> {
                        Greeting(navController)
                    }
                    composable<ScreenAddTran> (
                        typeMap = mapOf(
                            typeOf<Long>() to NavType.LongType,
                            typeOf<String>() to NavType.StringType
                            )
                    ){
                        val arguments = it.toRoute<ScreenAddTran>()
                        AddTranScreen(
                            navController = navController,
                            tranId = arguments.tranId,
                            tranType = arguments.tranType
                        )
                    }
                    composable<ScreenExpenseList> {
                        ExpListScreen(
                            navController = navController,
                            onClickList = { tranId, tranType -> navController.navigate(
                                ScreenAddTran(tranId, tranType))
                            }
                        )
                    }
                    composable<ScreenAccountList> {
                        AccListScreen(
                            navController = navController,
                            onClickList = { accountId -> navController.navigate(
                                ScreenAccount(accountId))
                            }
                        )
                    }
                    composable<ScreenAccount>(
                        typeMap = mapOf(
                            typeOf<Long>() to NavType.LongType
                        )
                    ){
                        val arguments = it.toRoute<ScreenAccount>()
                        AccountTran(
                            navController = navController,
                            accountId = arguments.accountId
                        )
                    }
                    composable<ScreenBudgetList>{
                       BudgetListScreen(
                           navController = navController,
                           onClickList = { budgetId -> navController.navigate(
                               ScreenBudget(budgetId) )
                           }
                        )
                    }
                    composable<ScreenBudget>(
                        typeMap = mapOf(
                            typeOf<Long>() to NavType.LongType
                        )
                    ){
                        val arguments = it.toRoute<ScreenBudget>()
                        BudgetTran(
                            budgetId = arguments.budgetID,
                            navController = navController,
                        )
                    }
                    composable<ScreenSetting>{
                        SettingsScreen(
                            navController = navController,
                        )
                    }
                    composable<ScreenSchedule>{
                        ScheduleScreen(
                            navController = navController,
                            onClickList = { id, tranType -> navController.navigate(
                                ScreenAddTran(id, tranType))
                            }
                        )
                    }
                    composable<ScreenAddSchedule>(
                        typeMap = mapOf(
                            typeOf<Long>() to NavType.LongType,
                            typeOf<String>() to NavType.StringType
                        )
                    ){
                        val arguments = it.toRoute<ScreenAddSchedule>()
                        AddSchedule(
                            id = arguments.id,
                            tranType = arguments.tranType,
                            navController = navController,
                        )
                    }
                }

            }
        }
    }
}

@Serializable
object ScreenHome

@Serializable
object ScreenExpenseList

@Serializable
object ScreenAccountList

@Serializable
object ScreenBudgetList

@Serializable
data class ScreenAddTran (
    val tranId: Long,
    val tranType: String = TransactionType.Expense.name
)

@Serializable
data class ScreenAccount (
    val accountId: Long,
)

@Serializable
data class ScreenBudget (
    val budgetID: Long,
)

@Serializable
object ScreenSetting

@Serializable
object ScreenSchedule

@Serializable
data class ScreenAddSchedule (
    val id: Long,
    val tranType: String = TransactionType.Expense.name
)



/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: UserData,
): Boolean
{
    var useDarkTheme = false

    useDarkTheme = if(uiState.useDarkTheme=="true") true else false

    return useDarkTheme
}


/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

