package com.nahlasamir244.currencyapp.conversion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nahlasamir244.core.datautils.ApiException
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.currencyapp.utils.MainCoroutineRule
import com.nahlasamir244.currencyapp.utils.getOrAwaitValue
import com.nahlasamir244.domain.ConvertCurrencyUseCase
import com.nahlasamir244.domain.GetCurrencySymbolsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel

    private val convertCurrencyUseCase = mock<ConvertCurrencyUseCase>()
    private val getCurrencySymbolsUseCase = mock<GetCurrencySymbolsUseCase>()

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            convertCurrencyUseCase, getCurrencySymbolsUseCase
        )
        viewModel.homeEvent.observeForever {}
    }

    @Test
    fun `when FetchSymbols should invoke getCurrencySymbolsUseCase`() {
        coroutineRule.run {
            viewModel.invokeAction(HomeScreenContract.Action.FetchSymbols)
            TestCoroutineScope().launch {
                verify(getCurrencySymbolsUseCase).invoke()
            }
        }
    }

    @Test
    fun `when Details clicked should post NavigateToHistoryScreen event`() {
        coroutineRule.run {
            viewModel.invokeAction(
                HomeScreenContract.Action.OnDetailsClicked
            )
            val expected = HomeScreenContract.Event.NavigateToHistoryScreen
            assertEquals(expected, viewModel.homeEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `when convert currency action invoked should invoke convertCurrencyUseCase`() {
        coroutineRule.run {
            viewModel.invokeAction(
                HomeScreenContract.Action.ConvertCurrency(
                    "EUR",
                    5.5,
                    "AED",
                    HomeScreenContract.Amount.FROM
                )
            )
            TestCoroutineScope().launch {
                verify(convertCurrencyUseCase).invoke("EUR", "AED", 5.5)
            }
        }
    }

    @Test
    fun `when convertCurrencyUseCase return Result of Error should update state to ConvertCurrencyError`() =
        coroutineRule.run {
            viewModel.homeState.postValue(null)
            whenever(convertCurrencyUseCase(anyString(), anyString(), anyDouble())).doReturn(flow {
                emit(
                    Result.Error(
                        ApiException(3, "message")
                    )
                )
            })
            viewModel.invokeAction(
                HomeScreenContract.Action.ConvertCurrency(
                    "EUR",
                    5.5,
                    "AED",
                    HomeScreenContract.Amount.FROM
                )
            )
            assertTrue(viewModel.homeState.getOrAwaitValue() is HomeScreenContract.State.ConvertCurrencyError)
        }
}
