package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.activities.MainActivity
import com.example.movieapp.databinding.BottomSheetLanguageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageBottomSheetFragment : BottomSheetDialogFragment() {

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
            (activity as? MainActivity)?.setLocale("el")
            dismiss()
        }

        binding.buttonEnglish.setOnClickListener {
            (activity as? MainActivity)?.setLocale("en")
            dismiss()
        }
    }
}