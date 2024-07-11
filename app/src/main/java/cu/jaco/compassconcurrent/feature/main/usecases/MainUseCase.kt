package cu.jaco.compassconcurrent.feature.main.usecases

import cu.jaco.compassconcurrent.feature.main.repositories.AboutRepository
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val aboutRepository: AboutRepository,
) {
    suspend fun refreshWordCount() {
        val aboutInfo = aboutRepository.fetchAboutInfo()
        val wordCount = aboutInfo.split("\\s+".toRegex()).size
        aboutRepository.setWordCount(wordCount)
    }

    suspend fun refreshEvery10thCharacter() {
        val aboutInfo = aboutRepository.fetchAboutInfo()
        val every10thCharacter = aboutInfo.filterIndexed { index, _ -> index % 10 == 0 }
        aboutRepository.setEvery10thCharacterList(every10thCharacter.toList().toImmutableList())
    }

    fun getWordCount() = aboutRepository.getWordCount()

    fun getEvery10thCharacter() = aboutRepository.getEvery10thCharacter()
}