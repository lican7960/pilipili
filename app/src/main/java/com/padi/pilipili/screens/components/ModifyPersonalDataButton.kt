package com.padi.pilipili.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper

@Composable
fun ModifyPersonalDataButton() {
    var showDialog by remember { mutableStateOf(false) }

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

    Button(onClick = { showDialog = true }) {
        Text("修改个人信息")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("修改个人信息") },
            text = {
                Column {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                isEnabled = !isEnabled
                                spHelper.put("enable_modify_personal_data", isEnabled)
                            }, headlineContent = { Text("开启功能") }, trailingContent = {
                            Switch(checked = isEnabled, onCheckedChange = {
                                isEnabled = it
                                spHelper.put("enable_modify_personal_data", it)
                            })
                        })
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()
                    ) {
                        items(inputFields) { field ->
                            InputFieldItem(
                                inputField = field, modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isEnabled) {
                            val personData = inputFields.toPersonData()
                            val jsonString = Gson().toJson(personData)
                            runCatching {
                                spHelper.put("modify_personal_data", jsonString)
                            }.onFailure {
                                it.log()
                            }
                        }
                        showDialog = false
                    }) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("关闭")
                }
            })
    }
}


@Composable
fun InputFieldItem(
    inputField: InputField, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = inputField.label, modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = inputField.value,
            onValueChange = { inputField.value = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = if (inputField.isNumber) {
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
            }
        )
    }
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
