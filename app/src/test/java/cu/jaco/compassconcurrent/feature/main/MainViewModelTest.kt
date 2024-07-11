package cu.jaco.compassconcurrent.feature.main

import app.cash.turbine.test
import cu.jaco.compassconcurrent.R
import cu.jaco.compassconcurrent.feature.main.fakes.FakeMainUseCase
import cu.jaco.compassconcurrent.feature.main.ui.states.ScreenState
import cu.jaco.compassconcurrent.rules.CoroutineMainDispatcherRule
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineMainDispatcherRule(StandardTestDispatcher())

    private lateinit var mainUseCase: FakeMainUseCase

    private lateinit var systemUnderTest: MainViewModel

    @Before
    fun setUp() {
        mainUseCase = FakeMainUseCase()

        systemUnderTest = MainViewModel(
            mainUseCase = mainUseCase,
            dispatcher = coroutineRule.dispatcher
        )
    }

    @Test
    fun `screenState should emit word count`() = runTest(coroutineRule.dispatcher) {
        systemUnderTest.screenState.test {
            assertThat(awaitItem(), `is`(ScreenState()))

            mainUseCase.flowOfWordCount.emit(10)

            assertThat(awaitItem(), `is`(ScreenState(wordCount = 10)))
        }
    }

    @Test
    fun `screenState should emit every 10th character list`() = runTest(coroutineRule.dispatcher) {
        systemUnderTest.screenState.test {
            assertThat(awaitItem(), `is`(ScreenState()))

            mainUseCase.flowOfEvery10thCharacter.emit(persistentListOf('a', 'b', 'c'))

            assertThat(awaitItem(), `is`(ScreenState(every10thCharacter = persistentListOf('a', 'b', 'c'))))
        }
    }

    @Test
    fun `onRefreshClick should update the isLoading`() = runTest(coroutineRule.dispatcher) {
        systemUnderTest.screenState.test {
            assertThat(awaitItem(), `is`(ScreenState(isLoading = false)))

            systemUnderTest.onRefreshClick()

            assertThat(awaitItem(), `is`(ScreenState(isLoading = true)))

            coroutineRule.dispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem(), `is`(ScreenState(isLoading = false)))

            expectNoEvents()
        }
    }

    @Test
    fun `onRefreshClick should emit error when error occurs`() = runTest(coroutineRule.dispatcher) {
        mainUseCase.throwException = Exception()

        systemUnderTest.screenState.test {
            assertThat(awaitItem(), `is`(ScreenState(isLoading = false)))

            systemUnderTest.onRefreshClick()

            assertThat(awaitItem(), `is`(ScreenState(isLoading = true)))

            coroutineRule.dispatcher.scheduler.advanceUntilIdle()

            assertThat(
                awaitItem(),
                `is`(
                    ScreenState(
                        isLoading = false,
                        error = R.string.error_message
                    )
                )
            )

            expectNoEvents()
        }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `onRefreshClick should request content in parallel`() = runTest(coroutineRule.dispatcher) {
        mainUseCase.delay = 100L

        systemUnderTest.screenState.test {
            assertThat(awaitItem(), `is`(ScreenState(isLoading = false)))

            systemUnderTest.onRefreshClick()

            assertThat(awaitItem(), `is`(ScreenState(isLoading = true)))

            coroutineRule.dispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem(), `is`(ScreenState(isLoading = false)))
            assertThat(100L, `is`(currentTime))

            expectNoEvents()
        }
    }

}