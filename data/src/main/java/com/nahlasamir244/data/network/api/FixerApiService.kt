package com.nahlasamir244.data.network.api

import com.nahlasamir244.core.model.response.ConversionResponse
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.core.model.response.ExchangeRatesResponse
import com.nahlasamir244.core.model.response.HistoricalRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerApiService {
    //open
    @GET("latest")
    suspend fun getLatestExchangeRates(
        @Query("base") baseCurrency: String?,
        @Query("symbols") symbols: String?
    ): Response<ExchangeRatesResponse>

    //open
    @GET("symbols")
    suspend fun getCurrenciesSymbols(): Response<CurrenciesSymbolsResponse>

    //premium
    @GET("convert")
    suspend fun convertCurrency(
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String,
        @Query("amount") amount: Double
    ): Response<ConversionResponse>

    //open
    @GET("{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @Query("base") baseCurrency: String?,
        @Query("symbols") symbols: String?
    ): Response<HistoricalRatesResponse>

}