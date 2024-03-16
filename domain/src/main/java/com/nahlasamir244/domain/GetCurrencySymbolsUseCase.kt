package com.nahlasamir244.domain

import com.nahlasamir244.data.network.repo.currency.CurrencyRepo
import javax.inject.Inject

class GetCurrencySymbolsUseCase @Inject constructor(
    private val repository: CurrencyRepo
) {
    operator fun invoke() = repository.getCurrenciesSymbols()
}