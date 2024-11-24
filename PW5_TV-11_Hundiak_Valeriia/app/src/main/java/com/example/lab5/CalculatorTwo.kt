package com.example.lab5

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier


@Composable
fun CalculatorTwo(modifier: Modifier = Modifier) {

    val focusManager = LocalFocusManager.current

    // Функція для компонентів комірок
    @Composable
    fun InputField(value: String, onValueChange: (String) -> Unit, label: String) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { focusManager.clearFocus() }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }


    val stateMap = remember {
        mutableStateMapOf(
            "omega" to mutableStateOf("0.01"),
            "tS" to mutableStateOf("0.045"),
            "pM" to mutableStateOf("5120"),
            "tM" to mutableStateOf("6451"),
            "zAvar" to mutableStateOf("23.6"),
            "zPlan" to mutableStateOf("17.6"),
            "kP" to mutableStateOf("0.004")
        )
    }

    val results = remember {
        mutableStateMapOf(
            "mWnedA" to mutableStateOf(""),
            "mWnedP" to mutableStateOf(""),
            "mZ" to mutableStateOf("")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        val labels = listOf(
            "omega" to "Частота відмов",
            "tS" to "Середній час відновлення трансформатора напругою 35 кВ",
            "pM" to "Потужність",
            "tM" to "Очікуваний час простою",
            "zAvar" to "Збитки у разі аварійного переривання",
            "zPlan" to "Збитки у разі запланованого переривання",
            "kP" to "Середній час планового простою"
        )

        labels.forEach { (key, label) ->
            InputField(
                value = stateMap[key]?.value ?: "",
                onValueChange = { stateMap[key]?.value = it },
                label = label
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка обчислення результатів
        Button(
            onClick = {
                val omegaValue = stateMap["omega"]?.value?.toDoubleOrNull() ?: 0.0
                val tSValue = stateMap["tS"]?.value?.toDoubleOrNull() ?: 0.0
                val pMValue = stateMap["pM"]?.value?.toDoubleOrNull() ?: 0.0
                val tMValue = stateMap["tM"]?.value?.toDoubleOrNull() ?: 0.0
                val zAvarValue = stateMap["zAvar"]?.value?.toDoubleOrNull() ?: 0.0
                val zPlanValue = stateMap["zPlan"]?.value?.toDoubleOrNull() ?: 0.0
                val kPValue = stateMap["kP"]?.value?.toDoubleOrNull() ?: 0.0

                val mWnedAValue = omegaValue * tSValue * pMValue * tMValue
                val mWnedPValue = kPValue * pMValue * tMValue
                val mZValue = zAvarValue * mWnedAValue + zPlanValue * mWnedPValue

                results["mWnedA"]?.value = mWnedAValue.toString()
                results["mWnedP"]?.value = mWnedPValue.toString()
                results["mZ"]?.value = mZValue.toString()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Обчислити")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Результати
        val resultLabels = listOf(
            "mWnedA" to "Очікувана відсутність енергопостачання в надзвичайних ситуаціях",
            "mWnedP" to "Очікуваний дефіцит енергії для запланованих",
            "mZ" to "Загальна очікувана вартість перерв у роботі"
        )

        resultLabels.forEach { (key, label) ->
            Text(
                text = "$label: ${results[key]?.value}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
