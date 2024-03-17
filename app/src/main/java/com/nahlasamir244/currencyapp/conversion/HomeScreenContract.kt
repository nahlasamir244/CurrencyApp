package com.nahlasamir244.currencyapp.conversion

import androidx.lifecycle.LiveData

sealed class HomeScreenContract {

    interface ViewModel {
        val homeState: LiveData<State>
        val homeEvent: LiveData<Event>
        fun invokeAction(action: Action)
    }

    sealed class Action {
        object FetchSymbols : Action() // called also on retry
        data class ConvertCurrency(
            val baseCurrencySymbol: String,
            val baseCurrencyAmount: Double,
            val toCurrencySymbol: String,
            val calculatedFor: Amount
        ) : Action() // called when swap symbols , currency symbol selected , on any amount value changed

        object OnDetailsClicked : Action()
    }

    sealed class State {
        object SymbolsLoading : State()
        object ConversionLoading : State()
        data class CurrencySymbolsSuccess(val currencySymbolList: List<CurrencyUiModel>) :
            State()

        object EmptySymbolsList : State()
        data class CurrencySymbolsError(val message: String) : State() //disable all screen
        data class ConvertCurrencySuccess(val convertedAmount: Double, val calculatedFor: Amount) :
            State()

        object ConvertCurrencyError : State()
    }

    sealed class Event {
        //to display error toast message
        data class Warning(val message: String) : Event()
        object InternetConnectionError : Event()
        object NavigateToHistoryScreen : Event()
    }

    enum class Amount {
        FROM,
        TO,
    }
}