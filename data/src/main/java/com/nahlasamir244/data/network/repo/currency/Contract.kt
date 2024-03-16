package com.nahlasamir244.data.network.repo.currency

import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.core.model.response.ExchangeRatesResponse
import kotlinx.coroutines.flow.Flow


interface CurrencyRemoteDataSource {
    suspend fun getCurrenciesSymbols(): CurrenciesSymbolsResponse
    suspend fun getLatestExchangeRates(
        baseCurrency: String?,
        symbols: String?
    ): ExchangeRatesResponse
}

interface CurrencyRepo {
    fun getCurrenciesSymbols(): Flow<Result<CurrenciesSymbolsResponse>>
    fun getLatestExchangeRates(
        baseCurrency: String?,
        symbols: String?
    ): Flow<Result<ExchangeRatesResponse>>
}