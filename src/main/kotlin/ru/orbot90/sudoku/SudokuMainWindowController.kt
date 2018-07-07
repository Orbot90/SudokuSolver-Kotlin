package ru.orbot90.sudoku

import com.google.gson.Gson
import javafx.fxml.FXML
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class SudokuMainWindowController {

    @FXML
    private lateinit var sudokuPane: GridPane
    private lateinit var sudokuCellBoxes: Array<Array<VBox>>
    private val sudokuProcessor = SudokuProcessor()

    @FXML
    fun initialize() {
        sudokuCellBoxes = Array(9) {Array(9) {
            val box = VBox()
            box.styleClass.add("sudokucell")
            box.children.add(Text())
            box
        }}
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellBox = sudokuCellBoxes[rowIndex][columnIndex]
                sudokuPane.add(cellBox, columnIndex, rowIndex)
            }
        }
    }

    @FXML
    fun loadFromFile() {
        val sudokuArr = sudokuProcessor.retrieveSudokuFromFile("sudoku.jsn")
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellValue = sudokuArr[rowIndex][columnIndex]
                if (cellValue != 0) {
                    val text = this.sudokuCellBoxes[rowIndex][columnIndex].children[0] as Text
                    text.text = cellValue.toString()
                }
            }
        }
    }

    @FXML
    fun solveSudoku() {
        val valuesArr = Array(9) {
            IntArray(9)
        }
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val text = this.sudokuCellBoxes[rowIndex][columnIndex].children[0] as Text
                val textVal = text.text
                if (textVal.isNotBlank()) {
                    val cellValue = Integer.valueOf(textVal)
                    valuesArr[rowIndex][columnIndex] = cellValue
                }
            }
        }
        val result = sudokuProcessor.solveSudoku(valuesArr)

        val resultArr = Gson().fromJson(result, Array<IntArray>::class.java)
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellValue = resultArr[rowIndex][columnIndex]
                val text = this.sudokuCellBoxes[rowIndex][columnIndex].children[0] as Text
                text.text = cellValue.toString()
            }
        }
    }
}