package com.example.newnav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.navigation.NavigationBottomBar
import com.example.newnav.viewmodels.expTransViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: expTransViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    val transbyDate = state.expTrans.groupBy { it.dateTrans  }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBottomBar(
                navController= navController
                , selected = 1
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenTran)
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            transbyDate.forEach { (initialDate, expsByDate) ->
                stickyHeader {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    Text(text = initialDate,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium,
                    )
                }
                items(expsByDate) { exp ->
                    Text(text = exp.amount.toString())
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ExpListScreenPreview(){
    ExpListScreen( )
}