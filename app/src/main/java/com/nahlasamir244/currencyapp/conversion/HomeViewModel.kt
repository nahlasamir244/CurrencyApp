package com.nahlasamir244.currencyapp.conversion

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahlasamir244.core.datautils.NoInternetException
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.core.presentation.livedata.SingleLiveEvent
import com.nahlasamir244.domain.ConvertCurrencyUseCase
import com.nahlasamir244.domain.GetCurrencySymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getCurrencySymbolsUseCase: GetCurrencySymbolsUseCase
) : ViewModel(), HomeScreenContract.ViewModel {

    override val homeState = MediatorLiveData<HomeScreenContract.State>()
    override val homeEvent = SingleLiveEvent<HomeScreenContract.Event>()

    override fun invokeAction(action: HomeScreenContract.Action) {
        when (action) {
            is HomeScreenContract.Action.ConvertCurrency -> {
                convertCurrency(
                    action.baseCurrencySymbol,
                    action.toCurrencySymbol,
                    action.baseCurrencyAmount,
                    action.calculatedFor
                )
            }

            HomeScreenContract.Action.FetchSymbols -> {
                fetchSymbols()
            }

            HomeScreenContract.Action.OnDetailsClicked -> {
                homeEvent.postValue(HomeScreenContract.Event.NavigateToHistoryScreen)
            }
        }
    }

    private fun fetchSymbols() {
        viewModelScope.launch {
            getCurrencySymbolsUseCase().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        //this message better be localized
                        homeState.postValue(
                            HomeScreenContract.State.CurrencySymbolsError(
                                result.exception.message ?: "cant fetch symbols"
                            )
                        )
                        homeEvent.postValue(
                            HomeScreenContract.Event.Warning(
                                result.exception.message ?: "cant fetch symbols"
                            )
                        )
                    }

                    Result.Loading -> {
                        homeState.postValue(HomeScreenContract.State.Loading)
                    }

                    is Result.Success -> {
                        handleFetchSymbolsSuccess(result)

                    }
                }
            }
        }
    }

    private fun handleFetchSymbolsSuccess(result: Result.Success<CurrenciesSymbolsResponse>) {
        val symbolList = result.data?.mapToCurrencyUiModelList()
        if (symbolList.isNullOrEmpty()) {
            homeState.postValue(HomeScreenContract.State.EmptySymbolsList)
        } else {
            homeState.postValue(
                HomeScreenContract.State.CurrencySymbolsSuccess(
                    symbolList
                )
            )
        }
    }

    private fun convertCurrency(
        baseCurrency: String,
        toCurrency: String,
        amount: Double,
        calculatedFor: HomeScreenContract.Amount
    ) {
        viewModelScope.launch {
            convertCurrencyUseCase(baseCurrency, toCurrency, amount).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        when (result.exception) {
                            is NoInternetException -> {
                                homeEvent.postValue(HomeScreenContract.Event.InternetConnectionError)
                            }

                            else -> {
                                homeEvent.postValue(result.exception.localizedMessage?.let { message ->
                                    HomeScreenContract.Event.Warning(
                                        message
                                    )
                                })
                            }
                        }
                    }

                    Result.Loading -> {
                        homeState.postValue(HomeScreenContract.State.Loading)
                    }

                    is Result.Success -> {
                        homeState.postValue(result.data?.let { conversionResult ->
                            HomeScreenContract.State.ConvertCurrencySuccess(
                                conversionResult, calculatedFor
                            )
                        })
                    }
                }
            }
        }
    }
}