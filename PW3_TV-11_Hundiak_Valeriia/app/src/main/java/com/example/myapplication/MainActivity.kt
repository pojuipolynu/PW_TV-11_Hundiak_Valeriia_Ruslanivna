package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val P = findViewById<EditText>(R.id.P)
        val Q1 = findViewById<EditText>(R.id.Q1)
        val Q2 = findViewById<EditText>(R.id.Q2)
        val B = findViewById<EditText>(R.id.B)
        result = findViewById(R.id.resultTextView)

        calculateButton.setOnClickListener {
            val pc = P.text.toString().toDoubleOrNull()
            val q1 = Q1.text.toString().toDoubleOrNull()
            val q2 = Q2.text.toString().toDoubleOrNull()
            val b = B.text.toString().toDoubleOrNull()

            if (pc != null && q1 != null && q2 != null && b != null) {
                calculateResults(pc, b, q1, q2)
            } else {
                result.text = "Введіть валідні значення."
            }
        }
    }

    private fun calculateEnergyShare(Pc: Double, q: Double): Double {
        val delta = Pc * 0.05
        val lowerBound = Pc - delta
        val upperBound = Pc + delta
        val step = 0.001

        var integral = 0.0
        for (p in generateSequence(lowerBound) { it + step }.takeWhile { it < upperBound }) {
            val pd = (1 / (q * sqrt(2 * PI))) * exp(-((p - Pc).pow(2)) / (2 * q.pow(2)))
            integral += pd * step
        }
        return integral
    }

    private fun calculateResults(averagePower: Double, cost: Double, q1: Double, q2: Double) {
        val q1Result = calculateProfitAndPenalty(averagePower, cost, q1)
        val q2Result = calculateProfitAndPenalty(averagePower, cost, q2)

        result.text = """
            Результат для Q1:
            Прибуток від енергії: ${"%.2f".format(q1Result.first)} грн
            Штраф: ${"%.2f".format(q1Result.second)} грн
            Фінальний результат: ${"%.2f".format(q1Result.third)} грн (${if (q1Result.third >= 0) "прибуток" else "збиток"})
            
            Результат для Q2:
            Прибуток від енергії: ${"%.2f".format(q2Result.first)} грн
            Штраф: ${"%.2f".format(q2Result.second)} грн
            Фінальний результат: ${"%.2f".format(q2Result.third)} грн (${if (q2Result.third >= 0) "прибуток" else "збиток"})
        """.trimIndent()
    }

    private fun calculateProfitAndPenalty(Pc: Double, cost: Double, q: Double): Triple<Double, Double, Double> {
        val energyShare = calculateEnergyShare(Pc, q)
        val energyWithoutImbalance = round(Pc * 24 * energyShare)
        val profit = energyWithoutImbalance * cost * 1000
        val energyWithImbalance = round(Pc * 24 * (1 - energyShare))
        val penalty = energyWithImbalance * cost * 1000
        return Triple(profit, penalty, profit - penalty)
    }
}
