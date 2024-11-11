package com.aylmer.aylfunds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme

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
                actionIcon2 = Icons.Default.AccessTime,
                actionIconContentDescription2 = "Schedule",
                onActionClick2 = {navController.navigate(ScreenSchedule)}
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewNavTheme {
        Greeting( navController =  rememberNavController()
        )
    }
}