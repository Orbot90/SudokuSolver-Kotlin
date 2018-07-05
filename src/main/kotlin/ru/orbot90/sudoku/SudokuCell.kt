package ru.orbot90.sudoku

class SudokuCell(var cellValue: Int = 0) {

    private val possibleValuesSet: MutableSet<Int> = HashSet()
    private var cellResolved: Boolean = (cellValue != 0)
    @Transient
    private var performActionSubscriber: (() -> Unit)? = null

    init {
        for (possibleValue in 1..9) {
            this.possibleValuesSet.add(possibleValue)
        }
    }

    fun excludeValue(excludedValue: Int) {
        if (this.cellResolved) {
            return
        }
        val removed = this.possibleValuesSet.remove(excludedValue)
        this.checkForOnlyPossibleValue()
        if (removed) {
            performActionSubscriber?.let { it() }
        }
    }

    private fun checkForOnlyPossibleValue() {
        if (possibleValuesSet.size == 1) {
            this.resolveValue(possibleValuesSet.first())
        }
    }

    fun hasValueAmongPossible(value: Int): Boolean {
        return this.possibleValuesSet.contains(value)
    }

    fun resolveValue(value: Int) {
        if (value == 0) {
            return
        }
        this.cellValue = value
        this.cellResolved = true
        performActionSubscriber?.let { it() }
    }

    fun isCellResolved(): Boolean {
        return this.cellResolved
    }

    fun initializeGuessValue(guessFrame: GuessProcessor.GuessFrame) {
        val guessValue = this.possibleValuesSet.first()
        this.resolveValue(guessValue)
        guessFrame.initializeGuessValue(guessValue)
    }

    fun initSubscriber(subscriber: () -> Unit) {
        this.performActionSubscriber = subscriber
    }
}