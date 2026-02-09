package org.example.geoblinker.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel for MVI pattern
 * State - что показывает экран
 * Event - что делает пользователь
 * Effect - одноразовые события (навигация, тосты)
 */
abstract class BaseViewModel<State, Event, Effect> : ViewModel() {
    
    abstract val initialState: State
    
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()
    
    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()
    
    abstract fun handleEvent(event: Event)
    
    protected fun updateState(update: State.() -> State) {
        _state.value = _state.value.update()
    }
    
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
