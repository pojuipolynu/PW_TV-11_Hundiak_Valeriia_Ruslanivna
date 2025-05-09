package com.example.lab4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab4.databinding.FragmentFourthBinding
import kotlin.math.sqrt
import kotlin.math.pow

class FourthFragment : Fragment() {

    private var _binding: FragmentFourthBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultButton.setOnClickListener {

            fun isValidNumber(input: String?): Boolean {
                return !input.isNullOrBlank() && input.toDoubleOrNull() != null
            }

            var RhInput = binding.inputRh.text.toString()
            var XhInput = binding.inputXh.text.toString()
            var RmInput = binding.inputRm.text.toString()
            var XmInput = binding.inputXm.text.toString()

            if (!isValidNumber(RhInput) || !isValidNumber(XhInput) || !isValidNumber(RmInput) ||
                !isValidNumber(XmInput)) {
                binding.result.text = "Введіть змінні у кожну комірку."
            } else{
                var Rh = RhInput.toDouble()
                var Xh = XhInput.toDouble()
                var Rm = RmInput.toDouble()
                var Xm = XmInput.toDouble()

                val Xt = (11.1 * 115.0.pow(2)) / (100 * 6.3)

                var Xsh = Xh + Xt
                var Zsh = sqrt(Rh.pow(2) + Xsh.pow(2))

                var Xsh_min = Xm + Xt
                var Zsh_min = sqrt(Rm.pow(2) + Xsh_min.pow(2))

                // Розрахунок струму трифазного/двофазного КЗ в нормальному режимі
                var Ish_3 = (115.0 * 10.0.pow(3)) / (sqrt(3.0) * Zsh)
                var Ish_2 = Ish_3 * (sqrt(3.0) / 2)

                // Розрахунок струму трифазного/двофазного КЗ в мінімальному режимі
                var Ish_3_min = (115.0 * 10.0.pow(3)) / (sqrt(3.0) * Zsh_min)
                var Ish_2_min = Ish_3_min * (sqrt(3.0) / 2)

                val k = (11.0.pow(2)) / (115.0.pow(2))

                Zsh = sqrt((Rh*k).pow(2) + (Xsh*k).pow(2))

                Zsh_min = sqrt((Rm*k).pow(2) + (Xsh_min*k).pow(2))

                // Розрахунок дійсного струму трифазного/двофазного КЗ в нормальному режимі
                val DIsh_3 = (11.0 * 10.0.pow(3)) / (sqrt(3.0) * Zsh)
                val DIsh_2 = Ish_3 * (sqrt(3.0) / 2)

                // Розрахунок дійсного струму трифазного/двофазного КЗ в мінімальному режимі
                val DIsh_3_min = (11.0 * 10.0.pow(3)) / (sqrt(3.0) * Zsh_min)
                val DIsh_2_min = Ish_3_min * (sqrt(3.0) / 2)

                binding.result.text = "Струм трифазного КЗ.\nНормальний режим:${Ish_3}. Мінімальний режим:${Ish_3_min}\n" +
                        "Струм двофазного КЗ.\nНормальний режим:${Ish_2}. Мінімальний режим:${Ish_2_min}\n"
                        "Дійсний струм трифазного КЗ.\nНормальний режим:${DIsh_3}. Мінімальний режим:${DIsh_3_min}\n" +
                        "Дійсний струм двофазного КЗ.\nНормальний режим:${DIsh_2}. Мінімальний режим:${DIsh_2_min}\n" +
                        "Аварійний режим на данній підстанції не передбачений."
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}