package com.nahlasamir244.data.network.repo.currency

import com.nahlasamir244.core.data.common.CommonRemoteDataSource
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.core.model.response.ExchangeRatesResponse
import com.nahlasamir244.data.network.api.FixerApiService
import javax.inject.Inject

class CurrencyRemoteDataSourceImpl @Inject constructor(
    private val api: FixerApiService
) : CommonRemoteDataSource(), CurrencyRemoteDataSource {
    override suspend fun getCurrenciesSymbols(): CurrenciesSymbolsResponse {
        return makeRequest {
            api.getCurrenciesSymbols()
        }
    }

    override suspend fun getLatestExchangeRates(
        baseCurrency: String?,
        symbols: String?
    ): ExchangeRatesResponse {
        return makeRequest {
            api.getLatestExchangeRates(baseCurrency, symbols)
        }
    }
}