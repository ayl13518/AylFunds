package com.aylmer.aylfunds.preference


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.designsys.component.DropdownList
import com.aylmer.aylfunds.navigation.AylTopBar
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var enableBackup by remember { mutableStateOf(true) }

    val state by viewModel.state.collectAsState()
//    val categoryList by viewModel.categoryFiltered.collectAsState(emptyList())
    var accountList = state.accountList
    var defaultAccount = state.defaultAccount
    var checked by remember { mutableStateOf(false) }

    viewModel.files.asList()

    checked = state.useDarkTheme == "true"



    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            AylTopBar(
                titleRes = "Preferences",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = { navController.popBackStack() },
                actionIcon = Icons.AutoMirrored.Default.Help,
                actionIconContentDescription = "Action icon",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Text(text = "SettingsScreen")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                //.background(MaterialTheme.colorScheme.primaryContainer)
                , horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Use DarkTheme")
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        if (it == true) viewModel.onUpdateDarkTheme("true")
                        else viewModel.onUpdateDarkTheme("false")
                    }
                )
            }

//            DropdownList(
//                label = "Default Expense Category",
//                itemList = categoryList,
//                onTypeChange = {  },
//                modifier = Modifier.fillMaxWidth(),
//                defaultSelectedItem = ""
//            )
//
//            DropdownList(
//                label = "Default Income Category",
//                itemList = categoryList,
//                onTypeChange = {  },
//                modifier = Modifier.fillMaxWidth(),
//                defaultSelectedItem = ""
//            )

            DropdownList(
                label = "Default Account",
                itemList = accountList,
                onTypeChange = { viewModel.onUpdateDefaultAccount(it) },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = defaultAccount.toString()
            )

            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            Button(
                onClick = {
                    enableBackup = false
                    viewModel.onBackup(
                        scope,
                        snackBarHostState
                    )
                },
                enabled = enableBackup,
            ) {
                Text(text = "Backup Database")
            }

            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            Button(
                onClick = {
                    enableBackup = false
                    viewModel.onLoadBackUp(
                        scope,
                        snackBarHostState
                    )
                },
                enabled = enableBackup,
            ) {
                Text(text = "Restore Database")
            }

            // for files
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            FilePicker()
//            Button(
//                onClick = {
//                    enableBackup=false
//                    viewModel.onCreateFile(
//                        scope,
//                        snackBarHostState
//                    )
//                },
//                enabled = enableBackup,
//            ) {
//                Text(text = "Create File")
//            }

        }
    }
}


@Composable
fun FilePicker() {

    val context = LocalContext.current
    val exportDir = File(context.filesDir, "export")
    val accounts = File(exportDir, "accounts.csv")
    val budgets = File(exportDir, "budgets.csv")
    val file3 = File(exportDir, "transactions.csv")
    val file4 = File(exportDir, "transfer.csv")
    val file5 = File(exportDir, "schedules.csv")
    val sourceFile = File(context.filesDir, "export.zip")

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->

                zipFiles(listOf(accounts, budgets, file3, file4, file5), sourceFile)

                val contentResolver = context.contentResolver
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    FileInputStream(sourceFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }

    Button(onClick = { launch(launcher, context) }) {
        Text(text = "Copy BackUp to External")
    }

}

private fun launch(launcher: ActivityResultLauncher<Intent>, context: Context) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/zip"
        putExtra(Intent.EXTRA_TITLE, "newfile.zip")
    }
    launcher.launch(intent)
}

fun zipFiles(files: List<File>, zipFile: File) {
    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { out ->
        files.forEach { file ->
            FileInputStream(file).use { fi ->
                BufferedInputStream(fi).use { origin ->
                    val entry = ZipEntry(file.name)
                    out.putNextEntry(entry)
                    origin.copyTo(out, 1024)
                }
            }
        }
    }
}
