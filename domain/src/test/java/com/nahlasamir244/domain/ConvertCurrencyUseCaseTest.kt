package com.nahlasamir244.domain

import app.cash.turbine.test
import com.nahlasamir244.core.datautils.ApiException
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.ExchangeRatesResponse
import com.nahlasamir244.data.network.repo.currency.CurrencyRepo
import com.nahlasamir244.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ConvertCurrencyUseCaseTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var closeableMockito: AutoCloseable? = null

    @Mock
    private lateinit var repo: CurrencyRepo

    private lateinit var usecase: ConvertCurrencyUseCase


    @Before
    fun beforeEach() {
        closeableMockito = MockitoAnnotations.openMocks(this)
        usecase =
            spy(ConvertCurrencyUseCase(repo))
    }


    @Test
    fun `ConvertCurrencyUseCase should return error when request fails`() =
        runTest {
            val error = Result.Error(ApiException(3, "Error message !!"))

            whenever(repo.getLatestExchangeRates(anyString(), anyString())).doReturn(flowOf(error))

            usecase(anyString(), anyString(), anyDouble()).test {
                awaitItem() shouldBeEqualTo error
                awaitComplete()
            }

        }


    @Test
    fun `ConvertCurrencyUseCase should return conversion rate times amount when request is successful`() =
        runTest {
            val rate = 12.5
            val amount = 3.0
            val expected = 37.5
            val response = ExchangeRatesResponse(null, mapOf("EUR" to rate))
            whenever(repo.getLatestExchangeRates(anyString(), anyString())).doReturn(
                flowOf(
                    Result.Success(
                        response
                    )
                )
            )
            usecase("", "", amount).test {
                awaitItem() shouldBeEqualTo Result.Success(expected)
                awaitComplete()
            }
        }


    @After
    @Throws(Exception::class)
    fun afterEach() {
        closeableMockito?.close()

    }

}