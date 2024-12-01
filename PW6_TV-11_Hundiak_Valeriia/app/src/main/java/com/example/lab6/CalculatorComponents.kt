package com.example.lab6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

//Функція створення комірок введення
@Composable
fun Inputs(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label)

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
fun CalculatorApp() {
    var efficiency by remember { mutableStateOf("0.92") }
    var powerFactor by remember { mutableStateOf("0.9") }
    var voltage by remember { mutableStateOf("0.38") }
    var quantity by remember { mutableStateOf("4") }
    var pH by remember { mutableStateOf("20") }
    var kB by remember { mutableStateOf("0.21") }
    var tg by remember { mutableStateOf("1.55") }
    var result by remember { mutableStateOf("") }
    var isCalculationError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Калькулятор", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        CalculatorContent(
            efficiency, powerFactor, voltage, quantity, pH,
            kB, tg, result, isCalculationError,
            { efficiency = it }, { powerFactor = it }, { voltage = it }, { quantity = it },
            { pH = it }, { kB = it }, { tg = it },
            { result = it }, { isCalculationError = it }
        )
    }
}

@Composable
fun CalculatorContent(
    efficiency: String, powerFactor: String, voltage: String, quantity: String, pH: String,
    kB: String, tg: String, result: String, isCalculationError: Boolean,
    onEfficiencyChange: (String) -> Unit, onPowerFactorChange: (String) -> Unit, onVoltageChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit, onpHChange: (String) -> Unit, onkBChange: (String) -> Unit,
    ontgChange: (String) -> Unit, onResultChange: (String) -> Unit, onErrorChange: (Boolean) -> Unit
) {
    Inputs("Коефіцієнт корисної дії", efficiency, onEfficiencyChange, keyboardType = KeyboardType.Decimal)
    Inputs("Коефіцієнт потужності навантаження", powerFactor, onPowerFactorChange, keyboardType = KeyboardType.Decimal)
    Inputs("Напруга навантаження", voltage, onVoltageChange, keyboardType = KeyboardType.Decimal)
    Inputs("Кількість ЕП", quantity, onQuantityChange, keyboardType = KeyboardType.Number)
    Inputs("Номінальна потужність ЕП", pH, onpHChange, keyboardType = KeyboardType.Decimal)
    Inputs("Коефіцієнт використання", kB, onkBChange, keyboardType = KeyboardType.Decimal)
    Inputs("Коефіцієнт реактивної потужності", tg, ontgChange, keyboardType = KeyboardType.Decimal)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            try {
                val eff = efficiency.toDoubleOrNull() ?: throw NumberFormatException()
                val pf = powerFactor.toDoubleOrNull() ?: throw NumberFormatException()
                val volt = voltage.toDoubleOrNull() ?: throw NumberFormatException()
                val qty = quantity.toDoubleOrNull() ?: throw NumberFormatException()
                val power = pH.toDoubleOrNull() ?: throw NumberFormatException()
                val usage = kB.toDoubleOrNull() ?: throw NumberFormatException()
                val reactive_k = tg.toDoubleOrNull() ?: throw NumberFormatException()

                //Проведення обчислень
                val totalpH = qty * power
                val current = (qty * power) / (sqrt(3.0) * volt * pf * eff)
                val groupUsage = usage * totalpH / totalpH
                val effectiveQty = (totalpH * totalpH) / totalpH
                val reactivePower = totalpH * usage * reactive_k
                val activePower = totalpH * usage
                val totalPower = sqrt(activePower * activePower + reactivePower * reactivePower)

                onResultChange("""
                    Розрахунковий струм: ${String.format("%.2f", current)} А
                    Груповий коефіцієнт використання: ${String.format("%.2f", groupUsage)}
                    Ефективна кількість ЕП: ${String.format("%.2f", effectiveQty)}
                    Активне навантаження: ${String.format("%.2f", activePower)} кВт
                    Реактивне навантаження: ${String.format("%.2f", reactivePower)} квар
                    Повна потужність: ${String.format("%.2f", totalPower)} кВА
                """.trimIndent())

                onErrorChange(false)
            } catch (e: Exception) {
                onResultChange("Помилкові значення.")
                onErrorChange(true)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text("Розрахувати")
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (result.isNotEmpty()) {
        Text(
            text = result,
            color = if (isCalculationError) Color.Red else Color.Black,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}
