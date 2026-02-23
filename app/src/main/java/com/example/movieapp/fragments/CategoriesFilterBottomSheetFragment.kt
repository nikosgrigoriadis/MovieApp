package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            combine(
                viewModel.availableCategoryNames,
                viewModel.selectedCategories
            ) { categoryNames, selected ->
                categoryNames to (selected ?: categoryNames.toSet())
            }.collect { (categoryNames, selectedSet) ->
                binding.categoriesContainer.removeAllViews()
                categoryNames.forEach { categoryName ->
                    val checkBox = MaterialCheckBox(requireContext()).apply {
                        text = categoryName
                        isChecked = categoryName in selectedSet
                        setOnCheckedChangeListener { buttonView, isChecked ->
                            if (!viewModel.setCategoryChecked(categoryName, isChecked)) {
                                buttonView.isChecked = true
                                Toast.makeText(
                                    requireContext(),
                                    "At least one category must remain selected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
