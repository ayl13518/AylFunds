package com.example.newnav.preference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.designsys.component.DropdownList
import com.example.newnav.navigation.AylTopBar
import com.example.newnav.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {

    val state by viewModel.state.collectAsState()
    val categoryList by viewModel.categoryFiltered.collectAsState(emptyList())
    var accountList = state.accountList
    var defaultAccount = state.defaultAccount

    Scaffold (
        topBar = {
            AylTopBar(
                titleRes = "Preferences",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = {navController.popBackStack()},
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Action icon",
            )
        },
//        bottomBar = {
//            NavigationBottomBar(
//                navController= navController
//                , selected = 0
//            )
//        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Add")
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "SettingsScreen")


            DropdownList(
                label = "Default Expense Category",
                itemList = categoryList,
                onTypeChange = {  },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = ""
            )

            DropdownList(
                label = "Default Income Category",
                itemList = categoryList,
                onTypeChange = {  },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = ""
            )

            DropdownList(
                label = "Default Account",
                itemList = accountList,
                onTypeChange = { },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = defaultAccount.toString()
            )

            Text(text = defaultAccount.toString())

        }
    }
}