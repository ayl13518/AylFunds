package com.aylmer.aylfunds.funds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.ScreenSchedule
import com.aylmer.aylfunds.ScreenSetting
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar
import com.aylmer.aylfunds.ui.theme.NewNavTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreen(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            AylTopBar(
                titleRes = "Funds",
                navigationIcon = Icons.Rounded.Dehaze,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = { navController.navigate(ScreenSetting) },
                actionIcon = Icons.Default.AddBox,
                actionIconContentDescription = "Add fund",
                onActionClick = { /* TODO: navigate to add-fund screen */ },
                actionIcon2 = Icons.Default.AccessTime,
                actionIconContentDescription2 = "Schedule",
                onActionClick2 = { navController.navigate(ScreenSchedule) }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                navController = navController, selected = 4
            )
        },
    ) { paddingValues ->
        // TODO: replace with the funds list
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Funds",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@ThemePreviews
@Composable
fun FundsScreenPreview() {
    NewNavTheme {
        FundsScreen()
    }
}
