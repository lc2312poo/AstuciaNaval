package com.example.astucianaval.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ColocarBarcosViewModel : ViewModel() {

    val gridSize = 8
    private val totalCells = gridSize * gridSize
    private val maxBarcos = 8

    private val _cells = MutableStateFlow(List(totalCells) { false })
    val cells: StateFlow<List<Boolean>> = _cells

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    fun toggleCell(index: Int) {
        val current = _cells.value.toMutableList()

        val seleccionados = current.count { it }

        if (!current[index] && seleccionados >= maxBarcos) {
            _mensaje.value = "⚠️ No puedes añadir más barcos"
            return
        }

        current[index] = !current[index]

        _cells.value = current
        _mensaje.value = ""
    }

    fun obtenerBarcos(): List<Int> {
        return _cells.value.mapIndexedNotNull { index, isShip ->
            if (isShip) index else null
        }
    }
    fun validarBarcos(): Boolean {
        return obtenerBarcos().size == maxBarcos
    }

    fun setMensaje(value: String) {
        _mensaje.value = value
    }
}
