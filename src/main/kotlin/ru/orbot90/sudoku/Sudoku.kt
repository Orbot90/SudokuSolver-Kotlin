package ru.orbot90.sudoku

class Sudoku(sudokuArray: Array<IntArray>, private val performedActionSubscriber: () -> Unit) {

    val solved : Boolean
        get() = this.isAllCellsHaveResolvedValues()

    private fun isAllCellsHaveResolvedValues(): Boolean {
        this.sudokuCells.forEach {
            it.forEach {
                if (!it.isCellResolved()) {
                return false
            } }
        }
        return true
    }

    private val sudokuCells : Array<Array<SudokuCell>> = Array(9)
        {
            Array(9)
            { SudokuCell(0, performedActionSubscriber) }
        }

    private val sectorMappings : Array<SectorMapping> = Array(9)
    {
        when (it) {
            0 -> SectorMapping(it, IndexRanges(0..2, 0..2))
            1 -> SectorMapping(it, IndexRanges(0..2, 3..5))
            2 -> SectorMapping(it, IndexRanges(0..2, 6..8))
            3 -> SectorMapping(it, IndexRanges(3..5, 0..2))
            4 -> SectorMapping(it, IndexRanges(3..5, 3..5))
            5 -> SectorMapping(it, IndexRanges(3..5, 6..8))
            6 -> SectorMapping(it, IndexRanges(6..8, 0..2))
            7 -> SectorMapping(it, IndexRanges(6..8, 3..5))
            8 -> SectorMapping(it, IndexRanges(6..8, 6..8))
            else -> {
                throw IllegalArgumentException("Unknown index")
            }
        }
    }

    init {
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellValue = sudokuArray[rowIndex][columnIndex]
                this.sudokuCells[rowIndex][columnIndex].resolveValue(cellValue)
            }
        }
    }

    fun getRow(rowNumber: Int) : SudokuGroupOfNine {
        val groupCells : MutableSet<SudokuCell> = HashSet()
        for (columnIndex in 0..8) {
            groupCells.add(this.sudokuCells[rowNumber][columnIndex])
        }
        return SudokuGroupOfNine(groupCells)
    }

    fun getColumn(columnNumber: Int) : SudokuGroupOfNine {
        val groupCells : MutableSet<SudokuCell> = HashSet()
        for (rowIndex in 0..8) {
            groupCells.add(this.sudokuCells[rowIndex][columnNumber])
        }
        return SudokuGroupOfNine(groupCells)
    }

    fun getSector(sectorNumber: Int) : SudokuGroupOfNine {
        if (sectorNumber < 0 || sectorNumber > 8) {
            throw IllegalArgumentException("Sector number $sectorNumber does not exist")
        }

        val sectorIndexRanges = this.sectorMappings[sectorNumber].indexRanges

        val groupCells : MutableSet<SudokuCell> = HashSet()
        for (rowIndex in sectorIndexRanges.rowRange) {
            for (columnIndex in sectorIndexRanges.columnRange) {
                groupCells.add(this.sudokuCells[rowIndex][columnIndex])
            }
        }
        return SudokuGroupOfNine(groupCells)
    }

    private class SectorMapping(val sectorNumber: Int, val indexRanges: IndexRanges)

    private class IndexRanges(val rowRange: IntRange, val columnRange: IntRange)
}