package com.nahlasamir244.currencyapp.conversion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nahlasamir244.currencyapp.R
import com.nahlasamir244.currencyapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.homeState.observe(viewLifecycleOwner, Observer(::renderViewState))
        viewModel.homeEvent.observe(viewLifecycleOwner, Observer(::renderViewEvent))
        initListeners()
        viewModel.invokeAction(HomeScreenContract.Action.FetchSymbols)
    }

    private fun renderViewState(state: HomeScreenContract.State) {
        when (state) {
            is HomeScreenContract.State.ConvertCurrencySuccess -> {
                handleCurrencyConversionSuccess(state)
            }

            is HomeScreenContract.State.CurrencySymbolsError -> {
                handleCurrencySymbolsError()
            }

            is HomeScreenContract.State.CurrencySymbolsSuccess -> {
                handleCurrencySymbolsSuccess(state)
            }

            HomeScreenContract.State.EmptySymbolsList -> {
                //we can handle this in different way
                handleCurrencySymbolsError()
            }

            HomeScreenContract.State.SymbolsLoading -> {
                handleSymbolsLoading()
            }

            HomeScreenContract.State.ConversionLoading -> {
                handleConversionLoading()
            }

            HomeScreenContract.State.ConvertCurrencyError -> {
                binding.apply {
                    btnRetry.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    tiToAmount.isEnabled = true
                    tiFromAmount.isEnabled = true
                }
            }
        }

    }

    private fun handleCurrencySymbolsSuccess(state: HomeScreenContract.State.CurrencySymbolsSuccess) {
        binding.apply {
            setupFromToDropdown(spinnerFrom, state.currencySymbolList)
            setupFromToDropdown(spinnerTo, state.currencySymbolList)
            btnRetry.visibility = View.GONE
            progressBar.visibility = View.GONE
            tiToAmount.isEnabled = true
            tiFromAmount.isEnabled = true
        }
    }

    private fun handleSymbolsLoading() {
        binding.apply {
            btnRetry.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            tiToAmount.isEnabled = false
            tiFromAmount.isEnabled = false
        }
    }

    private fun handleConversionLoading() {
        binding.apply {
            btnRetry.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            tiToAmount.isEnabled = true
            tiFromAmount.isEnabled = true
        }
    }

    private fun handleCurrencySymbolsError() {
        binding.apply {
            btnRetry.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            tiToAmount.isEnabled = false
            tiFromAmount.isEnabled = false
        }
    }

    private fun handleCurrencyConversionSuccess(state: HomeScreenContract.State.ConvertCurrencySuccess) {
        with(binding) {
            when (state.calculatedFor) {
                HomeScreenContract.Amount.FROM -> {
                    tiFromAmount.setText(state.convertedAmount.toString())
                }

                HomeScreenContract.Amount.TO -> {
                    tiToAmount.setText(state.convertedAmount.toString())
                }
            }
            btnRetry.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun renderViewEvent(event: HomeScreenContract.Event) {
        when (event) {
            HomeScreenContract.Event.NavigateToHistoryScreen -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
            }

            is HomeScreenContract.Event.Warning -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            HomeScreenContract.Event.InternetConnectionError -> {
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnSwapSymbols.setOnClickListener {
                swapCurrencies()
            }

            btnRetry.setOnClickListener {
                btnRetry.visibility = View.GONE
                viewModel.invokeAction(HomeScreenContract.Action.FetchSymbols)
            }
            btnDetails.setOnClickListener {
                viewModel.invokeAction(HomeScreenContract.Action.OnDetailsClicked)
            }
            tiFromAmount.doAfterTextChanged {
                convertFromAmount()
            }
            tiToAmount.doAfterTextChanged {
                convertToAmount()
            }
            spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertFromAmount()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
            spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertToAmount()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun convertToAmount() {
        with(binding) {
            if (!tiToAmount.text.isNullOrBlank()) {
                viewModel.invokeAction(
                    HomeScreenContract.Action.ConvertCurrency(
                        spinnerTo.selectedItem.toString(),
                        tiToAmount.text.toString().toDouble(),
                        spinnerFrom.selectedItem.toString(),
                        HomeScreenContract.Amount.FROM
                    )
                )
            }
        }
    }

    private fun convertFromAmount() {
        with(binding) {
            if (!tiFromAmount.text.isNullOrBlank()) {
                viewModel.invokeAction(
                    HomeScreenContract.Action.ConvertCurrency(
                        spinnerFrom.selectedItem.toString(),
                        tiFromAmount.text.toString().toDouble(),
                        spinnerTo.selectedItem.toString(),
                        HomeScreenContract.Amount.TO
                    )
                )
            }
        }
    }

    private fun setupFromToDropdown(
        spinner: Spinner,
        options: List<CurrencyUiModel>
    ) {
        context?.let { context ->
            val arrayAdapter = ArrayAdapter(context, R.layout.from_to_list_item, options)
            arrayAdapter.setDropDownViewResource(R.layout.from_to_list_item)
            spinner.apply {
                adapter = arrayAdapter
                setSelection(0)
            }
        }
    }

    private fun swapCurrencies() {
        val selectedPositionFrom = binding.spinnerFrom.selectedItemPosition
        val selectedPositionTo = binding.spinnerTo.selectedItemPosition
        binding.spinnerFrom.setSelection(selectedPositionTo)
        binding.tiToAmount.setText("")
        binding.spinnerTo.setSelection(selectedPositionFrom)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}