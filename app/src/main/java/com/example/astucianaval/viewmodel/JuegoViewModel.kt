package com.example.astucianaval.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astucianaval.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {

    var state = GameState()
        private set

    private lateinit var difficulty: Difficulty

    fun setDifficulty(diff: Difficulty) {
        difficulty = diff
    }

    fun placePlayerShips(selectedCells: List<Int>) {
        val ship = Ship(selectedCells)
        state.playerBoard.placeShip(ship)
    }

    fun generateEnemyShips() {
        val cells = (0 until 64).shuffled().take(8)
        val ship = Ship(cells)
        state.enemyBoard.placeShip(ship)
    }

    fun playerShot(index: Int): Boolean {
        val acierto = state.enemyBoard.receiveShot(index)

        state = state.copy(
            hitsPlayer = if (acierto) state.hitsPlayer + 1 else state.hitsPlayer
        )

        if (state.hitsPlayer >= 8) {
            state = state.copy(gameOver = true)
        }

        enemyShoot()

        return acierto
    }

    private fun enemyShoot() {
        viewModelScope.launch {
            delay(800)

            val disponibles = state.playerBoard.cells
                .mapIndexed { i, v -> i to v }
                .filter { it.second == CellState.EMPTY || it.second == CellState.SHIP }
                .map { it.first }

            val target = disponibles.random()

            val acierto = state.playerBoard.receiveShot(target)

            state = state.copy(
                hitsEnemy = if (acierto) state.hitsEnemy + 1 else state.hitsEnemy
            )

            if (state.hitsEnemy >= 8) {
                state = state.copy(gameOver = true)
            }
        }
    }
}
