package com.danielmilano.countries.fragment.countries

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.danielmilano.countries.R
import com.danielmilano.countries.databinding.FragmentCountriesBinding
import com.danielmilano.countries.fragment.countrydetail.CountryDetailFragment.Companion.ARG_COUNTRY
import com.danielmilano.domain.entities.Country
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class CountriesFragment : Fragment(R.layout.fragment_countries), CountryAdapter.OnItemClickListener {

    private var _binding: FragmentCountriesBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel: CountriesViewModel by sharedStateViewModel()

    private var mAdapter = CountryAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountriesBinding.bind(view)
        observeEvents()
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getCountriesEvent.collect {
                when (it) {
                    GetCountriesEvent.LoadCountries -> {
                        viewModel.getCountries()
                    }
                    is GetCountriesEvent.GetCountriesSuccess -> {
                        onSuccess(it.countries)
                    }
                    is GetCountriesEvent.GetFilteredCountries -> {
                        onSuccess(it.countries)
                    }
                    is GetCountriesEvent.GetCountriesError -> {
                        onError()
                    }
                    GetCountriesEvent.Loading -> {
                        loading()
                    }
                }
            }
        }
    }

    private fun loading() {
        mBinding.info.isInvisible = true
        mBinding.retryButton.isInvisible = true
        mBinding.progressBar.isVisible = true
    }

    private fun onError() {
        mBinding.progressBar.isInvisible = true
        mBinding.retryButton.isVisible = true
        mBinding.info.text = getString(R.string.countries_fragment_network_error)
        mBinding.info.isVisible = true
    }

    private fun onSuccess(countries: List<Country>) {
        mBinding.progressBar.isInvisible = true
        if (countries.isEmpty()) {
            mBinding.recycler.isVisible = false
            mBinding.info.text = getString(R.string.countries_fragment_no_results_found)
            mBinding.info.isVisible = true
        } else {
            mBinding.info.isVisible = false
            mBinding.recycler.isVisible = true
            mAdapter.setData(countries)
            mBinding.recycler.scrollToPosition(0)
        }
    }

    private fun setupUI() {
        setHasOptionsMenu(true)
        mBinding.recycler.adapter = mAdapter
        mBinding.retryButton.setOnClickListener { viewModel.getCountries() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_countries, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!isVisible){
                    return true
                }
                viewModel.setQuery(newText)
                viewModel.getFilteredCountries(newText)
                return true
            }
        })

        //Restoring state from viewmodel
        viewModel.query.value?.let {
            searchItem.expandActionView()
            searchView.setQuery(it, false)
            searchView.clearFocus()
        }

        val filterItem = menu.findItem(R.id.action_filter)
        filterItem.setOnMenuItemClickListener {
            findNavController().navigate(R.id.countries_filter_fragment)
            true
        }
    }

    override fun onItemClick(country: Country) {
        findNavController().navigate(R.id.action_countries_fragment_to_country_detail_fragment, bundleOf(ARG_COUNTRY to country))
    }
}