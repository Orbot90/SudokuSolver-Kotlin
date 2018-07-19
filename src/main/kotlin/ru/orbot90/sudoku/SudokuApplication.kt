package ru.orbot90.sudoku

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class SudokuApplication : Application() {
    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(this.javaClass.getResource("/fxml/main_window.fxml"))
        val mainDialog: Parent = loader.load()
        primaryStage.title = "Sudoku Solver"
        primaryStage.scene = Scene(mainDialog, 600.0, 700.0)
        val controller = loader.getController<Any>() as SudokuMainWindowController
        controller.primaryStage = primaryStage
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(SudokuApplication::class.java)
}