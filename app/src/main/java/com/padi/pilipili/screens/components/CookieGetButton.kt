package com.padi.pilipili.screens.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.padi.pilipili.hook.CookieUtils
import com.padi.pilipili.utils.SPHelper

@Composable
fun CookieGetButton() {
    var showDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(CookieUtils.getBiliCookie()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Button(onClick = { showDialog = true }) {
        Text("获取Cookie")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("获取Cookie") },
            text = {
                OutlinedTextField(
                    value = text,
                    label = { Text("Cookie") },
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        val description = if (passwordVisible) "隐藏密码" else "显示密码"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", text)
                    clipboardManager.setPrimaryClip(clip)
                }) {
                    Text("复制")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("关闭")
                }
            })
    }
}
