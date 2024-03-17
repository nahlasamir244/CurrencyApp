package com.nahlasamir244.currencyapp.conversion

import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse

//might seem not necessary to have class hold one value but in future might hold other info
data class CurrencyUiModel(
    val symbol: String,
    val isSelected: Boolean = false
) {
    override fun toString(): String = symbol
}

fun CurrenciesSymbolsResponse.mapToCurrencyUiModelList() = this.symbols?.toList()?.map {
    CurrencyUiModel(it.first)
}