package com.example.mobilecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    //Декларація компонентів інтерфейсу
    private lateinit var coalWeight: EditText
    private lateinit var mazutWeight: EditText
    private lateinit var gasWeight: EditText
    private lateinit var qCoal: EditText
    private lateinit var calculateButton: Button
    private lateinit var buttonChangeInputs: Button
    private lateinit var resultTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ініціалізація комірок вводу данних
        coalWeight = findViewById(R.id.masacoal)
        mazutWeight = findViewById(R.id.masamazut)
        gasWeight = findViewById(R.id.masagas)
        qCoal = findViewById(R.id.zhoraniiacoal)

        // Ініціалізація кнопок та виводу результату
        calculateButton = findViewById(R.id.button)
        buttonChangeInputs = findViewById(R.id.button2)
        resultTextView = findViewById(R.id.result)

        //Ініціалізація самого розрахунку
        calculateButton.setOnClickListener {
            //Перевірка заповненості полів
            if (!areInputsValid()) {
                resultTextView.text = "Усі поля мають бути заповнені"
                resultTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }
            showResult(calculateResults())
        }
        //Ініціалізація кнопки повернення назад(зміни даних)
        buttonChangeInputs.setOnClickListener {
            toggleInputs(show = true)
        }
    }

    //Функція перевірки заповненості полів
    private fun areInputsValid(): Boolean {
        val coalWeightInput = coalWeight.text.toString()
        val mazutWeightInput = mazutWeight.text.toString()
        val gasWeightInput = gasWeight.text.toString()
        val qCoalInput = qCoal.text.toString()

        if (coalWeightInput.isEmpty() || mazutWeightInput.isEmpty() || gasWeightInput.isEmpty() || qCoalInput.isEmpty()) {
            return false
        }

        return try {
            coalWeightInput.toDouble()
            mazutWeightInput.toDouble()
            gasWeightInput.toDouble()
            qCoalInput.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    //Функція розрахунку
    private fun calculateResults(): Map<String, Any> {
            //Отримання початкових даних
            val gasDensity = 0.723
            val coal = coalWeight.text.toString().toDouble()
            val mazut = mazutWeight.text.toString().toDouble()
            val gas = gasWeight.text.toString().toDouble() * gasDensity
            val q = qCoal.text.toString().toDouble()

            //Розрахунок результатів на основі маси вугілля
            val kCoal = BigDecimal((10.0.pow(6).toInt() / q * 0.8 * 25.2 / (100 - 1.5) * (1 - 0.985))).setScale(2, RoundingMode.HALF_UP).toDouble()
            val eCoal = BigDecimal((10.0.pow(-6) * kCoal * q * coal)).setScale(2, RoundingMode.HALF_UP).toDouble()
            //Розрахунок результатів на основі маси мазуту
            val kmazut = BigDecimal((10.0.pow(6).toInt() / 39.48 * 1 * 0.15 / (100 - 0) * (1 - 0.985))).setScale(2, RoundingMode.HALF_UP).toDouble()
            val emazut = BigDecimal((10.0.pow(-6) * kmazut * 39.48 * mazut)).setScale(2, RoundingMode.HALF_UP).toDouble()
            //Розрахунок результатів на основі маси газу
            val kGas = BigDecimal((10.0.pow(6).toInt() / 33.08 * 0 * 0 / (100 - 0) * (1 - 0.985))).setScale(2, RoundingMode.HALF_UP).toDouble()
            val eGas = BigDecimal((10.0.pow(-6) * kGas * 33.08 * gas)).setScale(2, RoundingMode.HALF_UP).toDouble()
            //Повернення результатів
            return mapOf(
                "kCoal" to kCoal,
                "eCoal" to eCoal,
                "kmazut" to kmazut,
                "emazut" to emazut,
                "kGas" to kGas,
                "eGas" to eGas)
    }
    //Функція для виведення результату розрахунку
    private fun showResult(results: Map<String, Any>) {
        resultTextView.text = """
        Для заданого енергоблоку і відповідним умовам роботи:
            1. Показник емісії твердих частинок при спалюванні вугілля становитиме: ${results["kCoal"]} г/ГДж;
            2. Валовий викид при спалюванні вугілля становитиме:  ${results["eCoal"]} т.;
            3. Показник емісії твердих частинок при спалюванні мазуту становитиме:  ${results["kmazut"]} г/ГДж;
            4. Валовий викид при спалюванні мазуту становитиме:  ${results["emazut"]} т.;
            5. Показник емісії твердих частинок при спалюванні природного газу становитиме:  ${results["kGas"]}
            г/ГДж;
            6. Валовий викид при спалюванні природного газу становитиме:  ${results["eGas"]} т..
        """.trimIndent()

        resultTextView.visibility = View.VISIBLE

        toggleInputs(show = false)
    }

    //Функція змінни видимості полів
    private fun toggleInputs(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        coalWeight.visibility = visibility
        mazutWeight.visibility = visibility
        gasWeight.visibility = visibility
        qCoal.visibility = visibility
        calculateButton.visibility = visibility

        buttonChangeInputs.visibility = if (show) View.GONE else View.VISIBLE
        resultTextView.visibility = if (show) View.GONE else View.VISIBLE

    }

}
