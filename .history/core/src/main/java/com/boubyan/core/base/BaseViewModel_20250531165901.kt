package com.boubyan.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent> : ViewModel() {
    
    private val _state = MutableStateFlow(getInitialState())
    val state: StateFlow<State> = _state.asStateFlow()
    
    protected abstract fun getInitialState(): State
    
    abstract fun handleIntent(intent: Intent)
    
    protected fun setState(newState: State) {
        _state.value = newState
    }
    
    protected fun updateState(update: (State) -> State) {
        _state.value = update(_state.value)
    }
    
    protected fun launchCoroutine(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}