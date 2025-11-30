package com.example.astucianaval.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TableroViewModel : ViewModel() {

    private val gridSize = 8
    private val totalCells = gridSize * gridSize

    var dificultad = mutableStateOf(1)

    val jugadorCeldas = mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } }

    val jugadorImpactos = mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } }

    val enemigoCeldas = mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } }

    val barcosJugador = mutableListOf<Int>()
    val barcosEnemigo = mutableListOf<Int>()

    var aciertos = mutableStateOf(0)
    var fallos = mutableStateOf(0)
    var tiempoRestante = mutableStateOf(120)

    val disparosJugador = mutableStateListOf<Boolean>().apply { repeat(64) { add(false) } }
    val disparosJugadorAciertos = mutableStateListOf<Boolean>().apply { repeat(64) { add(false) } }


    private val disparosIA = mutableListOf<Int>()

    private var ultimoAciertoIA: Int? = null
    private var modoCaza = false
    private var direccionesPendientes = mutableListOf<Int>()

    private var hitsSeguidosIA = mutableListOf<Int>()

    fun cargarBarcos(barcos: List<Int>) {
        barcosJugador.clear()
        barcosJugador.addAll(barcos)

        for (i in 0 until totalCells) {
            jugadorCeldas[i] = false
            jugadorImpactos[i] = false
        }
    }

    fun generarBarcosEnemigo() {
        barcosEnemigo.clear()
        for (i in 0 until totalCells) enemigoCeldas[i] = false

        while (barcosEnemigo.size < 8) {
            val pos = (0 until totalCells).random()
            if (!barcosEnemigo.contains(pos)) {
                barcosEnemigo.add(pos)
                enemigoCeldas[pos] = true
            }
        }
    }

    fun iniciarTemporizador(onTimeEnd: () -> Unit) {
        viewModelScope.launch {
            while (tiempoRestante.value > 0) {
                delay(1000)
                tiempoRestante.value--
            }
            onTimeEnd()
        }
    }

    fun disparoJugador(index: Int, onWin: () -> Unit, onLose: () -> Unit) {
        if (index !in 0 until totalCells) return

        if (enemigoCeldas[index]) {
            aciertos.value++
            enemigoCeldas[index] = false
        } else {
            fallos.value++
        }


        if (aciertos.value >= barcosEnemigo.size && barcosEnemigo.isNotEmpty()) {
            onWin()
            return
        }


    }

    fun disparoEnemigo(): Boolean {
        return when (dificultad.value) {
            0 -> iaFacil()
            1 -> iaIntermedia()
            2 -> iaExperta()
            else -> iaFacil()
        }
    }

    private fun iaFacil(): Boolean {
        var tiro: Int
        do {
            tiro = (0 until totalCells).random()
        } while (disparosIA.contains(tiro))

        return procesarDisparoIA(tiro)
    }

    private fun iaIntermedia(): Boolean {

        if (modoCaza && ultimoAciertoIA != null && direccionesPendientes.isNotEmpty()) {

            val base = ultimoAciertoIA!!
            val dir = direccionesPendientes.removeAt(0)

            val objetivo = when (dir) {
                0 -> base - gridSize
                1 -> base + gridSize
                2 -> base - 1
                3 -> base + 1
                else -> null
            }

            if (objetivo != null &&
                objetivo in 0 until totalCells &&
                !disparosIA.contains(objetivo)
            ) {
                return procesarDisparoIA(objetivo)
            }
        }

        return iaFacil()
    }

    private fun iaExperta(): Boolean {

        if (hitsSeguidosIA.isNotEmpty()) {
            val tiro = buscarContinuacionBarco()
            if (tiro != null) {
                return procesarDisparoIA_Experto(tiro)
            }
        }

        val candidatos = (0 until totalCells)
            .filter { !disparosIA.contains(it) }
            .filter { (it % 2) == 0 }

        if (candidatos.isNotEmpty()) {
            return procesarDisparoIA_Experto(candidatos.random())
        }

        return iaFacil()
    }

    private fun procesarDisparoIA(tiro: Int): Boolean {
        disparosIA.add(tiro)

        val acierto = barcosJugador.contains(tiro)

        jugadorCeldas[tiro] = true

        if (acierto) {
            jugadorImpactos[tiro] = true
            ultimoAciertoIA = tiro
            modoCaza = true
            direccionesPendientes = mutableListOf(0, 1, 2, 3)
        } else {
            jugadorImpactos[tiro] = false
        }

        val impactosTotales = jugadorImpactos.count { it }
        if (barcosJugador.isNotEmpty() && impactosTotales >= barcosJugador.size) {
            return true
        }

        return false
    }

    private fun procesarDisparoIA_Experto(tiro: Int): Boolean {
        disparosIA.add(tiro)

        val acierto = barcosJugador.contains(tiro)
        jugadorCeldas[tiro] = true

        if (acierto) {
            jugadorImpactos[tiro] = true
            hitsSeguidosIA.add(tiro)
        } else {
            jugadorImpactos[tiro] = false
        }

        val impactosTotales = jugadorImpactos.count { it }
        if (barcosJugador.isNotEmpty() && impactosTotales >= barcosJugador.size) {
            return true
        }

        return false
    }

    private fun buscarContinuacionBarco(): Int? {
        val base = hitsSeguidosIA.firstOrNull() ?: return null

        val opciones = listOf(
            base - gridSize,
            base + gridSize,
            base - 1,
            base + 1
        )

        return opciones.firstOrNull { it in 0 until totalCells && !disparosIA.contains(it) }
    }
}