package com.example.newnav

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
import androidx.compose.material.icons.filled.Add
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.newnav.data.BudgetDatabase
import com.example.newnav.ui.theme.NewNavTheme
import kotlinx.serialization.Serializable
import com.example.newnav.viewmodels.expTransViewModel
import androidx.compose.runtime.getValue
import com.example.newnav.accounts.AccListScreen
import com.example.newnav.accounts.AccountTran
import com.example.newnav.navigation.NavigationBottomBar


//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            BudgetDatabase::class.java,
            "budgetdatabase.db"
        ).build()
    }

    private val viewModel by viewModels<expTransViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return expTransViewModel(db.expDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = true
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

            NewNavTheme {
                val state by viewModel.state.collectAsState()

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenHome
                ) {
                    composable<ScreenHome> {
                        Greeting(navController)
                    }
                    composable<ScreenTran> {
                        AddTranScreen(
                            onAmtChange = {viewModel.onAmountUpdate(it)},
                            navController= navController,
                            viewModel = viewModel
                        )
                    }
                    composable<ScreenExpenseList> {
                        ExpListScreen(
                            navController= navController,
                            viewModel = viewModel
                        )
                    }
                    composable<ScreenAccountList> {
                        AccListScreen(
                            navController = navController,
                        )
                    }
                    composable<ScreenAccount>{
                        AccountTran(
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
object ScreenTran

@Serializable
object ScreenAccount

@Composable
fun Greeting(
    navController: NavController
) {
    Scaffold (
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 0
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenTran)
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