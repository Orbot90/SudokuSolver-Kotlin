package ru.orbot90.sudoku

class SudokuGroupOfNine(private val groupCells: Set<SudokuCell>) {

    init {
        if (groupCells.size != 9) {
            throw IllegalArgumentException("Group cells count is not 9, but ${groupCells.size}")
        }
    }

    fun isGroupValid(): Boolean {
        for (value in 1..9) {
            if (this.isValueRepeatingInGroup(value)) {
                return false
            }
        }
        return true
    }

    private fun isValueRepeatingInGroup(value: Int): Boolean {
        var valueEntryCount = 0
        for (cell in this.groupCells) {
            if (cell.cellValue == value) {
                if (valueEntryCount > 0) {
                    return false
                }
                valueEntryCount++
            }
        }
        return true
    }

    fun removeResolvedValuesFromPossible() {
        for (cell in groupCells) {
            if (cell.isCellResolved()) {
                val value = cell.cellValue
                this.removeValueFromCells(value)
            }
        }
    }

    private fun removeValueFromCells(value: Int) {
        for (cell in groupCells) {
            cell.excludeValue(value)
        }
    }

    fun resolveUniquePossibleValuesInGroup() {
        for (value in 1..9) {
            val uniqueValueCell: SudokuCell? = this.getCellWithUniqueValueIfExists(value)
            uniqueValueCell?.resolveValue(value)
        }
    }

    private fun getCellWithUniqueValueIfExists(value: Int): SudokuCell? {
        var uniqueCell: SudokuCell? = null

        for (cell in this.groupCells) {
            if (uniqueCell == null && cell.hasValueAmongPossible(value)) {
                uniqueCell = cell
            } else if (uniqueCell != null && cell.hasValueAmongPossible(value)) {
                return null
            }
        }
        return null
    }
}