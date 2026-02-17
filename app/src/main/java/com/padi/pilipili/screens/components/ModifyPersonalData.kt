package com.padi.pilipili.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.LocalWindowBottomSheetState
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.extra.WindowBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun ModifyPersonalData() {
    val showDialog = remember { mutableStateOf(false) }

    val spHelper = SPHelper.getInstance()

    var isEnabled by remember {
        mutableStateOf(spHelper.get("enable_modify_personal_data", false))
    }

    val savedJson = spHelper.get("modify_personal_data", "")
    val savedPersonData = remember(savedJson) {
        runCatching {
            if (savedJson.isNotEmpty()) {
                Gson().fromJson(savedJson, PersonData::class.java)
            } else null
        }.getOrNull()
    }
    val context = LocalContext.current
    val inputFields = remember {
        mutableStateListOf(
            InputField("name", "昵称", isNumber = false, value = savedPersonData?.name ?: ""),
            InputField("dynamic", "动态", value = savedPersonData?.dynamic?.toString() ?: ""),
            InputField("following", "关注", value = savedPersonData?.following?.toString() ?: ""),
            InputField("follower", "粉丝", value = savedPersonData?.follower?.toString() ?: ""),
            InputField(
                "newFollowers", "新增粉丝", value = savedPersonData?.newFollowers?.toString() ?: ""
            ),
            InputField("bcoin", "B币", value = savedPersonData?.bcoin?.toString() ?: ""),
            InputField("coin", "硬币", value = savedPersonData?.coin?.toString() ?: ""),
            InputField("rank", "排名", value = savedPersonData?.rank?.toString() ?: "")
        )
    }


    SuperArrow(
        title = "修改个人信息", summary = "伪造自己的个人信息，只能自己看见", onClick = {
            showDialog.value = true
        })

    WindowBottomSheet(
        onDismissRequest = { showDialog.value = false },
        title = "修改个人信息",
        show = showDialog,
    ) {
        Card {
            SuperSwitch(
                title = "开启功能", checked = isEnabled, onCheckedChange = {
                    isEnabled = it
                    spHelper.put("enable_modify_personal_data", isEnabled)
                })
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()
        ) {
            items(inputFields) { field ->
                InputFieldItem(
                    inputField = field, modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        val dismiss = LocalWindowBottomSheetState.current
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                modifier = Modifier.weight(1f), text = "取消", onClick = { dismiss?.invoke() })
            Spacer(Modifier.width(16.dp))
            Button(
                modifier = Modifier.weight(1f), onClick = {
                    if (isEnabled) {
                        val personData = inputFields.toPersonData()
                        val jsonString = Gson().toJson(personData)
                        runCatching {
                            spHelper.put("modify_personal_data", jsonString)
                        }.onFailure {
                            it.log()
                        }
                    }
                    dismiss?.invoke()
                }, colors = ButtonDefaults.buttonColorsPrimary()
            ) {
                Text("保存", color = MiuixTheme.colorScheme.onPrimary)
            }
        }
        Spacer(Modifier.height(16.dp))

    }
}


@Composable
fun InputFieldItem(
    inputField: InputField, modifier: Modifier = Modifier
) {

    TextField(
        label = inputField.label,
        value = inputField.value,
        onValueChange = { inputField.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        keyboardOptions = if (inputField.isNumber) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        }
    )
}

// 数据模型
class InputField(
    val key: String, val label: String, val isNumber: Boolean = true, value: String = ""
) {
    var value by mutableStateOf(value)
}

data class PersonData(
    val name: String?,
    val dynamic: Int?,
    val following: Int?,
    val follower: Int?,
    val newFollowers: Int?,
    val bcoin: Int?,
    val coin: Int?,
    val rank: Int?
)

// 转换扩展函数
fun List<InputField>.toPersonData(): PersonData {
    fun getInt(key: String) = this.find { it.key == key }?.value?.toIntOrNull()
    fun getString(key: String) = this.find { it.key == key }?.value.orEmpty()

    return PersonData(
        name = getString("name"),
        dynamic = getInt("dynamic"),
        following = getInt("following"),
        follower = getInt("follower"),
        newFollowers = getInt("newFollowers"),
        bcoin = getInt("bcoin"),
        coin = getInt("coin"),
        rank = getInt("rank")
    )
}
