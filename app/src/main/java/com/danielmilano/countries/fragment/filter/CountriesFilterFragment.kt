package com.danielmilano.countries.fragment.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.danielmilano.countries.R
import com.danielmilano.countries.databinding.FragmentCountriesFilterBinding
import com.danielmilano.countries.fragment.countries.CountriesViewModel
import com.danielmilano.domain.entities.Country
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CountriesFilterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCountriesFilterBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel: CountriesViewModel by sharedStateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countries_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountriesFilterBinding.bind(view)
        mBinding.filterType = viewModel.filterBy
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            close.setOnClickListener { dismiss() }
            confirm.setOnClickListener {
                viewModel.filterBy = mBinding.filterType!!
                dismiss()
            }
            filterByAll.setOnClickListener {
                mBinding.filterType = Country.FilterType.ALL
            }
            filterByContinent.setOnClickListener {
                mBinding.filterType = Country.FilterType.CONTINENT
            }
            filterByLanguage.setOnClickListener {
                mBinding.filterType = Country.FilterType.LANGUAGE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // showing and dismissing dialog multiple times continuously in a short time
        // causes nav controller to doesn't update properly its current destination
        findNavController().popBackStack()
    }
}