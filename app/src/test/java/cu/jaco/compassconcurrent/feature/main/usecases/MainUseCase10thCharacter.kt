package cu.jaco.compassconcurrent.feature.main.usecases

import cu.jaco.compassconcurrent.feature.main.repositories.AboutRepository
import cu.jaco.compassconcurrent.rules.CoroutineMainDispatcherRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class MainUseCase10thCharacter(
    private val aboutInfo: String,
    private val characters: ImmutableList<Char>,
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
        coJustRun { aboutRepository.setEvery10thCharacterList(any()) }
        coEvery { aboutRepository.fetchAboutInfo() } returns aboutInfo

        systemUnderTest.refreshEvery10thCharacter()

        coVerify(exactly = 1) { aboutRepository.setEvery10thCharacterList(characters) }
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf("Hello World", persistentListOf('l')),
            arrayOf("<p> Compass Hello World </p>", persistentListOf('s', 'o')),
            arrayOf("<p> Compass Hello World\nWith line break </p>", persistentListOf('s', 'o', 'l', ' ')),
            arrayOf("<p> Compass Hello World\tWith tabs </p>", persistentListOf('s', 'o', 't')),
            arrayOf("<p>\nCompass Hello World\n\tWith line breaks and tabs </p>", persistentListOf('s', 'o', ' ', 'k', 's')),
            arrayOf("<p> Compass Hello World   With continuous spaces </p>", persistentListOf('s', 'o', 'h', 'u', '<')),
        )
    }
}
