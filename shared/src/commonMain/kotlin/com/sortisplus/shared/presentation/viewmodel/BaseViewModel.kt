package com.sortisplus.shared.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Base ViewModel for shared presentation logic
 * Provides a coroutine scope that can be used across platforms
 */
abstract class BaseViewModel {
    
    /**
     * Coroutine scope for the ViewModel
     * Uses SupervisorJob to ensure that failure in one coroutine doesn't cancel others
     * Uses Dispatchers.Default for compatibility across platforms (Android has Main, Desktop doesn't)
     */
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    /**
     * Called when the ViewModel should be cleared
     * Cancels all running coroutines
     */
    open fun onCleared() {
        viewModelScope.cancel()
    }
}