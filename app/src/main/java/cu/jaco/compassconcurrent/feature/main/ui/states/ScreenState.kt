package cu.jaco.compassconcurrent.feature.main.ui.states

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ScreenState(
    val isLoading: Boolean = false,
    val wordCount: Int = 0,
    val every10thCharacter: ImmutableList<Char> = persistentListOf(),
    @StringRes val error: Int? = null,
)