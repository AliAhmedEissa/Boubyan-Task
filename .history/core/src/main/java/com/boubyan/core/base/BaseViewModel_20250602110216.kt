package com.boubyan.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that implements the MVI (Model-View-Intent) pattern.
 * Handles state management and intent processing for all ViewModels in the app.
 *
 * @param State The type of the state that this ViewModel manages
 * @param Intent The type of the intents that this ViewModel can handle
 */
abstract class BaseViewModel<State, Intent> : ViewModel() {
    
    /** Private mutable state flow for internal state management */
    private val _state = MutableStateFlow(getInitialState())
    /** Public immutable state flow for the UI to observe */
    val state: StateFlow<State> = _state.asStateFlow()
    
    /**
     * Abstract method to provide the initial state for this ViewModel.
     * Must be implemented by concrete ViewModels.
     */
    protected abstract fun getInitialState(): State
    
    /**
     * Abstract method to handle intents from the UI.
     * Must be implemented by concrete ViewModels.
     * @param intent The intent to handle
     */
    abstract fun handleIntent(intent: Intent)
    
    /**
     * Updates the state with a new value.
     * @param newState The new state value
     */
    protected fun setState(newState: State) {
        _state.value = newState
    }
    
    /**
     * Updates the state using a transformation function.
     * @param update The function to transform the current state into a new state
     */
    protected fun updateState(update: (State) -> State) {
        _state.value = update(_state.value)
    }
    
    /**
     * Launches a coroutine in the ViewModel's scope.
     * @param block The suspend function to execute
     */
    protected fun launchCoroutine(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}