package cu.jaco.compassconcurrent.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.jaco.compassconcurrent.di.dispatchers.IoDispatcher
import cu.jaco.compassconcurrent.feature.main.ui.states.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    fun onRefreshClick() {
        viewModelScope.launch(dispatcher) {
            _screenState.update { it.copy(isLoading = true) }
            delay(1000L)
            _screenState.update {
                it.copy(
                    isLoading = false,
                    wordCount = 100,
                    every10thCharacter = persistentListOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j')
                )
            }
        }
    }
}