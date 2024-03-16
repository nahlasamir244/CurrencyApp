package com.nahlasamir244.domain

import app.cash.turbine.test
import com.nahlasamir244.core.datautils.ApiException
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
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
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCurrencySymbolsUseCaseTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var closeableMockito: AutoCloseable? = null

    @Mock
    private lateinit var repo: CurrencyRepo

    private lateinit var usecase: GetCurrencySymbolsUseCase


    @Before
    fun beforeEach() {
        closeableMockito = MockitoAnnotations.openMocks(this)
        usecase =
            spy(GetCurrencySymbolsUseCase(repo))
    }


    @Test
    fun `GetCurrencySymbolsUseCase should return error when request fails`() =
        runTest {
            val error = Result.Error(ApiException(3, "Error message !!"))

            whenever(repo.getCurrenciesSymbols()).doReturn(flowOf(error))

            usecase().test {
                awaitItem() shouldBeEqualTo error
                awaitComplete()
            }

        }


    @Test
    fun `GetCurrencySymbolsUseCase should return symbols list amount when request is successful`() =
        runTest {
            val response = mock<CurrenciesSymbolsResponse>()
            whenever(repo.getCurrenciesSymbols()).doReturn(
                flowOf(
                    Result.Success(
                        response
                    )
                )
            )
            usecase().test {
                awaitItem() shouldBeEqualTo Result.Success(response)
                awaitComplete()
            }
        }


    @Test
    fun `GetCurrencySymbolsUseCase should return loading result request is Loading`() =
        runTest {
            whenever(repo.getCurrenciesSymbols()).doReturn(
                flowOf(
                    Result.Loading
                )
            )
            usecase().test {
                awaitItem() shouldBeEqualTo Result.Loading
                awaitComplete()
            }
        }


    @After
    @Throws(Exception::class)
    fun afterEach() {
        closeableMockito?.close()

    }

}