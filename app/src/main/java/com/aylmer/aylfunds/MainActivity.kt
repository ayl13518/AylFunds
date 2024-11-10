package com.aylmer.aylfunds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import kotlinx.serialization.Serializable
import com.aylmer.aylfunds.accounts.AccListScreen
import com.aylmer.aylfunds.accounts.AccountTran
import com.aylmer.aylfunds.budgets.BudgetListScreen
import com.aylmer.aylfunds.budgets.BudgetTran
import com.aylmer.aylfunds.models.TransactionType

import com.aylmer.aylfunds.models.UserData
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.preference.SettingsScreen
import com.aylmer.aylfunds.transactions.AddTranScreen
import com.aylmer.aylfunds.transactions.ExpListScreen
import com.aylmer.aylfunds.preference.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlin.reflect.typeOf


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    navController: NavController
) {
    Scaffold (
        topBar = {
            AylTopBar(
                titleRes = "Welcome",
                navigationIcon = Icons.Rounded.Dehaze,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = {navController.navigate(ScreenSetting)},
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Action icon",
            )
        },
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 0
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
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "HomeScreen")


        }
    }

}


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

    if(uiState.useDarkTheme=="true") useDarkTheme=true
    else useDarkTheme=false
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewNavTheme {
        Greeting( navController =  rememberNavController()
        )
    }
}