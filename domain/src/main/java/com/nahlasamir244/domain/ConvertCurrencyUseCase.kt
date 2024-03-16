package com.nahlasamir244.domain

import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.data.network.repo.currency.CurrencyRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepo
) {
    operator fun invoke(
        baseCurrency: String,
        toCurrency: String,
        amount: Double
    ): Flow<Result<Double?>> = repository.getLatestExchangeRates(baseCurrency, toCurrency).map {
        when (it) {
            is Result.Success -> {
                val rate = it.data?.rates?.toList()?.firstOrNull()?.second
                val result = if (rate == null) {
                    rate
                } else {
                    amount * rate
                }
                Result.Success(
                    result
                )
            }

            is Result.Error -> it
            is Result.Loading -> it
        }
    }
}