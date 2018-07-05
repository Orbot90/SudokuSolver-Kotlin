package ru.orbot90.sudoku

import java.util.*

class GuessProcessor(val sudoku: Sudoku) {

    private val guessStack : Stack<GuessFrame> = Stack()

    fun performGuess() {
        val frame = GuessFrame(sudoku)
        this.sudoku.initGuessFrame(frame)
        this.guessStack.push(frame)
    }

    fun restoreSudokuStateFromGuessFrame(){
        val frame = guessStack.pop()
        this.sudoku.restoreCellsStateFromJson(frame.sudokuCellsStateJson)
        this.sudoku.excludeValueInPosition(frame.guessPosition, frame.guessValue)
    }

    class GuessFrame(sudoku : Sudoku) {
        val sudokuCellsStateJson = sudoku.getCellsStateJson()
        lateinit var guessPosition : Pair<Int, Int>
        var guessValue : Int = 0

        fun initializeGuessPosition(guessRow: Int, guessColumn: Int) {
            this.guessPosition = guessRow to guessColumn
        }

        fun initializeGuessValue(guessValue: Int) {
            this.guessValue = guessValue
        }
    }
}