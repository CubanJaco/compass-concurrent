package cu.jaco.compassconcurrent.feature.main.usecases

import cu.jaco.compassconcurrent.feature.main.repositories.AboutRepository
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(
    private val aboutRepository: AboutRepository,
) : MainUseCase {
    override suspend fun refreshWordCount() {
        val aboutInfo = aboutRepository.fetchAboutInfo()
        val wordCount = aboutInfo.split("\\s+".toRegex()).size
        aboutRepository.setWordCount(wordCount)
    }

    override suspend fun refreshEvery10thCharacter() {
        val aboutInfo = aboutRepository.fetchAboutInfo()
        val every10thCharacter = aboutInfo.filterIndexed { index, _ -> (index + 1)  % 10 == 0 }
        aboutRepository.setEvery10thCharacterList(every10thCharacter.toList().toImmutableList())
    }

    override fun getWordCount() = aboutRepository.getWordCount()

    override fun getEvery10thCharacter() = aboutRepository.getEvery10thCharacter()
}