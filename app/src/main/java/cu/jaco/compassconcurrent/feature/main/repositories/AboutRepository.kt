package cu.jaco.compassconcurrent.feature.main.repositories

import cu.jaco.compassconcurrent.feature.main.repositories.local.DataStore
import cu.jaco.compassconcurrent.feature.main.repositories.network.CompassService
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AboutRepository @Inject constructor(
    private val localDataSource: DataStore,
    private val networkDataSource: CompassService,
) {
    suspend fun fetchAboutInfo(): String = networkDataSource.fetchAboutInfo()

    fun getWordCount(): Flow<Int> = localDataSource.wordCount

    fun getEvery10thCharacter(): Flow<ImmutableList<Char>> = localDataSource.every10thCharacterList

    suspend fun setWordCount(count: Int) = localDataSource.setWordCount(count)

    suspend fun setEvery10thCharacterList(list: ImmutableList<Char>) = localDataSource.setEvery10thCharacterList(list)
}