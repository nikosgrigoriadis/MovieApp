package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.databinding.FragmentCategoriesFilterBinding
import com.example.movieapp.viewmodels.MoviesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategoriesFilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCategoriesFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.categories, viewModel.selectedCategories) { categories, selected ->
                categories to (selected ?: categories.map { it.cat }.toSet())
            }.collect { (categories, selectedSet) ->
                binding.categoriesContainer.removeAllViews()
                categories.forEach { category ->
                    val checkBox = MaterialCheckBox(requireContext()).apply {
                        text = category.cat
                        isChecked = category.cat in selectedSet
                        setOnCheckedChangeListener { _, isChecked ->
                            viewModel.setCategoryChecked(category.cat, isChecked)
                        }
                    }
                    binding.categoriesContainer.addView(checkBox)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CategoriesFilterBottomSheet"
    }
}
