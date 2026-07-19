package com.example.hydroflow.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

// Représente l'état de l'interface utilisateur de suivi d'hydratation
data class HydrationUiState(
    val currentIntakeMl: Int = 0,
    val goalMl: Int = 2000,
    val logs: List<WaterLog> = emptyList(),
    val streakDays: Int = 3 // Exemple de streak initialisé
) {
    val isGoalReached: Boolean get() = currentIntakeMl >= goalMl
    val progressPercentage: Float get() = (currentIntakeMl.toFloat() / goalMl.toFloat()).coerceAtMost(1f)
}

// Représente une entrée de consommation d'eau
data class WaterLog(
    val id: String,
    val amountMl: Int,
    val timestamp: Date = Date()
)

class HydrationViewModel : ViewModel() {

    // Utilisation d'un StateFlow pour une gestion d'état réactive conforme à Material 3
    private val _uiState = MutableStateFlow(HydrationUiState())
    val uiState: StateFlow<HydrationUiState> = _uiState.asStateFlow()

    // Ajouter une quantité d'eau (par exemple 250 ml)
    fun addWater(amountMl: Int) {
        _uiState.update { currentState ->
            val newAmount = currentState.currentIntakeMl + amountMl
            val newLog = WaterLog(
                id = System.currentTimeMillis().toString(),
                amountMl = amountMl
            )
            currentState.copy(
                currentIntakeMl = newAmount,
                logs = listOf(newLog) + currentState.logs
            )
        }
    }

    // Réinitialiser la consommation journalière
    fun resetIntake() {
        _uiState.update { currentState ->
            currentState.copy(
                currentIntakeMl = 0,
                logs = emptyList()
            )
        }
    }

    // Supprimer une entrée spécifique de l'historique
    fun deleteLog(logId: String) {
        _uiState.update { currentState ->
            val logToRemove = currentState.logs.find { it.id == logId }
            val removedAmount = logToRemove?.amountMl ?: 0
            currentState.copy(
                currentIntakeMl = (currentState.currentIntakeMl - removedAmount).coerceAtLeast(0),
                logs = currentState.logs.filter { it.id != logId }
            )
        }
    }
}