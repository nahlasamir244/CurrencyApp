package com.nahlasamir244.data.network.repo.currency

import com.nahlasamir244.core.data.common.CommonRepo
import com.nahlasamir244.core.data.di.IODispatcher
import com.nahlasamir244.core.data.sharedpref.ISharedPreferencesRepo
import com.nahlasamir244.core.data.utils.NetworkConnectivityHelper
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.core.model.response.ExchangeRatesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRepoImpl @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val iSharedPreferencesRepo: ISharedPreferencesRepo,
    networkConnectivityHelper: NetworkConnectivityHelper,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher
) : CommonRepo(networkConnectivityHelper, coroutineDispatcher), CurrencyRepo {

    override fun getCurrenciesSymbols(): Flow<Result<CurrenciesSymbolsResponse>> {
        return networkFlow {
            currencyRemoteDataSource.getCurrenciesSymbols()
        }
    }

    override fun getLatestExchangeRates(
        baseCurrency: String?,
        symbols: String?
    ): Flow<Result<ExchangeRatesResponse>> {
        return networkFlow {
            currencyRemoteDataSource.getLatestExchangeRates(
                baseCurrency ?: iSharedPreferencesRepo.baseCurrency, symbols
            )
        }
    }
}