package cu.jaco.compassconcurrent.feature.main.usecases

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MainUseCase {
    suspend fun refreshWordCount()

    suspend fun refreshEvery10thCharacter()

    fun getWordCount(): Flow<Int>

    fun getEvery10thCharacter(): Flow<ImmutableList<Char>>
}