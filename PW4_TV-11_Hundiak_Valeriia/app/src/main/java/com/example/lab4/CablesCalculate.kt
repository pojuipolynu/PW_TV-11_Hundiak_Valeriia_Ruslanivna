package com.example.lab4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab4.databinding.FragmentSecondBinding
import kotlin.math.sqrt

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultButton.setOnClickListener {
            //Функція перевірки на валідність вводу
            fun isValidNumber(input: String?): Boolean {
                return !input.isNullOrBlank() && input.toDoubleOrNull() != null
            }

            val UInput = binding.inputU.text.toString()
            val KZInput = binding.inputKz.text.toString()
            val TimeInput = binding.inputTime.text.toString()
            val SmInput = binding.inputSm.text.toString()
            val TmInput = binding.inputTm.text.toString()


            if (!isValidNumber(UInput) || !isValidNumber(KZInput) || !isValidNumber(TimeInput) ||
                !isValidNumber(SmInput) || !isValidNumber(TmInput)) {
                binding.result.text = "Введіть змінні у кожну комірку."
            } else {
                val U = UInput.toDouble()
                val KZ = KZInput.toDouble()
                val Time = TimeInput.toDouble()
                val Sm = SmInput.toDouble()
                val Tm = TmInput.toDouble()

                //Розрахунок економічної густини струму
                var j = 0.0
                if (1000 < Tm && Tm < 3000) {
                    j = 1.6
                } else if (3000 < Tm && Tm < 5000) {
                    j = 1.4
                } else if (5000 < Tm) {
                    j = 1.2
                }

                //Розрахунок кабелів
                val bron = (Sm / 2) / (sqrt(3.0) * U) / j
                val abb = ((KZ * 1000) * sqrt(Time) / 92)

                binding.result.text = "Броньований кабель: ${bron}.\nААБ кабель: ${abb}."
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}