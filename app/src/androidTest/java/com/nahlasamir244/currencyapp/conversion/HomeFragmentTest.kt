package com.nahlasamir244.currencyapp.conversion

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nahlasamir244.core.presentation.livedata.SingleLiveEvent
import com.nahlasamir244.currencyapp.R
import com.nahlasamir244.currencyapp.util.rules.FragmentTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentRule = FragmentTestRule(HomeFragment::class.java)

    @get:Rule(order = 2)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val states = MediatorLiveData<HomeScreenContract.State>()
    private val events = SingleLiveEvent<HomeScreenContract.Event>()

    private val viewModel = mock<HomeViewModel>()

    private fun injectIntent() = Intent()

    private fun launchActivity() {
        fragmentRule.launchActivity(injectIntent())
    }

    @Before
    fun setup() {
        whenever(viewModel.homeState).doReturn(states)
        whenever(viewModel.homeEvent).doReturn(events)
    }

    @Test
    fun whenLoadingSymbolsState_shouldShowLoading() {
        launchActivity()
        runBlocking(Dispatchers.Main) {
            states.postValue(HomeScreenContract.State.SymbolsLoading)
        }
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_retry)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.ti_from_amount)).check(matches(Matchers.not(isEnabled())))
        onView(withId(R.id.ti_to_amount)).check(matches(Matchers.not(isEnabled())))
    }


    @Test
    fun whenSymbolsErrorState_shouldShowRetryButton() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(HomeScreenContract.State.CurrencySymbolsError("Error"))
        }

        onView(withId(R.id.progress_bar)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.btn_retry)).check(matches(isDisplayed()))
        onView(withId(R.id.ti_from_amount)).check(matches(Matchers.not(isEnabled())))
        onView(withId(R.id.ti_to_amount)).check(matches(Matchers.not(isEnabled())))
    }


    @Test
    fun whenDetailsClicked_shouldTriggerOnDetailsClicked() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(
                HomeScreenContract.State.CurrencySymbolsSuccess(
                    listOf(CurrencyUiModel("EUR"), CurrencyUiModel("USD"))
                )
            )
        }
        onView(
            withId(R.id.btn_details)
        ).perform(
            ViewActions.click()
        )
        verify(viewModel, times(1)).invokeAction(
            HomeScreenContract.Action.OnDetailsClicked
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}