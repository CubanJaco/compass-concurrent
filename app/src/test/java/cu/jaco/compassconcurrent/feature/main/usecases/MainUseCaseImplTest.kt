package cu.jaco.compassconcurrent.feature.main.usecases

import cu.jaco.compassconcurrent.feature.main.repositories.AboutRepository
import cu.jaco.compassconcurrent.rules.CoroutineMainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainUseCaseImplTest {

    @get:Rule
    val coroutineRule = CoroutineMainDispatcherRule(StandardTestDispatcher())

    private val aboutRepository: AboutRepository = mockk()

    private lateinit var systemUnderTest: MainUseCaseImpl

    @Before
    fun setUp() {
        systemUnderTest = MainUseCaseImpl(aboutRepository)
    }

    @Test
    fun `get word count`() = runTest(coroutineRule.dispatcher) {
        every { aboutRepository.getWordCount() } returns flowOf()

        systemUnderTest.getWordCount()

        verify(exactly = 1) { aboutRepository.getWordCount() }
    }

    @Test
    fun `get every 10th character`() = runTest(coroutineRule.dispatcher) {
        every { aboutRepository.getEvery10thCharacter() } returns flowOf()

        systemUnderTest.getEvery10thCharacter()

        verify(exactly = 1) { aboutRepository.getEvery10thCharacter() }
    }

}