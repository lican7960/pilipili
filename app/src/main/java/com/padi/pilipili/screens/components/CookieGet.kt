package com.padi.pilipili.screens.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.unit.dp
import com.padi.pilipili.hook.CookieUtils
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.LocalWindowBottomSheetState
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.WindowBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun CookieGet() {
    val showDialog = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(CookieUtils.getBiliCookie()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SuperArrow(
        title = "获取Cookie", summary = "用户的登录凭证清不要外泄", onClick = {
            showDialog.value = true
        })

    WindowBottomSheet(
        onDismissRequest = { showDialog.value = false }, title = "获取Cookie", show = showDialog
    ) {

        TextField(
            value = text,
            label = "Cookie",
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

        Spacer(Modifier.height(16.dp))
        val dismiss = LocalWindowBottomSheetState.current
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                modifier = Modifier.weight(1f), text = "取消", onClick = { dismiss?.invoke() })
            Spacer(Modifier.width(16.dp))
            Button(
                modifier = Modifier.weight(1f), onClick = {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", text)
                    clipboardManager.setPrimaryClip(clip)
                    dismiss?.invoke()
                }, colors = ButtonDefaults.buttonColorsPrimary()
            ) {
                Text("复制", color = MiuixTheme.colorScheme.onPrimary)
            }
        }
        Spacer(Modifier.height(16.dp))
    }

}
