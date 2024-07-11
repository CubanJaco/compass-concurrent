package cu.jaco.compassconcurrent.feature.main.repositories

import cu.jaco.compassconcurrent.feature.main.repositories.local.DataStore
import cu.jaco.compassconcurrent.feature.main.repositories.network.CompassService
import cu.jaco.compassconcurrent.rules.CoroutineMainDispatcherRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AboutRepositoryTest {

    @get:Rule
    val coroutineRule = CoroutineMainDispatcherRule(StandardTestDispatcher())

    private val localDataSource: DataStore = mockk()

    private val networkDataSource: CompassService = mockk()

    private lateinit var systemUnderTest: AboutRepository

    @Before
    fun setUp() {
        systemUnderTest = AboutRepository(
            localDataSource = localDataSource,
            networkDataSource = networkDataSource,
        )
    }

    @Test
    fun `fetch about info`() = runTest(coroutineRule.dispatcher) {
        coEvery { networkDataSource.fetchAboutInfo() } returns "About info"

        val result = systemUnderTest.fetchAboutInfo()

        assertThat(result, `is`("About info"))

        coVerify(exactly = 1) { networkDataSource.fetchAboutInfo() }
    }

    @Test
    fun `get word count`() = runTest(coroutineRule.dispatcher) {
        val flow = flowOf(1)
        every { localDataSource.wordCount } returns flow

        val result = systemUnderTest.getWordCount()

        assertThat(result, `is`(flow))

        coVerify(exactly = 1) { localDataSource.wordCount }
    }

    @Test
    fun `get every 10th character`() = runTest(coroutineRule.dispatcher) {
        val flow = flowOf(persistentListOf('a', 'b', 'c'))
        every { localDataSource.every10thCharacterList } returns flow

        val result = systemUnderTest.getEvery10thCharacter()

        assertThat(result, `is`(flow))

        coVerify(exactly = 1) { localDataSource.every10thCharacterList }
    }

    @Test
    fun `set word count`() = runTest(coroutineRule.dispatcher) {
        coJustRun { localDataSource.setWordCount(10) }

        systemUnderTest.setWordCount(10)

        coVerify(exactly = 1) { localDataSource.setWordCount(10) }
    }

    @Test
    fun `set every 10th character list`() = runTest(coroutineRule.dispatcher) {
        coJustRun { localDataSource.setEvery10thCharacterList(persistentListOf('a', 'b', 'c')) }

        systemUnderTest.setEvery10thCharacterList(persistentListOf('a', 'b', 'c'))

        coVerify(exactly = 1) { localDataSource.setEvery10thCharacterList(persistentListOf('a', 'b', 'c')) }
    }
}