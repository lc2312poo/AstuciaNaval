package com.example.astucianaval.model

class Board(
    val size: Int = 8
) {
    val cells = MutableList(size * size) { CellState.EMPTY }

    fun placeShip(ship: Ship) {
        ship.cells.forEach { index ->
            cells[index] = CellState.SHIP
        }
    }

    fun receiveShot(index: Int): Boolean {
        return if (cells[index] == CellState.SHIP) {
            cells[index] = CellState.HIT
            true
        } else {
            cells[index] = CellState.MISS
            false
        }
    }
}
