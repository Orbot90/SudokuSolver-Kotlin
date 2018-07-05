package ru.orbot90.sudoku

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SudokuProcessor {

    var actionPerformedDuringCycle : Boolean = false

    fun solveSudokuFromJsonTextFile(filePath: String) {
        val sudokuArray = this.retrieveSudokuFromFile(filePath)
        this.solveSudoku(Sudoku(sudokuArray, this::onActionPerformed))
    }

    private fun solveSudoku(sudoku: Sudoku) {
        while (!sudoku.solved) {
            this.actionPerformedDuringCycle = false
            for (rowNumber in 0..8) {
                this.processGroup(sudoku.getRow(rowNumber))
            }

            for (columnNumber in 0..8) {
                this.processGroup(sudoku.getColumn(columnNumber))
            }

            for (sectorNumber in 0..8) {
                this.processGroup(sudoku.getSector(sectorNumber))
            }
            if (!actionPerformedDuringCycle) {
                break
            }
        }
    }

    private fun processGroup(group: SudokuGroupOfNine) {
        group.removeResolvedValuesFromPossible()
        group.resolveUniquePossibleValuesInGroup()
    }

    private fun retrieveSudokuFromFile(filePath: String) : Array<IntArray> {
        val jsonText = this.retrieveSudokuJsonFromFile(filePath)
        return this.retrieveSudokuArrayFromJson(jsonText)
    }

    private fun retrieveSudokuJsonFromFile(fileName:String) : String {
        return object {}::class.java.getResource("/$fileName").readText()
    }

    private fun retrieveSudokuArrayFromJson(jsonText: String) : Array<IntArray> {
        return Gson()
                .fromJson<Array<IntArray>>(jsonText,
                        object : TypeToken<Array<IntArray>>() {}.type)
    }

    private fun onActionPerformed() {
        this.actionPerformedDuringCycle = true
    }
}