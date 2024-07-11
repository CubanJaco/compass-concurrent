package cu.jaco.compassconcurrent.feature.main.fakes

import cu.jaco.compassconcurrent.feature.main.usecases.MainUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeMainUseCase(
) : MainUseCase {
    var delay: Long = 0L
    var throwException: Exception? = null

    val flowOfWordCount: MutableSharedFlow<Int> = MutableSharedFlow()
    val flowOfEvery10thCharacter: MutableSharedFlow<ImmutableList<Char>> = MutableSharedFlow()

    override suspend fun refreshWordCount() {
        delay(delay)
        throwException?.let { throw it }
    }

    override suspend fun refreshEvery10thCharacter() {
        delay(delay)
        throwException?.let { throw it }
    }

    override fun getWordCount(): Flow<Int> = flowOfWordCount

    override fun getEvery10thCharacter(): Flow<ImmutableList<Char>> = flowOfEvery10thCharacter
}