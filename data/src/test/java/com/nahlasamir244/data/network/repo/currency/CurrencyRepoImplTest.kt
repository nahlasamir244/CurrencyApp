package com.nahlasamir244.data.network.repo.currency

import app.cash.turbine.test
import com.nahlasamir244.core.data.sharedpref.ISharedPreferencesRepo
import com.nahlasamir244.core.data.utils.NetworkConnectivityHelper
import com.nahlasamir244.core.datautils.ApiException
import com.nahlasamir244.core.datautils.Result
import com.nahlasamir244.core.model.response.CurrenciesSymbolsResponse
import com.nahlasamir244.data.utils.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrencyRepoImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var remoteDataSource: CurrencyRemoteDataSource

    @Mock
    private lateinit var networkConnectivityHelper: NetworkConnectivityHelper

    @Mock
    private lateinit var iSharedPreferencesRepo: ISharedPreferencesRepo

    private lateinit var currencyRepo: CurrencyRepoImpl

    private var closeable: AutoCloseable? = null


    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)

        currencyRepo = Mockito.spy(
            CurrencyRepoImpl(
                remoteDataSource,
                iSharedPreferencesRepo,
                networkConnectivityHelper,
                Dispatchers.Main
            )
        )
    }

    @Test
    fun `getCurrenciesSymbols should call remoteDataSource to get currencies symbols when Internet is Connected`() =
        runTest {
            val response: CurrenciesSymbolsResponse = mock()
            whenever(networkConnectivityHelper.isConnected()).doReturn(true)

            whenever(remoteDataSource.getCurrenciesSymbols()).thenReturn(response)

            currencyRepo.getCurrenciesSymbols().collect {

            }
            verify(remoteDataSource, times(1)).getCurrenciesSymbols()
        }

    @Test
    fun `when getCurrenciesSymbols triggered should return response with loading then success`() =
        runTest {

            val response: CurrenciesSymbolsResponse = mock()

            whenever(networkConnectivityHelper.isConnected()).doReturn(true)

            `when`(remoteDataSource.getCurrenciesSymbols()).thenReturn(response)

            currencyRepo.getCurrenciesSymbols().test {
                awaitItem() shouldBeInstanceOf Result.Loading::class.java
                val successResult = awaitItem()
                successResult shouldBeInstanceOf Result.Success::class.java
                (successResult as? Result.Success)?.data shouldBe response
                awaitComplete()
            }
        }

    @Test
    fun testGetCurrenciesSymbolsShouldReturnErrorIfErrorOccurred() = runTest {
        whenever(networkConnectivityHelper.isConnected()).doReturn(true)

        `when`(remoteDataSource.getCurrenciesSymbols()).thenThrow(
            ApiException(3, "error message !")
        )

        currencyRepo.getCurrenciesSymbols().test {
            awaitItem() shouldBeInstanceOf Result.Loading::class.java
            awaitItem() shouldBeInstanceOf Result.Error::class.java
            awaitComplete()
        }
    }

    @After
    @Throws(Exception::class)
    fun afterEach() {
        closeable?.close()

    }
}