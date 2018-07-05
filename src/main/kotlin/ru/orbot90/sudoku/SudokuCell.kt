package ru.orbot90.sudoku

class SudokuCell(var cellValue: Int, private val performedActionSubscriber: () -> Unit) {

    private val possibleValuesSet: MutableSet<Int> = HashSet()
    private var cellResolved: Boolean = (cellValue != 0)

    init {
        for (possibleValue in 1..9) {
            this.possibleValuesSet.add(possibleValue)
        }
    }

    fun excludeValue(excludedValue: Int) {
        if (this.cellResolved) {
            return
        }
        this.possibleValuesSet.remove(excludedValue)
        this.checkForOnlyPossibleValue()
        this.performedActionSubscriber()
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
        this.performedActionSubscriber()
    }

    fun isCellResolved(): Boolean {
        return this.cellResolved
    }
}