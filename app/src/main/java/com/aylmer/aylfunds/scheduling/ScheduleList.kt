package com.aylmer.aylfunds.scheduling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.R
import com.aylmer.aylfunds.ScreenAccount
import com.aylmer.aylfunds.ScreenAddSchedule
import com.aylmer.aylfunds.ScreenSchedule
import com.aylmer.aylfunds.accounts.AccountViewModel
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.navigation.NavigationBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: SchedulingViewModel = hiltViewModel(),
    //onClickList: (id: Long) -> Unit
){

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,

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
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.activity_horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {

            AylTopBar(
                titleRes = "Schedule",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigate Back",
                onNavigationClick = { navController.popBackStack() },
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Help",
//                actionIcon2 = Icons.Default.AccessTime,
//                actionIconContentDescription2 = "Schedule",
            )
        }
    }

}