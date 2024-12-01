package com.example.lab5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

data class EquipmentReliability(
    val failureRate: Double,
    val averageRepairTime: Int,
    val frequency: Double,
    val averageRecoveryTime: Int?
)

val data = mapOf(
    "T-110 kV" to EquipmentReliability(0.015, 100, 1.0, 43),
    "T-35 kV" to EquipmentReliability(0.02, 80, 1.0, 28),
    "T-10 kV (кабельна мережа 10 кВ)" to EquipmentReliability(0.005, 60, 0.5, 10),
    "T-10 kV (повітряна мережа 10 кВ)" to EquipmentReliability(0.05, 60, 0.5, 10),
    "B-110 kV (елегазовий)" to EquipmentReliability(0.01, 30, 0.1, 30),
    "B-10 kV (малолойний)" to EquipmentReliability(0.02, 15, 0.33, 15),
    "B-10 kV (вакуумний)" to EquipmentReliability(0.05, 15, 0.33, 15),
    "Збірні шини 10 кВ на 1 приєднання" to EquipmentReliability(0.03, 2, 0.33, 15),
    "АВ-0,38 кВ" to EquipmentReliability(0.05, 20, 1.0, 15),
    "ЕД 6,10 кВ" to EquipmentReliability(0.1, 50, 0.5, 0),
    "ЕД 0,38 кВ" to EquipmentReliability(0.1, 50, 0.5, 0),
    "ПЛ-110 кВ" to EquipmentReliability(0.007, 10, 0.167, 35),
    "ПЛ-35 кВ" to EquipmentReliability(0.02, 8, 0.167, 35),
    "ПЛ-10 кВ" to EquipmentReliability(0.02, 10, 0.167, 35),
    "КЛ-10 кВ (траншея)" to EquipmentReliability(0.03, 44, 1.0, 9),
    "КЛ-10 кВ (кабельний канал)" to EquipmentReliability(0.005, 18, 1.0, 9)
)

@Composable
fun CalculatorOne(modifier: Modifier = Modifier) {

    val focusManager = LocalFocusManager.current


    val amountMap = remember {
        mutableMapOf(
            "T-110 kV" to mutableStateOf("1"),
            "T-35 kV" to mutableStateOf("0"),
            "T-10 kV (кабельна мережа 10 кВ)" to mutableStateOf("0"),
            "T-10 kV (повітряна мережа 10 кВ)" to mutableStateOf("0"),
            "B-110 kV (елегазовий)" to mutableStateOf("1"),
            "B-10 kV (малолойний)" to mutableStateOf("1"),
            "B-10 kV (вакуумний)" to mutableStateOf("0"),
            "Збірні шини 10 кВ на 1 приєднання" to mutableStateOf("6"),
            "АВ-0,38 кВ" to mutableStateOf("0"),
            "ЕД 6,10 кВ" to mutableStateOf("0"),
            "ЕД 0,38 кВ" to mutableStateOf("0"),
            "ПЛ-110 кВ" to mutableStateOf("10"),
            "ПЛ-35 кВ" to mutableStateOf("0"),
            "ПЛ-10 кВ" to mutableStateOf("0"),
            "КЛ-10 кВ (траншея)" to mutableStateOf("0"),
            "КЛ-10 кВ (кабельний канал)" to mutableStateOf("0")
        )
    }

    var Woc by remember { mutableStateOf("") }
    var Tvoc by remember { mutableStateOf("") }
    var Kaoc by remember { mutableStateOf("") }
    var Kpoc by remember { mutableStateOf("") }
    var Wdk by remember { mutableStateOf("") }
    var Wds by remember { mutableStateOf("") }

    // Функція для компонентів комірок
    @Composable
    fun InputField(label: String, value: MutableState<String>) {
        OutlinedTextField(
            value = value.value,
            onValueChange = { value.value = it },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        amountMap.forEach { (label, state) ->
            InputField(label = label, value = state)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка обчислення результатів
        Button(
            onClick = {
                var w_0_c = 0.0
                var tvoc = 0.0
                var kaoc = 0.0
                var kpos = 0.0
                var wdk = 0.0
                var wds = 0.0

                amountMap.forEach { (key, value) ->
                    val amount = value.value.toIntOrNull() ?: 0
                    if (amount > 0) {
                        val entry = data[key]
                        if (entry != null) {
                            w_0_c += amount * entry.failureRate
                            tvoc += amount * entry.averageRepairTime * entry.failureRate
                        }
                    }
                }

                if (w_0_c > 0) {
                    tvoc /= w_0_c
                }
                kaoc = (tvoc * w_0_c) / 8760
                kpos = 1.2 * 43 / 8760
                wdk = 2 * w_0_c * (kaoc + kpos)
                wds = wdk + 0.02

                Woc = roundToTwoDecimalString(w_0_c)
                Tvoc = roundToTwoDecimalString(tvoc)
                Kaoc = roundToTwoDecimalString(kaoc)
                Kpoc = roundToTwoDecimalString(kpos)
                Wdk = roundToTwoDecimalString(wdk)
                Wds = roundToTwoDecimalString(wds)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Обчислити")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Результати
        listOf(
            "Частота відмов одноколової системи: " to Woc,
            "Середня тривалість відновлення: " to Tvoc,
            "Коефіцієнт аварійного простою одноколової системи: " to Kaoc,
            "Коефіцієнт планового простою одноколової системи: " to Kpoc,
            "Частота відмов одночасно двох кіл: " to Wdk,
            "Частота відмов двоколової системи з урахуванням секційного вимикача: " to Wds
        ).forEach { (label, value) ->
            Text("$label$value", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun roundToTwoDecimalString(value: Double): String {
    return value.toString()
}