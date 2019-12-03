package zh.games.minesweeper.board

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import zh.games.minesweeper.board.Direction.DOWN
import zh.games.minesweeper.board.Direction.LEFT
import zh.games.minesweeper.board.Direction.RIGHT
import zh.games.minesweeper.board.Direction.UP

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestRectangularBoard {
    @Test
    fun test00AllCells_0() {
        val board = createSquareBoard(2)
        val cells = board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
        assertEquals("[(1, 1), (1, 2), (2, 1), (2, 2)]", cells.toString())
    }

    @Test
    fun test00AllCells_1() {
        val board = createRectangularBoard(3, 2)
        val cells = board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
        assertEquals("[(1, 1), (1, 2), (2, 1), (2, 2), (3, 1), (3, 2)]", cells.toString())
    }

    @Test
    fun test00AllCells_2() {
        val board = createRectangularBoard(2, 3)
        val cells = board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
        assertEquals("[(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (2, 3)]", cells.toString())
    }

    @Test
    fun test01Cell_0() {
        val board = createSquareBoard(2)
        val cell = board.getCellOrNull(1, 2)
        assertEquals(1, cell?.i)
        assertEquals(2, cell?.j)
    }

    @Test
    fun test01Cell_1() {
        val board = createRectangularBoard(3, 2)
        val cell = board.getCellOrNull(3, 1)
        assertEquals(3, cell?.i)
        assertEquals(1, cell?.j)
    }

    @Test
    fun test01Cell_2() {
        val board = createRectangularBoard(2, 3)
        val cell = board.getCellOrNull(2, 3)
        assertEquals(2, cell?.i)
        assertEquals(3, cell?.j)
    }

    @Test
    fun test02NoCell() {
        val board = createRectangularBoard(2, 3)
        val cell = board.getCellOrNull(3, 4)
        assertEquals(null, cell)
    }

    @Test
    fun test03Row_1() {
        val board = createRectangularBoard(2, 3)
        val row = board.getRow(1, 1..2)
        assertEquals("[(1, 1), (1, 2)]", row.toString())
    }

    @Test
    fun test03Row_2() {
        val board = createRectangularBoard(2, 3)
        val row = board.getRow(1, 1..3)
        assertEquals("[(1, 1), (1, 2), (1, 3)]", row.toString())
    }

    @Test
    fun test04RowReversed() {
        val board = createRectangularBoard(3, 2)
        val row = board.getRow(1, 2 downTo 1)
        assertEquals("[(1, 2), (1, 1)]", row.toString())
    }

    @Test
    fun test05RowWrongRange() {
        val board = createRectangularBoard(3, 2)
        val row = board.getRow(1, 1..10)
        assertEquals("[(1, 1), (1, 2)]", row.toString())
    }

    @Test
    fun test06Neighbour_0() {
        val board = createRectangularBoard(2, 3)
        with(board) {
            val cell = getCellOrNull(1, 1)
            assertNotNull(cell)
            assertEquals(null, cell!!.getNeighbour(UP))
            assertEquals(null, cell.getNeighbour(LEFT))
            assertEquals("(2, 1)", cell.getNeighbour(DOWN).toString())
            assertEquals("(1, 2)", cell.getNeighbour(RIGHT).toString())
        }
    }

    @Test
    fun test06Neighbour_1() {
        val board = createRectangularBoard(2, 3)
        with(board) {
            val cell = getCellOrNull(2, 3)
            assertNotNull(cell)
            assertEquals("(1, 3)", cell!!.getNeighbour(UP).toString())
            assertEquals("(2, 2)", cell.getNeighbour(LEFT).toString())
            assertEquals(null, cell.getNeighbour(DOWN))
            assertEquals(null, cell.getNeighbour(RIGHT))
        }
    }

    @Test
    fun test07AllCells() {
        val board = createRectangularBoard(4, 3)
        val cells = board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
        assertEquals("Wrong result for 'getAllCells()' for the board of width 3.",
                "[(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (2, 3), (3, 1), (3, 2), (3, 3), (4, 1), (4, 2), (4, 3)]",
                cells.toString())
    }

    @Test
    fun test08Cell() {
        val board = createSquareBoard(4)
        val cell = board.getCellOrNull(2, 3)
        assertEquals("The board of width 4 should contain the cell (2, 3).",
                "(2, 3)", cell.toString())
    }

    @Test
    fun test09NoCell() {
        val board = createRectangularBoard(4, 5)
        val cell = board.getCellOrNull(10, 10)
        assertEquals("The board of width 4 should contain the cell (10, 10).", null, cell)
    }

    @Test
    fun test10Row() {
        val row = createSquareBoard(4).getRow(1, 1..2)
        assertEquals("Wrong row for 'createSquareBoard(4).getRow(1, 1..2)'.",
                "[(1, 1), (1, 2)]", row.toString())
    }

    @Test
    fun test11Column() {
        val row = createSquareBoard(4).getColumn(1..2, 3)
        assertEquals("Wrong column for 'createSquareBoard(4).getColumn(1..2, 3)'.",
                "[(1, 3), (2, 3)]", row.toString())
    }

    @Test
    fun test12RowReversedRange() {
        val row = createSquareBoard(4).getRow(1, 4 downTo 1)
        assertEquals("Wrong column for 'createSquareBoard(4).getRow(1, 4 downTo 1)'.",
                "[(1, 4), (1, 3), (1, 2), (1, 1)]", row.toString())
    }

    @Test
    fun test13ColumnReversedRange() {
        val row = createSquareBoard(4).getColumn(2 downTo 1, 3)
        assertEquals("Wrong column for 'createSquareBoard(4).getColumn(2 downTo 1, 3)'.",
                "[(2, 3), (1, 3)]", row.toString())
    }

    @Test
    fun test14ColumnWrongRange() {
        val row = createSquareBoard(4).getColumn(3..6, 2)
        assertEquals("Wrong column for 'createSquareBoard(4).getColumn(3..6, 2)'.",
                "[(3, 2), (4, 2)]", row.toString())
    }

    private fun neighbourMessage(cell: Cell, direction: Direction) =
            "Wrong neighbour for the cell $cell in a direction $direction."

    @Test
    fun test15Neighbour() {
        with(createSquareBoard(4)) {
            val cell = getCellOrNull(2, 3)
            assertNotNull("The board of width 4 should contain the cell (2, 3).", cell)
            assertEquals(neighbourMessage(cell!!, UP), "(1, 3)", cell.getNeighbour(UP).toString())
            assertEquals(neighbourMessage(cell, DOWN), "(3, 3)", cell.getNeighbour(DOWN).toString())
            assertEquals(neighbourMessage(cell, LEFT), "(2, 2)", cell.getNeighbour(LEFT).toString())
            assertEquals(neighbourMessage(cell, RIGHT), "(2, 4)", cell.getNeighbour(RIGHT).toString())
        }
    }

    @Test
    fun test16NullableNeighbour() {
        with(createSquareBoard(4)) {
            val cell = getCellOrNull(4, 4)
            assertNotNull("The board of width 4 should contain the cell (4, 4).", cell)
            assertEquals(neighbourMessage(cell!!, UP), "(3, 4)", cell.getNeighbour(UP).toString())
            assertEquals(neighbourMessage(cell, LEFT), "(4, 3)", cell.getNeighbour(LEFT).toString())
            assertEquals(neighbourMessage(cell, DOWN), null, cell.getNeighbour(DOWN))
            assertEquals(neighbourMessage(cell, RIGHT), null, cell.getNeighbour(RIGHT))
        }
    }

    @Test
    fun test17TheSameCell() {
        val board = createSquareBoard(4)
        val first = board.getCell(1, 2)
        val second = board.getCellOrNull(1, 2)
        assertTrue("'getCell' and 'getCellOrNull' should return the same 'Cell' instances.\n" +
                "Create only 'width * width' cells; all the functions working with cells " +
                "should return existing cells instead of creating new ones.",
                first === second)
    }

    @Test
    fun test18TheSameCell() {
        val board = createSquareBoard(1)
        val first = board.getAllCells().first()
        val second = board.getCell(1, 1)
        assertTrue("'getAllCells' and 'getCell' should return the same 'Cell' instances.\n" +
                "Create only 'width * width' cells; all the functions working with cells " +
                "should return existing cells instead of creating new ones.",
                first === second)
    }

    @Test
    fun test19TheSameCell() {
        val board = createSquareBoard(4)
        val cell = board.getCell(1, 1)
        val first = board.run { cell.getNeighbour(RIGHT) }
        val second = board.getCell(1, 2)
        assertTrue("'getNeighbour' shouldn't recreate the 'Cell' instance.\n" +
                "Create only 'width * width' cells; all the functions working with cells " +
                "should return existing cells instead of creating new ones.",
                first === second)
    }

    @Test
    fun test20TheSameCell() {
        val board = createSquareBoard(2)
        val row = board.getRow(1, 1..1)
        val first = row[0]
        val second = board.getCell(1, 1)
        assertTrue("'getRow' shouldn't recreate the 'Cell' instances.\n" +
                "Create only 'width * width' cells; all the functions working with cells " +
                "should return existing cells instead of creating new ones.",
                first === second)
    }

    @Test
    fun test21TheSameCell() {
        val board = createSquareBoard(2)
        val column = board.getColumn(1..1, 2)
        val first = column[0]
        val second = board.getCell(1, 2)
        assertTrue("'getColumn' shouldn't recreate the 'Cell' instances.\n" +
                "Create only 'width * width' cells; all the functions working with cells " +
                "should return existing cells instead of creating new ones.",
                first === second)
    }

    @Test
    fun test22GetNeighbours_Center() {
        testGetNeighbours(setOf(
            Cell(1, 1), Cell(1, 2), Cell(1, 3),
            Cell(2, 1), Cell(2, 3),
            Cell(3, 1), Cell(3, 2), Cell(3, 3)),
            2, 2)
    }

    @Test
    fun test23GetNeighbours_Corner() {
        testGetNeighbours(setOf(
            Cell(1, 2), Cell(2, 2), Cell(2, 1)),
            1, 1)
    }

    @Test
    fun test24GetNeighbours_MidBorder() {
        testGetNeighbours(setOf(
            Cell(1, 1), Cell(1, 2),
            Cell(2, 2),
            Cell(3, 1), Cell(3, 2)),
            2, 1)
    }

    private fun testGetNeighbours(expectedNeighbours: Set<Cell>, row: Int, col: Int, width: Int = 3) {
        with(createSquareBoard(width)) {
            val cell = getCell(row, col)
            val neighbours = cell.getNeighbours().toSet()
            assertEquals(expectedNeighbours, neighbours)
        }
    }
}
