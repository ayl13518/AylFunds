package com.aylmer.aylfunds.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.aylmer.aylfunds.designsys.component.ThemePreviews
import com.aylmer.aylfunds.ui.theme.NewNavTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AylTopBar(
    titleRes: String="",
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    actionIcon2: ImageVector = Icons.Default.MoreVert,
    actionIconContentDescription2: String="",
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    onActionClick2: () -> Unit = {},
){
    CenterAlignedTopAppBar(
        title = { Text(text =  titleRes) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            if (actionIconContentDescription2.isNotEmpty()) {
                IconButton(onClick = onActionClick2) {
                    Icon(
                        imageVector = actionIcon2,
                        contentDescription = actionIconContentDescription2,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

        },
        colors = colors,
        modifier = modifier.testTag("aylTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
private fun NiaTopAppBarPreview() {
    NewNavTheme {
        AylTopBar(
            titleRes = "test",
            navigationIcon = Icons.Rounded.ArrowBackIosNew,
            navigationIconContentDescription = "Navigation icon",
            actionIcon = Icons.Default.MoreVert,
            actionIconContentDescription = "Action icon",
            actionIcon2 = Icons.Default.AccessTime,
            actionIconContentDescription2 = "Schedule",
        )
    }
}