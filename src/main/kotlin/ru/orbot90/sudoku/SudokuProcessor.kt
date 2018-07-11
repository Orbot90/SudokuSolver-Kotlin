package ru.orbot90.sudoku

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SudokuProcessor {

    var actionPerformedDuringCycle : Boolean = false
    lateinit var guessProcessor: GuessProcessor

    fun solveSudokuFromJsonTextFile(filePath: String) {
        val sudokuArray = this.retrieveSudokuFromFile(filePath)
        val sudoku = Sudoku(sudokuArray, this::onActionPerformed)
        this.solveSudoku(sudoku)
        this.writeSolvedResultToFile(sudoku)
    }

    fun solveSudoku(sudokuValues: Array<IntArray>): String {
        val sudoku = Sudoku(sudokuValues, this::onActionPerformed)
        this.solveSudoku(sudoku)
        return sudoku.getValuesJson()
    }

    private fun writeSolvedResultToFile(sudoku: Sudoku) {
        val result = sudoku.getValuesJson()
        File("sudoku-result.jsn")
                .printWriter()
                .use { out ->
            out.println(result)
        }
    }

    private fun solveSudoku(sudoku: Sudoku) {
        sudoku.subscribeOnCells(this::onActionPerformed)
        this.guessProcessor = GuessProcessor(sudoku)

        while (!sudoku.solved) {
            try {
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
                    this.guessProcessor.performGuess()
                }
            } catch (e: CellGroupValidationException) {
                this.guessProcessor.restoreSudokuStateFromGuessFrame()
            }
        }
    }

    fun validateSudoku(sudokuArray: Array<IntArray>) {
        val sudoku = Sudoku(sudokuArray, this::onActionPerformed)

        sudoku.subscribeOnCells(this::onActionPerformed)
        this.guessProcessor = GuessProcessor(sudoku)
        var validationFinished = false
        var solutionFound = false
        while (!validationFinished) {
            try {
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
                    this.guessProcessor.performGuess()
                }

                if (sudoku.solved) {
                    this.validateAllGroups(sudoku)
                    if (solutionFound) {
                        throw InvalidSudokuException("Sudoku has more than one solution")
                    }
                    solutionFound = true
                    try {
                        this.guessProcessor.restoreSudokuStateFromGuessFrame()
                    } catch (e: NoSudokuStateToRestoreException) {
                        if (!solutionFound) {
                            throw InvalidSudokuException("Sudoku has no solution")
                        } else {
                            validationFinished = true
                        }
                    }
                }
            } catch (e: CellGroupValidationException) {
                try {
                    this.guessProcessor.restoreSudokuStateFromGuessFrame()
                } catch (e: NoSudokuStateToRestoreException) {
                    if (!solutionFound) {
                        throw InvalidSudokuException("Sudoku has no solution")
                    } else {
                        validationFinished = true
                    }
                }
            }
        }
    }

    private fun processGroup(group: SudokuGroupOfNine) {
        group.validateGroup()
        group.removeResolvedValuesFromPossible()
        group.resolveUniquePossibleValuesInGroup()
    }

    private fun validateAllGroups(sudoku: Sudoku) {
        this.actionPerformedDuringCycle = false
        for (rowNumber in 0..8) {
            sudoku.getRow(rowNumber).validateGroup()
        }

        for (columnNumber in 0..8) {
            sudoku.getColumn(columnNumber).validateGroup()
        }

        for (sectorNumber in 0..8) {
            sudoku.getSector(sectorNumber).validateGroup()
        }
    }

    fun retrieveSudokuFromFile(filePath: String) : Array<IntArray> {
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