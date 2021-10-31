package com.danielmilano.countries.fragment.countrydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danielmilano.countries.databinding.FragmentCountryDetailBinding
import com.danielmilano.domain.entities.Country

class CountryDetailFragment : Fragment() {

    companion object {
        const val ARG_COUNTRY = "ARG_COUNTRY"
    }

    private var _binding: FragmentCountryDetailBinding? = null
    private val mBinding get() = _binding!!

    private val country by lazy {
        arguments?.getSerializable(ARG_COUNTRY) as Country
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCountryDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.country = country
    }

}