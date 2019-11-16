package zh.games.minesweeper.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import zh.games.minesweeper.board.Cell

class MineSweeperGameTest {
    @Test
    fun numberOfInitializedCellsIsMineLimit() {
        val width = 30
        val mineLimit = 20
        val game = getMineSweeperGame(width, mineLimit)
        val numberOfMines = getAllCells(width)
            .map { cell -> game[cell.i, cell.j] }
            .filter { it == -1 }
            .count()
        assertEquals(numberOfMines, mineLimit)
    }

    @Test
    fun wonWhenAllMinesSecuredAndOtherCellsOpen() {
        val (game, minedCells, emptyCells) = getGameWithSplitCells()

        minedCells.forEach { (i, j) -> game.toggleSecure(i, j) }
        emptyCells.forEach { (i, j) -> game.open(i, j) }

        assertTrue(game.hasWon())
    }

    @Test
    fun doNotWinWhenAtLeastOneMinedCellNotSecured() {
        val (game, minedCells, emptyCells) = getGameWithSplitCells()

        minedCells
            .shuffled()
            .take(minedCells.size - 1)
            .forEach { (i, j) -> game.toggleSecure(i, j) }
        emptyCells.forEach { (i, j) -> game.open(i, j) }

        assertFalse(game.hasWon())
    }

    @Test
    fun doNotWinWhenAtLeastOneRegularCellNotOpened() {
        val (game, minedCells, emptyCells) = getGameWithSplitCells()

        minedCells.forEach { (i, j) -> game.toggleSecure(i, j) }
        emptyCells
            .shuffled()
            .take(emptyCells.size - 1)
            .forEach { (i, j) -> game.open(i, j) }

        assertFalse(game.hasWon())
    }

    private fun getGameWithSplitCells(): Triple<Game, List<Cell>, List<Cell>> {
        val width = 10
        val mineLimit = 10
        val game = getMineSweeperGame(width, mineLimit)
        val minedCells = game.getMinedCells(width)
        val emptyCells = getAllCells(width) - minedCells
        return Triple(game, minedCells, emptyCells)
    }


    private fun getMineSweeperGame(width: Int, mineLimit: Int) =
        newMineSweeperGame(width, mineLimit, RandomMineSweeperGameInitializer).apply { initialize() }

    private fun getAllCells(width: Int) = (1..width)
        .map { row -> (1..width).map { col -> Cell(row, col) } }
        .flatten()

    private fun Game.getMinedCells(width: Int) = getAllCells(width)
        .filter { cell -> this[cell.i, cell.j] == -1 }
}
