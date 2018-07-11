package ru.orbot90.sudoku

import com.google.gson.Gson
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class SudokuMainWindowController {

    @FXML
    private lateinit var sudokuPane: GridPane
    private lateinit var sudokuCellBoxes: Array<Array<VBox>>
    private val sudokuProcessor = SudokuProcessor()
    private var selectedBox: VBox? = null

    fun initialize() {
        sudokuCellBoxes = Array(9) {Array(9) {
            val box = VBox()
            box.styleClass.add("sudokucell")
            box.children.add(Text())
            box.onMouseClicked = EventHandler { event ->
                val selectedBoxFirstChild = selectedBox?.children?.get(0)
                if (selectedBoxFirstChild is TextField) {
                    selectedBox?.children?.set(0, Text(selectedBoxFirstChild.text))
                }
                val clickedBox = event.source as VBox
                selectedBox = clickedBox
                val cellText: String

                cellText = if (clickedBox.children.size > 0) {
                    val text: Text = clickedBox.children[0] as Text
                    text.text
                } else {
                    ""
                }

                val textInput = TextField(cellText)
                clickedBox.children[0] = textInput
                textInput.styleClass.add("sudokucell")
                textInput.requestFocus()
                textInput.textProperty().addListener { observable: ObservableValue<out String>, oldValue: String, newValue: String ->
                    if (newValue.length > 1) {
                        textInput.text = oldValue
                    }
                }
                textInput.onAction = EventHandler {
                    val cellText = textInput.text
                    if (cellText.isNotBlank()) {
                        clickedBox.children[0] = Text(cellText)
                    } else {
                        clickedBox.children[0] = null
                    }
                }
            }
            box
        }}

        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellBox = sudokuCellBoxes[rowIndex][columnIndex]
                sudokuPane.add(cellBox, columnIndex, rowIndex)
            }
        }
    }

    fun solveSudoku() {
        val valuesArr = Array(9) {
            IntArray(9)
        }
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val text = this.sudokuCellBoxes[rowIndex][columnIndex].children[0]
                val textVal = (if (text is Text) text.text else
                    (if (text is TextField) text.text else ""))
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
                val text = this.sudokuCellBoxes[rowIndex][columnIndex].children[0]
                if (text is Text) {
                    text.text = cellValue.toString()
                } else {
                    val textElement = Text(cellValue.toString())
                    this.sudokuCellBoxes[rowIndex][columnIndex].children[0] = textElement
                }
            }
        }
    }
}