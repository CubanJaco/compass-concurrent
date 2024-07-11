package cu.jaco.compassconcurrent.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.jaco.compassconcurrent.R
import cu.jaco.compassconcurrent.di.dispatchers.IoDispatcher
import cu.jaco.compassconcurrent.feature.main.ui.states.ScreenState
import cu.jaco.compassconcurrent.feature.main.usecases.MainUseCase
import cu.jaco.compassconcurrent.utils.safeRunCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    init {
        mainUseCase.getWordCount().onEach { wordCount ->
            _screenState.update { it.copy(wordCount = wordCount) }
        }.launchIn(viewModelScope)
        mainUseCase.getEvery10thCharacter().onEach { every10thCharacter ->
            _screenState.update { it.copy(every10thCharacter = every10thCharacter) }
        }.launchIn(viewModelScope)
    }

    fun onRefreshClick() {
        _screenState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch(dispatcher) {
            safeRunCatching {
                mainUseCase.refreshWordCount()
                _screenState.update { it.copy(isLoading = false) }
            }.onFailure {
                _screenState.update {
                    it.copy(
                        isLoading = false,
                        error = R.string.error_message,
                    )
                }
            }
        }

        viewModelScope.launch(dispatcher) {
            safeRunCatching {
                mainUseCase.refreshEvery10thCharacter()
                _screenState.update { it.copy(isLoading = false) }
            }.onFailure {
                _screenState.update {
                    it.copy(
                        isLoading = false,
                        error = R.string.error_message,
                    )
                }
            }
        }
    }
}