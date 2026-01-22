package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.databinding.BottomSheetLanguageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageBottomSheetFragment(private val onLanguageSelected: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGreek.setOnClickListener {
            onLanguageSelected("el-GR")
            dismiss()
        }

        binding.buttonEnglish.setOnClickListener {
            onLanguageSelected("en-US")
            dismiss()
        }
    }
}
