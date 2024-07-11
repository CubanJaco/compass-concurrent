package cu.jaco.compassconcurrent.feature.main.usecases

import cu.jaco.compassconcurrent.feature.main.repositories.AboutRepository
import cu.jaco.compassconcurrent.rules.CoroutineMainDispatcherRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class MainUseCaseWordCount(
    private val aboutInfo: String,
    private val wordsCount: Int,
) {
    @get:Rule
    val coroutineRule = CoroutineMainDispatcherRule(StandardTestDispatcher())

    private val aboutRepository: AboutRepository = mockk()

    private lateinit var systemUnderTest: MainUseCaseImpl

    @Before
    fun setUp() {
        systemUnderTest = MainUseCaseImpl(aboutRepository)
    }

    @Test
    fun `word count`() = runTest(coroutineRule.dispatcher) {
        coJustRun { aboutRepository.setWordCount(any()) }
        coEvery { aboutRepository.fetchAboutInfo() } returns aboutInfo

        systemUnderTest.refreshWordCount()

        coVerify(exactly = 1) { aboutRepository.setWordCount(wordsCount) }
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf("Hello World", 2),
            arrayOf("<p> Compass Hello World </p>", 5),
            arrayOf("<p> Compass Hello World\nWith line break </p>", 8),
            arrayOf("<p> Compass Hello World\tWith tabs </p>", 7),
            arrayOf("<p>\nCompass Hello World\n\tWith line breaks and tabs </p>", 10),
            arrayOf("<p> Compass Hello World   With continuous spaces </p>", 8),
        )
    }
}
