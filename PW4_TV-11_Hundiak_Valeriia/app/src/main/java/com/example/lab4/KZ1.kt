package com.example.lab4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab4.databinding.FragmentThirdBinding
import kotlin.math.sqrt
import kotlin.math.pow

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultButton.setOnClickListener {

            //Функція перевірки на валідність вводу
            fun isValidNumber(input: String?): Boolean {
                return !input.isNullOrBlank() && input.toDoubleOrNull() != null
            }

            val KzuInput = binding.inputKzu.text.toString()

            if (!isValidNumber(KzuInput)) {
                binding.result.text = "Введіть змінні у комірку."}
            else{

                val Kzu = KzuInput.toDouble()
                val Uc = 10.5

                //Розрахунок струму трифазного КЗ
                val strum = Uc / (sqrt(3.0) * (Uc.pow(2) / Kzu) + ((Uc / 100) * (Uc.pow(2) / 6.3)))

                binding.result.text = "Струм трифазного КЗ: ${strum}"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}