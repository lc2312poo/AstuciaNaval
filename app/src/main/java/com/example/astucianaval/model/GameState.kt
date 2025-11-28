package com.example.astucianaval.model

data class GameState(
    val playerBoard: Board = Board(),
    val enemyBoard: Board = Board(),
    val playerTurn: Boolean = true,
    val hitsPlayer: Int = 0,
    val hitsEnemy: Int = 0,
    val gameOver: Boolean = false
)
