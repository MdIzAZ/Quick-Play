package com.pro.quickplay.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowSettingsRedirectDialog(
    context: Context,
    onDismissDialog: ()->Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismissDialog()
        },
        title = { Text("Permission Required") },
        text = {
            Text("Go to settings to allow media access, as it is needed.")
        },
        confirmButton = {
            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + context.packageName)
                    }
                    context.startActivity(intent)
                },
                content = {
                    Text("Go to Settings")
                }
            )
        },
        dismissButton = {
            Button(
                onClick = { onDismissDialog() },
                content = {
                    Text("Cancel")
                }
            )
        }
    )
}