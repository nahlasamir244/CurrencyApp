package com.nahlasamir244.currencyapp.conversion

import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import org.junit.Assert
import org.junit.Test

class UiModelMapperTest {
    private val response = CurrenciesSymbolsResponse(
        mapOf(
            "USD" to "US Dollar",
            "EUR" to "Euro",
            "GBP" to "British Pound",
            "JPY" to "Japanese Yen"
        )
    )

    @Test
    fun toCurrencyUiModelList() {
        val actualUiModelList = response.mapToCurrencyUiModelList()
        val expectedUiModelList = listOf(
            CurrencyUiModel("USD"),
            CurrencyUiModel("EUR"),
            CurrencyUiModel("GBP"),
            CurrencyUiModel("JPY")
        )
        Assert.assertEquals(expectedUiModelList, actualUiModelList)
    }
}